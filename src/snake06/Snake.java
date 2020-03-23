package snake06;

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

	public SnakeBit snakeBody[];
	public int snakeLenth;
	public int speed;
	
	private SnakeBit food;
	
	private Graphics g;
	private MyPanal myPanal;
	
	public Snake(int x, int y, MyPanal myPanal) {
		snakeBody = new SnakeBit[100]; // 分配100个对象空间
		
		SnakeBit bit = new SnakeBit(x, y, Color.RED); // 蛇头
		bit.setDirection(NONE); // 初始时蛇处于静止状态
		snakeBody[0] = bit;
		snakeLenth = 1;
		
		this.myPanal = myPanal;
		this.speed = 250; // 初始蛇的移动速度ms
		
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
	
	private SnakeBit produceFood() {
		// 在空间中随机放一个食物
		int x = (int) (Math.floor(Math.random()*(MainFrame.GAME_WIDTH/MainFrame.GAP_SIZE-2)+1)*MainFrame.GAP_SIZE);
		int y = (int) (Math.floor(Math.random()*(MainFrame.GAME_HEIGHT/MainFrame.GAP_SIZE-2)+1)*MainFrame.GAP_SIZE);

		food = new SnakeBit(x, y, Color.GREEN);
		g.setColor(food.getColor());
		g.fill3DRect(food.getX(), food.getY(), MainFrame.GAP_SIZE, MainFrame.GAP_SIZE, true);
		
		return food;
	}
	
	public void drawInit(Graphics g) {
		if(this.g == null) {
			this.g = g;
		}
		g.setColor(snakeBody[0].getColor()); // 蛇头是RED
		g.fillRoundRect(snakeBody[0].getX(), 
				snakeBody[0].getY(), 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/5, 
				MainFrame.GAP_SIZE/5);		
		produceFood(); // 随机创建一个食物对象
	}
	
	public void snakeMove() { // 整体移动策略是：头部增加一节，尾部减少一节
		if(snakeBody[0].getDirection() != NONE) {			
			SnakeBit oldHead = snakeBody[0];
			SnakeBit newHead = null;
			int newX = oldHead.getX(), newY = oldHead.getY(); // 初始值为oldHead的值
			
			if(oldHead.getDirection() == LEFT) {
				newX = oldHead.getX() - MainFrame.GAP_SIZE;
			} else if(oldHead.getDirection() == RIGHT) {
				newX = oldHead.getX() + MainFrame.GAP_SIZE;
			} else if(oldHead.getDirection() == UP) {
				newY = oldHead.getY() - MainFrame.GAP_SIZE;
			} else if(oldHead.getDirection() == DOWN) {
				newY = oldHead.getY() + MainFrame.GAP_SIZE;
			}
			// 处理边界
			if(newX <= 0 || newX >= (MainFrame.GAME_WIDTH - MainFrame.GAP_SIZE)) {
				//drawSnake(newX, oldHead.getY(), oldHead.getColor());
				JOptionPane.showMessageDialog(myPanal, "碰墙了，游戏结束", "", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}			
			if(newY <= 0 || newY >= (MainFrame.GAME_HEIGHT - MainFrame.GAP_SIZE)) {
				//drawSnake(oldHead.getX(), newY, oldHead.getColor());
				JOptionPane.showMessageDialog(myPanal, "碰墙了，游戏结束", "", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			if(newX == food.getX() && newY == food.getY()) { // 如果蛇下一步运行位置上有食物，则吃掉
				snakeBody[snakeLenth] = food;
				snakeLenth += 1;				
				produceFood();
			}
			
			oldHead.setColor(Color.ORANGE); // 将原来的蛇头设置为身体的颜色
			drawSnake(oldHead.getX(), oldHead.getY(), oldHead.getColor()); // 重新绘制旧蛇头，颜色与身体颜色一致
			
			newHead = new SnakeBit(newX, newY, Color.RED, oldHead.getDirection()); // 创建新的蛇头对象，颜色红色，方向与旧蛇头方向一致				
			// 将新创建的蛇头对象添加到snakeBody数组的头部
			for(int i = snakeLenth; i > 0; i--) {
				snakeBody[i] = snakeBody[i-1]; // 将所有的元素后移一位
			}
			snakeBody[0] = newHead; // 将蛇头对象插入到最前面
			snakeLenth += 1;
			// 将最后一个元素颜色置为背景色，对象设置为null，蛇的长度-1
			clearSnake(snakeBody[snakeLenth-1].getX(), snakeBody[snakeLenth-1].getY(), Color.WHITE);
			snakeBody[snakeLenth-1] = null;
			snakeLenth -= 1;						
			
			drawSnake(newHead.getX(), newHead.getY(), newHead.getColor()); // 画出新的蛇头
		}
	}

	public void drawSnake(int x, int y, Color color) {
		this.g.setColor(color);
		this.g.fillRoundRect(x, 
				y, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/4, 
				MainFrame.GAP_SIZE/4);
	}
	
	public void clearSnake(int x, int y, Color color) {
		this.g.setColor(color);
		this.g.fillRect(x, y, MainFrame.GAP_SIZE, MainFrame.GAP_SIZE);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			snakeBody[0].setDirection(LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			snakeBody[0].setDirection(RIGHT);
			break;
		case KeyEvent.VK_UP:
			snakeBody[0].setDirection(UP);
			break;
		case KeyEvent.VK_DOWN:
			snakeBody[0].setDirection(DOWN);
			break;
		default:
			System.out.println("请使用方向键进行控制");
			break;
		}		
	}
	
}
