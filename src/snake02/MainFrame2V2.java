package snake02;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
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
		setBounds(300, 200, MainFrame2V2.GAME_WIDTH, MainFrame2V2.GAME_HEIGHT);
		setBackground(Color.GRAY);
		addWindowListener(winListener);
		setResizable(false);
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		int col = MainFrame2V2.GAME_WIDTH/MainFrame2V2.GAP_SIZE;
		int row = MainFrame2V2.GAME_HEIGHT/MainFrame2V2.GAP_SIZE;
		
		g.setColor(Color.WHITE);
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				g.fill3DRect(i*MainFrame2V2.GAP_SIZE, j*MainFrame2V2.GAP_SIZE, MainFrame2V2.GAP_SIZE, MainFrame2V2.GAP_SIZE, true);
			}			
		}
	}
	
}

public class MainFrame2V2{	
	public static final int GAME_WIDTH =800;
	public static final int GAME_HEIGHT =800;
	public static final int	GAP_SIZE = 25;

	public static void main(String[] args) {		
		MyFrame myframe = new MyFrame("贪吃蛇");		
	}
}
