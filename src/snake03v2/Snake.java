package snake03v2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Snake extends KeyAdapter {
	public static int LEFT = 1;
	public static int RIGHT = 2;
	public static int UP = 3;
	public static int DOWN = 4;
	
	private int x, y;
	private Graphics g;
	private MyPanal myPanal;
	
	
	public Snake(int x, int y, MyPanal myPanal) {
		this.x = x;
		this.y = y;
		this.myPanal = myPanal;
	}
	
	public void drawInit(Graphics g) {
		if(this.g == null) {
			this.g = g;			
		}
		g.setColor(Color.ORANGE);
		g.fillRoundRect(this.x, 
				this.y, 
				MainFrame03v2.GAP_SIZE, 
				MainFrame03v2.GAP_SIZE, 
				MainFrame03v2.GAP_SIZE/5, 
				MainFrame03v2.GAP_SIZE/5);
	}
	
	public void drawSnake(int x, int y) {
		this.g.setColor(Color.ORANGE);
		this.g.fillRoundRect(x, 
				y, 
				MainFrame03v2.GAP_SIZE, 
				MainFrame03v2.GAP_SIZE, 
				MainFrame03v2.GAP_SIZE/5, 
				MainFrame03v2.GAP_SIZE/5);
	}
	
	public void clearSnake(int x, int y) {
		this.g.setColor(Color.WHITE);
		this.g.fillRect(x, y, MainFrame03v2.GAP_SIZE, MainFrame03v2.GAP_SIZE);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			clearSnake(x, y);
			x = x - MainFrame03v2.GAP_SIZE;
			break;
		case KeyEvent.VK_RIGHT:
			clearSnake(x, y);
			x = x + MainFrame03v2.GAP_SIZE;
			break;
		case KeyEvent.VK_UP:
			clearSnake(x, y);
			y = y - MainFrame03v2.GAP_SIZE;
			break;
		case KeyEvent.VK_DOWN:
			clearSnake(x, y);
			y = y + MainFrame03v2.GAP_SIZE;
			break;
		default:
			System.out.println("请使用方向键进行控制");
			break;
		}
		// 处理蛇的边界
		if(x <= 0) {
			x = 0;
		} else if(x >= (MainFrame03v2.GAME_WIDTH - MainFrame03v2.GAP_SIZE)) {
			x = MainFrame03v2.GAME_WIDTH - MainFrame03v2.GAP_SIZE;
		}
		if(y <= 0) {
			y = 0;
		} else if(y >= (MainFrame03v2.GAME_HEIGHT - MainFrame03v2.GAP_SIZE)) {
			y = MainFrame03v2.GAME_HEIGHT - MainFrame03v2.GAP_SIZE;
		}
		//myPanal.repaint();
		drawSnake(x, y);
	}
	
}
