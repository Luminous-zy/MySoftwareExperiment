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
  /** ����. */
  private static GUI frame;
  /** ϸ��. */
  private Cell cell;
  /** ���Ϳ�. */
  private int length = 20, width = 10;
  /** �����ʾϸ��. */
  private JButton[][] square;
  /** ��ť�Ƿ�ѡ��. */
  private boolean[][] isSelected;
  /** ��ʾ��ǰ����. */
  private JButton jNowGeneration;
  /** ���ϸ������һ���������ʼ������ʼ���ܣ���ͣ���˳�. */
  private MenuItem clearCell, nextGeneration, randomInit, start, stop, exit;
  /** �߳�. */
  private Thread thread;
  /** �߳��Ƿ�������. */
  private boolean isRunning;
  /** ϸ���Ƿ�����. */
  private boolean isDead;
  private MenuBar menuBar = new MenuBar();
  private Font f1 = new Font("����", Font.BOLD, 16);
  private Font f2 = new Font("����", Font.BOLD, 20);
  private Menu menu2, menu3;

  /**
   * �������.
   *
   * @param args ���������в���
   */
  public static void main(final String[] args) {
    frame = new GUI("������Ϸ��LifeGame��");
  }

  /** ��ʼ������. */
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
        square[i][j] = new JButton(""); // ��ť�����ÿ��Ա�ʾϸ��
        square[i][j].setBackground(Color.WHITE); // ��ʼ����ϸ����Ϊ��
        centerPanel.add(square[i][j]);
      }
    }

    menu2 = new Menu("ϵͳ����");
    menu3 = new Menu("ϸ������");

    jLength = new JLabel("��ǰ���ȣ�20  ");
    jWidth = new JLabel("��ǰ��ȣ�10  ");
    nowGeneration = new JLabel("��ǰ������");
    jNowGeneration = new JButton("" + cell.getNowGeneration());
    jNowGeneration.setEnabled(false);
    jLength.setFont(f2);
    jWidth.setFont(f2);
    nowGeneration.setFont(f2);

    randomInit = new MenuItem("�����ʼ��");
    clearCell = new MenuItem("ϸ������");
    start = new MenuItem("��ʼ����");
    nextGeneration = new MenuItem("������һ��");
    stop = new MenuItem("��ͣ����");
    exit = new MenuItem("�˳�(Exit)");

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

    this.setMenuBar(menuBar); // ���˴���Ĳ˵�������Ϊָ���Ĳ˵���
    this.setSize(800, 520); // ���ڴ�С
    this.setVisible(true); // ���ڿɼ�
    this.setResizable(true);
    this.setLocationRelativeTo(null); // ���ھ���

    this.addWindowListener(new WindowAdapter() { // ע�������
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
   * �½�����.
   *
   * @param name �������
   */
  public GUI(final String name) {
    super(name);
    initGUI();
  }

  /**
   * ���ղ����¼�.
   *
   * @param e �����¼�
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    final int sleeptime = 500; // �߳�˯�ߵ�ʱ����
    if (e.getSource() == randomInit) { // �����ʼ��
      cell.randomCell();
      showCell();
      isRunning = false;
      thread = null;
    } else if (e.getSource() == clearCell) { // ϸ������
      cell.deleteAllCell();
      showCell();
      isRunning = false;
      thread = null;
    } else if (e.getSource() == start) { // ��ʼ
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
              JOptionPane.showMessageDialog(null, "����ϸ����������");
              isRunning = false;
              thread = null;
            }
          }
        }
      });
      thread.start();
    } else if (e.getSource() == nextGeneration) { // ��һ��
      makeNextGeneration();
      isRunning = false;
      thread = null;
    } else if (e.getSource() == stop) { // ��ͣ
      isRunning = false;
      thread = null;
    } else if (e.getSource() == exit) { // �˳�
      frame.dispose();
      System.exit(0);
    } else { // ϸ���ĵ��ѡ��
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

  /** ��һ��. */
  private void makeNextGeneration() {
    cell.update();
    showCell();
    jNowGeneration.setText("" + cell.getNowGeneration()); // ˢ�´���
  }

  /** ��ϸ�����ص�������. */
  public void showCell() {
    int[][] grid = cell.getStatus();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        if (grid[i + 1][j + 1] == 1) {
          square[i][j].setBackground(Color.BLACK); // ��ɫ��ʾ��ϸ��
        } else {
          square[i][j].setBackground(Color.WHITE); // ��ɫ��ʾ��ϸ��
        }
      }
    }
  }

}
