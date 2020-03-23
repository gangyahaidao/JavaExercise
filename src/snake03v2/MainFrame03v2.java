package snake03v2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindowListenerV3 extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}

class MyPanal extends Panel {
	private Snake snake;
	
	public MyPanal() {
		setBounds(4, 28, MainFrame03v2.GAME_WIDTH, MainFrame03v2.GAME_HEIGHT);
		setBackground(Color.WHITE);
		setFocusable(true);
		
		snake = new Snake(MainFrame03v2.GAME_WIDTH/MainFrame03v2.GAP_SIZE/2*MainFrame03v2.GAP_SIZE,
				MainFrame03v2.GAME_HEIGHT/MainFrame03v2.GAP_SIZE/2*MainFrame03v2.GAP_SIZE,
				this);
		addKeyListener(snake);
	}
	
	public void paint(Graphics g) {
		int col = MainFrame03v2.GAME_WIDTH/MainFrame03v2.GAP_SIZE;
		int row = MainFrame03v2.GAME_HEIGHT/MainFrame03v2.GAP_SIZE;
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {		
				if(i == 0 || i == row-1 || j == 0 || j == col-1) {
					g.setColor(Color.DARK_GRAY);
					g.fill3DRect(i*MainFrame03v2.GAP_SIZE, j*MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, true);
				} else {
					g.setColor(Color.WHITE);
					//g.fill3DRect(i*MainFrame03v2.GAP_SIZE, j*MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, false);
					g.fillRect(i*MainFrame03v2.GAP_SIZE, j*MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE);
				}
				
			}			
		}
		snake.drawInit(getGraphics());
	}
	
	
}

class MyFrame extends Frame {
	private MyWindowListenerV3 winListener;
	private MyPanal mypanal;
	
	public MyFrame(String title) {
		winListener = new MyWindowListenerV3();
		mypanal = new MyPanal();
		
		setTitle(title);
		Toolkit tool = getToolkit();
		Dimension dim = tool.getScreenSize();
		setBounds((dim.width-(MainFrame03v2.GAME_WIDTH+8))/2, 
				(dim.height-(MainFrame03v2.GAME_HEIGHT+32))/2, 
				MainFrame03v2.GAME_WIDTH+8, 
				MainFrame03v2.GAME_HEIGHT+32);
		setBackground(Color.WHITE);
		setLayout(null);
		add(mypanal);
		addWindowListener(winListener);		
		setResizable(false);
		setVisible(true);
	}	
}

public class MainFrame03v2 {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 800;
	public static final int	GAP_SIZE = 25;

	public static void main(String[] args) {
		MyFrame myframe = new MyFrame("贪吃蛇");
	}

}
