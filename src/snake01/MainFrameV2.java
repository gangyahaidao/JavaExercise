package snake01;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}

class MyFrame extends Frame {
	private MyWindowListener winListener;
	
	public MyFrame(String title) {
		winListener = new MyWindowListener();
		
		setTitle(title);
		setBounds(300, 200, 400, 400);
		setBackground(Color.GRAY);
		addWindowListener(winListener);
		setResizable(false);
		setVisible(true);
	}
}

public class MainFrameV2{

	public static void main(String[] args) {		
		MyFrame myframe = new MyFrame("贪吃蛇");		
	}
}
