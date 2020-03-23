package snake04;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

public class Snake extends KeyAdapter {
	public static int NONE = 0;
	public static int LEFT = 1;
	public static int RIGHT = 2;
	public static int UP = 3;
	public static int DOWN = 4;

	private int  direction;
	private int speed;
	
	private int x, y;
	private Graphics g;
	private MyPanal myPanal;
	
	public Snake(int x, int y, MyPanal myPanal) {
		this.x = x;
		this.y = y;
		this.myPanal = myPanal;
		
		speed = 250; // 初识蛇的移动速度
		direction = NONE; // 初始时处于不移动状态
		
		new Thread(new SnakeDriver()).start(); // 启动移动监听线程
	}
	
	private class SnakeDriver implements Runnable {
		@Override
		public void run() {
			while(true) {
				snakeMove();
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public void drawInit(Graphics g) {
		if(this.g == null) {
			this.g = g;			
		}
		g.setColor(Color.ORANGE);
		g.fillRoundRect(this.x, 
				this.y, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/5, 
				MainFrame.GAP_SIZE/5);
	}
	
	public void snakeMove() {
		if(direction != NONE) {
			clearSnake(x, y);
			if(direction == LEFT) {
				x = x - MainFrame.GAP_SIZE;
			} else if(direction == RIGHT) {
				x = x + MainFrame.GAP_SIZE;
			} else if(direction == UP) {
				y = y - MainFrame.GAP_SIZE;
			} else if(direction == DOWN) {
				y = y + MainFrame.GAP_SIZE;
			}
			// 处理蛇的边界
			if(x <= 0 || x >= (MainFrame.GAME_WIDTH - MainFrame.GAP_SIZE)) {
				drawSnake(x, y);
				JOptionPane.showMessageDialog(myPanal, "碰墙了，游戏结束", "", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
			if(y <= 0 || y >= (MainFrame.GAME_HEIGHT - MainFrame.GAP_SIZE)) {
				drawSnake(x, y);
				JOptionPane.showMessageDialog(myPanal, "碰墙了，游戏结束", "", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			drawSnake(x, y);
		}
	}

	public void drawSnake(int x, int y) {
		this.g.setColor(Color.ORANGE);
		this.g.fillRoundRect(x, 
				y, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/4, 
				MainFrame.GAP_SIZE/4);
	}
	
	public void clearSnake(int x, int y) {
		this.g.setColor(Color.WHITE);
		this.g.fillRect(x, y, MainFrame.GAP_SIZE, MainFrame.GAP_SIZE);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			direction = LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			direction = RIGHT;
			break;
		case KeyEvent.VK_UP:
			direction = UP;
			break;
		case KeyEvent.VK_DOWN:
			direction = DOWN;
			break;
		default:
			System.out.println("请使用方向键进行控制");
			break;
		}		
	}
	
}
