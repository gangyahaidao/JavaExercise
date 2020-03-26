package snake02;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 对v2版本窗口进行区域划分，同时调整一下绘制格子的样式
 * */
class MyPanel extends Panel {
	public MyPanel() {
		setBounds(4, 28, MainFrame2V3.GAME_WIDTH, MainFrame2V3.GAME_HEIGHT); // 设置面板在窗口中的相对位置及大小
		setBackground(Color.WHITE);
	}
	
	//覆盖父类的绘制方法，实现我们自己的绘制，是我们的窗口自动调用的，不是我们调用的
	public void paint(Graphics g) {
		int col = MainFrame2V3.GAME_WIDTH/MainFrame2V3.GAP_SIZE; // 格子的列数
		int row = MainFrame2V3.GAME_HEIGHT/MainFrame2V3.GAP_SIZE; // 行数
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(i == 0 || i == row-1 || j == 0 || j == col-1) { // 开始绘制墙
					g.setColor(Color.DARK_GRAY);
					g.fill3DRect(i*MainFrame2V3.GAP_SIZE, 
							j*MainFrame2V3.GAP_SIZE, 
							MainFrame2V3.GAP_SIZE, 
							MainFrame2V3.GAP_SIZE, 
							true);
				} else {
					g.setColor(Color.WHITE); // 重新设置画笔颜色
					g.fill3DRect(i*MainFrame2V3.GAP_SIZE, 
							j*MainFrame2V3.GAP_SIZE, 
							MainFrame2V3.GAP_SIZE, 
							MainFrame2V3.GAP_SIZE, 
							false);
				}			
			}
		}
	}
}

class MyWindowListener3 extends WindowAdapter {
	// override覆盖父类的关闭方法
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}

class MyFrame extends Frame {
	private MyWindowListener3 myWindowListener3;
	private MyPanel mypanel;
	
	public MyFrame(String title) {
		myWindowListener3 = new MyWindowListener3();
		mypanel = new MyPanel();
		
		setTitle(title); // 设置窗口标题
		setBounds(300, 200, MainFrame2V3.GAME_WIDTH+8, MainFrame2V3.GAME_HEIGHT+32); // 设置窗口所在空间位置及大小
		setBackground(Color.GRAY); // 设置窗口背景色
		setLayout(null); // 把窗口布局设置为null方式
		add(mypanel);
		addWindowListener(myWindowListener3); // 给窗口绑定鼠标点击关闭功能
		
		setResizable(false); // 设置窗口大小不可变
		setVisible(true);		
	}
	
}


public class MainFrame2V3 {
	public static final int GAME_WIDTH =800;
	public static final int GAME_HEIGHT =800;
	public static final int	GAP_SIZE = 25;

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame("贪吃蛇V1");
	}

}
