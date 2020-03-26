package snake02;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindowListener extends WindowAdapter {
	// override覆盖父类的关闭方法
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}

class MyFrame extends Frame {
	private MyWindowListener myWindowListener;
	
	public MyFrame(String title) {
		myWindowListener = new MyWindowListener();
		setTitle(title); // 设置窗口标题
		setBounds(300, 200, MainFrame2V2.GAME_WIDTH, MainFrame2V2.GAME_HEIGHT); // 设置窗口所在空间位置及大小
		setBackground(Color.GRAY); // 设置窗口背景色
		addWindowListener(myWindowListener); // 给窗口绑定鼠标点击关闭功能
		
		setResizable(false); // 设置窗口大小不可变
		setVisible(true);		
	}
	
	//覆盖父类的绘制方法，实现我们自己的绘制，是我们的窗口自动调用的，不是我们调用的
	public void paint(Graphics g) {
		int col = MainFrame2V2.GAME_WIDTH/MainFrame2V2.GAP_SIZE; // 格子的列数
		int row = MainFrame2V2.GAME_HEIGHT/MainFrame2V2.GAP_SIZE; // 行数
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				g.fill3DRect(i*MainFrame2V2.GAP_SIZE, 
						j*MainFrame2V2.GAP_SIZE, 
						MainFrame2V2.GAP_SIZE, 
						MainFrame2V2.GAP_SIZE, 
						true);
			}
		}
	}
}


public class MainFrame2V2 {
	public static final int GAME_WIDTH =800;
	public static final int GAME_HEIGHT =800;
	public static final int	GAP_SIZE = 25;

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame("贪吃蛇V1");
	}

}
