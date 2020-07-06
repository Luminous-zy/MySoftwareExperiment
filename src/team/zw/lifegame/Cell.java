package team.zw.lifegame;

/** Cell为细胞类，以实现数据封装和实现与细胞有关的方法. */
public class Cell {
  /** 长度. */
  private int length;
  /** 宽度. */
  private int width;
  /** 当前代数. */
  private int nowGeneration;
  /** 细胞状态，0代表死亡，1代表存活. */
  private int[][] status;

  /**
   * Cell类的构造函数.
   *
   * @param l 形参：长度
   * @param w 形参：宽度
   */
  public Cell(final int l, final int w) {
    length = l;
    width = w;
    nowGeneration = 0;
    status = new int[length + 2][width + 2];
    for (int i = 0; i <= length + 1; i++) {
      for (int j = 0; j <= width + 1; j++) {
        status[i][j] = 0;
      }
    }
  }

  /**
   * 实现数据封装.
   *
   * @param s 形参：细胞
   */
  public void setStatus(final int[][] s) {
    status = s;
  }

  /**
   * 实现数据封装.
   *
   * @return 细胞
   */
  public int[][] getStatus() {
    return status;
  }

  /**
   * 实现数据封装.
   *
   * @param n 形参：当前代数
   */
  public void setNowGeneration(final int n) {
    nowGeneration = n;
  }

  /**
   * 实现数据封装.
   *
   * @return 当前代数
   */
  public int getNowGeneration() {
    return nowGeneration;
  }

  /** 随机初始化. */
  public void randomCell() {
    final double p = 0.5; // 细胞随机初始化为活的概率
    for (int i = 1; i <= length; i++) {
      for (int j = 1; j <= width; j++) {
        status[i][j] = Math.random() > p ? 1 : 0;
      }
    }
  }

  /** 细胞清零. */
  public void deleteAllCell() {
    for (int i = 1; i <= length; i++) {
      for (int j = 1; j <= width; j++) {
        status[i][j] = 0;
      }
    }
  }

  /** 更新演化. */
  public void update() {
    int[][] newStatus = new int[length + 2][width + 2];
    for (int i = 1; i <= length; i++) {
      final int a = 3; // 细胞为活的邻居数量
      for (int j = 1; j <= width; j++) {
        switch (getNeighborCount(i, j)) {
        case 2:
          newStatus[i][j] = status[i][j]; // 细胞状态保持不变
          break;
        case a:
          newStatus[i][j] = 1; // 此时细胞活
          break;
        default:
          newStatus[i][j] = 0; // 此时细胞死
        }
      }
    }
    for (int i = 1; i <= length; i++) {
      for (int j = 1; j <= width; j++) {
        status[i][j] = newStatus[i][j];
      }
    }
    nowGeneration++;
  }

  /**
   * 获取邻居细胞的存活数量.
   *
   * @return 细胞邻居数量
   * @param i1 行
   * @param j1 列
   */
  int getNeighborCount(final int i1, final int j1) {
    int count = 0;
    for (int i = i1 - 1; i <= i1 + 1; i++) {
      for (int j = j1 - 1; j <= j1 + 1; j++) {
        count += status[i][j]; // 邻居存活，数量+1
      }
      count -= status[i1][j1]; // 减去自身的状态
    }
    return count;
  }
}
