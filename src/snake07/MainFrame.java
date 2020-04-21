package snake07;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v7,贪吃蛇移动与服务器进行通信功能
 * */
class MyPanel extends Panel {
	private Snake snake;
	
	public MyPanel() {
		setBounds(4, 28, MainFrame.GAME_WIDTH, MainFrame.GAME_HEIGHT); // 设置面板在窗口中的相对位置及大小
		setBackground(Color.WHITE);
		setFocusable(true); // 启动时获取鼠标焦点，这样可以响应键盘按键操作
		
		snake = new Snake(MainFrame.GAME_WIDTH/2, 
				MainFrame.GAME_HEIGHT/2, this);		
		addKeyListener(snake);
	}
	
	//覆盖父类的绘制方法，实现我们自己的绘制，是我们的窗口自动调用的，不是我们调用的
	public void paint(Graphics g) {
		int col = MainFrame.GAME_WIDTH/MainFrame.GAP_SIZE; // 格子的列数
		int row = MainFrame.GAME_HEIGHT/MainFrame.GAP_SIZE; // 行数
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(i == 0 || i == row-1 || j == 0 || j == col-1) { // 开始绘制墙
					g.setColor(Color.DARK_GRAY);
					g.fill3DRect(i*MainFrame.GAP_SIZE, 
							j*MainFrame.GAP_SIZE, 
							MainFrame.GAP_SIZE, 
							MainFrame.GAP_SIZE, 
							true);
				} else {
					g.setColor(Color.WHITE); // 重新设置画笔颜色
					g.fillRect(i*MainFrame.GAP_SIZE, 
							j*MainFrame.GAP_SIZE, 
							MainFrame.GAP_SIZE, 
							MainFrame.GAP_SIZE);
				}			
			}
		}		
		// 在画布中间显示蛇头，此处传递的画笔g只在paint方法中才有效，方法返回引用将不再有效
		// 所以此处传递过去的画笔需要用getGraphics()方法重新获取画笔对象
		snake.snakeInit(getGraphics());
	}
}

class MyWindowListener extends WindowAdapter {
	// override覆盖父类的关闭方法
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}

class MyFrame extends Frame {
	private MyWindowListener myWindowListener;
	private MyPanel mypanel;
	
	public MyFrame(String title) {
		myWindowListener = new MyWindowListener();
		mypanel = new MyPanel();
		
		setTitle(title); // 设置窗口标题
		setBounds(300, 200, MainFrame.GAME_WIDTH+8, MainFrame.GAME_HEIGHT+32); // 设置窗口所在空间位置及大小
		setBackground(Color.GRAY); // 设置窗口背景色
		setLayout(null); // 把窗口布局设置为null方式
		add(mypanel);
		addWindowListener(myWindowListener); // 给窗口绑定鼠标点击关闭功能
		
		setResizable(false); // 设置窗口大小不可变
		setVisible(true);		
	}
	
}

public class MainFrame {
	public static final int GAME_WIDTH =800;
	public static final int GAME_HEIGHT =800;
	public static final int	GAP_SIZE = 25;

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame("贪吃蛇V6");
	}

}
