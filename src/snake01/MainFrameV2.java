package snake01;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
		setBounds(300, 200, 400, 400); // 设置窗口所在空间位置及大小
		setBackground(Color.GRAY); // 设置窗口背景色
		addWindowListener(myWindowListener); // 给窗口绑定鼠标点击关闭功能
		
		setResizable(false); // 设置窗口大小不可变
		setVisible(true);		
	}
}


public class MainFrameV2 {

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame("贪吃蛇V1");
	}

}
