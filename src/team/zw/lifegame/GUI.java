package team.zw.lifegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener {
  /** 界面. */
  private static GUI frame;
  /** 细胞. */
  private Cell cell;
  /** 长和宽. */
  private int length = 20, width = 10;
  /** 方格表示细胞. */
  private JButton[][] square;
  /** 按钮是否被选中. */
  private boolean[][] isSelected;
  /** 显示当前代数. */
  private JButton jNowGeneration;
  /** 清除细胞，下一代，随机初始化，开始繁衍，暂停，退出. */
  private MenuItem clearCell, nextGeneration, randomInit, start, stop, exit;
  /** 线程. */
  private Thread thread;
  /** 线程是否在运行. */
  private boolean isRunning;
  /** 细胞是否死亡. */
  private boolean isDead;
  private MenuBar menuBar = new MenuBar();
  private Font f1 = new Font("宋体", Font.BOLD, 16);
  private Font f2 = new Font("仿宋", Font.BOLD, 20);
  private Menu menu2, menu3;

  /**
   * 程序入口.
   *
   * @param args 接收命令行参数
   */
  public static void main(final String[] args) {
    frame = new GUI("生命游戏（LifeGame）");
  }

  /** 初始化界面. */
  public void initGUI() {

    cell = new Cell(width, length);

    JPanel backPanel;
    JPanel centerPanel;
    JPanel upPanel;
    JLabel jWidth;
    JLabel jLength;
    JLabel nowGeneration;
    backPanel = new JPanel(new BorderLayout());
    centerPanel = new JPanel(new GridLayout(width, length));
    upPanel = new JPanel();
    this.setContentPane(backPanel);
    backPanel.add(centerPanel, "Center");
    backPanel.add(upPanel, "North");

    square = new JButton[width][length];
    isSelected = new boolean[width][length];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        square[i][j] = new JButton(""); // 按钮内容置空以表示细胞
        square[i][j].setBackground(Color.WHITE); // 初始所有细胞均为死
        centerPanel.add(square[i][j]);
      }
    }

    menu2 = new Menu("系统操作");
    menu3 = new Menu("细胞操作");

    jLength = new JLabel("当前长度：20  ");
    jWidth = new JLabel("当前宽度：10  ");
    nowGeneration = new JLabel("当前代数：");
    jNowGeneration = new JButton("" + cell.getNowGeneration());
    jNowGeneration.setEnabled(false);
    jLength.setFont(f2);
    jWidth.setFont(f2);
    nowGeneration.setFont(f2);

    randomInit = new MenuItem("随机初始化");
    clearCell = new MenuItem("细胞清零");
    start = new MenuItem("开始繁衍");
    nextGeneration = new MenuItem("更新下一代");
    stop = new MenuItem("暂停繁衍");
    exit = new MenuItem("退出(Exit)");

    upPanel.add(jLength);
    upPanel.add(jWidth);
    upPanel.add(nowGeneration);
    upPanel.add(jNowGeneration);
    upPanel.setBackground(Color.lightGray);

    menu2.add(stop);
    menu2.add(exit);

    menu3.add(start);
    menu3.add(randomInit);
    menu3.add(clearCell);
    menu3.add(nextGeneration);

    menuBar.add(menu3);
    menuBar.add(menu2);
    menuBar.setFont(f1);

    this.setMenuBar(menuBar); // 将此窗体的菜单栏设置为指定的菜单栏
    this.setSize(800, 520); // 窗口大小
    this.setVisible(true); // 窗口可见
    this.setResizable(true);
    this.setLocationRelativeTo(null); // 窗口居中

    this.addWindowListener(new WindowAdapter() { // 注册监听器
      @Override
      public void windowClosed(final WindowEvent e) {
        System.exit(0);
      }
    });

    randomInit.addActionListener(this);
    clearCell.addActionListener(this);
    nextGeneration.addActionListener(this);
    start.addActionListener(this);
    stop.addActionListener(this);
    exit.addActionListener(this);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        square[i][j].addActionListener(this);
      }
    }
  }

  /**
   * 新建界面.
   *
   * @param name 界面标题
   */
  public GUI(final String name) {
    super(name);
    initGUI();
  }

  /**
   * 接收操作事件.
   *
   * @param e 操作事件
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    final int sleeptime = 500; // 线程睡眠的时间数
    if (e.getSource() == randomInit) { // 随机初始化
      cell.randomCell();
      showCell();
      isRunning = false;
      thread = null;
    } else if (e.getSource() == clearCell) { // 细胞清零
      cell.deleteAllCell();
      showCell();
      isRunning = false;
      thread = null;
    } else if (e.getSource() == start) { // 开始
      isRunning = true;
      thread = new Thread(new Runnable() {
        @Override
        public void run() {
          while (isRunning) {
            makeNextGeneration();
            try {
              Thread.sleep(sleeptime);
            } catch (InterruptedException e1) {
              e1.printStackTrace();
            }
            isDead = true;
            for (int row = 1; row <= width; row++) {
              for (int col = 1; col <= length; col++) {
                if (cell.getStatus()[row][col] != 0) {
                  isDead = false;
                  break;
                }
              }
              if (!isDead) {
                break;
              }
            }
            if (isDead) {
              JOptionPane.showMessageDialog(null, "所有细胞已死亡！");
              isRunning = false;
              thread = null;
            }
          }
        }
      });
      thread.start();
    } else if (e.getSource() == nextGeneration) { // 下一代
      makeNextGeneration();
      isRunning = false;
      thread = null;
    } else if (e.getSource() == stop) { // 暂停
      isRunning = false;
      thread = null;
    } else if (e.getSource() == exit) { // 退出
      frame.dispose();
      System.exit(0);
    } else { // 细胞的点击选择
      int[][] status = cell.getStatus();
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < length; j++) {
          if (e.getSource() == square[i][j]) {
            isSelected[i][j] = !isSelected[i][j];
            if (isSelected[i][j]) {
              square[i][j].setBackground(Color.BLACK);
              status[i + 1][j + 1] = 1;
            } else {
              square[i][j].setBackground(Color.WHITE);
              status[i + 1][j + 1] = 0;
            }
            break;
          }
        }
      }
      cell.setStatus(status);
    }
  }

  /** 下一代. */
  private void makeNextGeneration() {
    cell.update();
    showCell();
    jNowGeneration.setText("" + cell.getNowGeneration()); // 刷新代数
  }

  /** 将细胞加载到界面上. */
  public void showCell() {
    int[][] grid = cell.getStatus();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        if (grid[i + 1][j + 1] == 1) {
          square[i][j].setBackground(Color.BLACK); // 黑色表示活细胞
        } else {
          square[i][j].setBackground(Color.WHITE); // 白色表示死细胞
        }
      }
    }
  }

}
