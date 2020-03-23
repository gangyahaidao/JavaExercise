package snake06;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 相比于上一个版本增加定时移动功能，并到墙边时能停下
 * */
class MyWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}

class MyPanal extends Panel {
	private Snake snake;
	
	public MyPanal() {
		setBounds(4, 28, MainFrame.GAME_WIDTH, MainFrame.GAME_HEIGHT);
		setBackground(Color.WHITE);
		setFocusable(true);
		
		snake = new Snake(MainFrame.GAME_WIDTH/MainFrame.GAP_SIZE/2*MainFrame.GAP_SIZE,
				MainFrame.GAME_HEIGHT/MainFrame.GAP_SIZE/2*MainFrame.GAP_SIZE,
				this);
		addKeyListener(snake);
	}
	
	public void paint(Graphics g) {
		int col = MainFrame.GAME_WIDTH/MainFrame.GAP_SIZE;
		int row = MainFrame.GAME_HEIGHT/MainFrame.GAP_SIZE;
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {		
				if(i == 0 || i == row-1 || j == 0 || j == col-1) {
					g.setColor(Color.DARK_GRAY);
					g.fill3DRect(i*MainFrame.GAP_SIZE, j*MainFrame.GAP_SIZE, MainFrame.GAP_SIZE, MainFrame.GAP_SIZE, true);
				} else {
					g.setColor(Color.WHITE);
					//g.fill3DRect(i*MainFrame03v2.GAP_SIZE, j*MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, false);
					g.fillRect(i*MainFrame.GAP_SIZE, j*MainFrame.GAP_SIZE, MainFrame.GAP_SIZE, MainFrame.GAP_SIZE);
				}
				
			}			
		}
		snake.drawInit(getGraphics());
	}	
}

class MyFrame extends Frame {
	private MyWindowListener winListener;
	private MyPanal mypanal;
	
	public MyFrame(String title) {
		winListener = new MyWindowListener();
		mypanal = new MyPanal();
		
		setTitle(title);
		Toolkit tool = getToolkit();
		Dimension dim = tool.getScreenSize();
		setBounds((dim.width-(MainFrame.GAME_WIDTH+8))/2, 
				(dim.height-(MainFrame.GAME_HEIGHT+32))/2, 
				MainFrame.GAME_WIDTH+8, 
				MainFrame.GAME_HEIGHT+32);
		setBackground(Color.WHITE);
		setLayout(null);
		add(mypanal);
		addWindowListener(winListener);		
		setResizable(false);
		setVisible(true);
	}	
}

public class MainFrame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 800;
	public static final int	GAP_SIZE = 25;

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame("贪吃蛇");
	}

}
