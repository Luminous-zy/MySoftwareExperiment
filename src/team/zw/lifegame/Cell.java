package team.zw.lifegame;

/** CellΪϸ���࣬��ʵ�����ݷ�װ��ʵ����ϸ���йصķ���. */
public class Cell {
  /** ����. */
  private int length;
  /** ���. */
  private int width;
  /** ��ǰ����. */
  private int nowGeneration;
  /** ϸ��״̬��0����������1������. */
  private int[][] status;

  /**
   * Cell��Ĺ��캯��.
   *
   * @param l �βΣ�����
   * @param w �βΣ����
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
   * ʵ�����ݷ�װ.
   *
   * @param s �βΣ�ϸ��
   */
  public void setStatus(final int[][] s) {
    status = s;
  }

  /**
   * ʵ�����ݷ�װ.
   *
   * @return ϸ��
   */
  public int[][] getStatus() {
    return status;
  }

  /**
   * ʵ�����ݷ�װ.
   *
   * @param n �βΣ���ǰ����
   */
  public void setNowGeneration(final int n) {
    nowGeneration = n;
  }

  /**
   * ʵ�����ݷ�װ.
   *
   * @return ��ǰ����
   */
  public int getNowGeneration() {
    return nowGeneration;
  }

  /** �����ʼ��. */
  public void randomCell() {
    final double p = 0.5; // ϸ�������ʼ��Ϊ��ĸ���
    for (int i = 1; i <= length; i++) {
      for (int j = 1; j <= width; j++) {
        status[i][j] = Math.random() > p ? 1 : 0;
      }
    }
  }

  /** ϸ������. */
  public void deleteAllCell() {
    for (int i = 1; i <= length; i++) {
      for (int j = 1; j <= width; j++) {
        status[i][j] = 0;
      }
    }
  }

  /** �����ݻ�. */
  public void update() {
    int[][] newStatus = new int[length + 2][width + 2];
    for (int i = 1; i <= length; i++) {
      final int a = 3; // ϸ��Ϊ����ھ�����
      for (int j = 1; j <= width; j++) {
        switch (getNeighborCount(i, j)) {
        case 2:
          newStatus[i][j] = status[i][j]; // ϸ��״̬���ֲ���
          break;
        case a:
          newStatus[i][j] = 1; // ��ʱϸ����
          break;
        default:
          newStatus[i][j] = 0; // ��ʱϸ����
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
   * ��ȡ�ھ�ϸ���Ĵ������.
   *
   * @return ϸ���ھ�����
   * @param i1 ��
   * @param j1 ��
   */
  int getNeighborCount(final int i1, final int j1) {
    int count = 0;
    for (int i = i1 - 1; i <= i1 + 1; i++) {
      for (int j = j1 - 1; j <= j1 + 1; j++) {
        count += status[i][j]; // �ھӴ�����+1
      }
      count -= status[i1][j1]; // ��ȥ�����״̬
    }
    return count;
  }
}
