package snake03;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Snake {
	//位置
	private int x;
	private int y;
	//大小
	private static final int WIDTH = 10 ;
	private static final int HEIGHT = 10 ;
	
	//方向
	private Direction dir = Direction.D;
	
	private MainFrame03 snakeFrame ;
	
	public Snake(int x, int y, MainFrame03 snakeFrame) {
		this.x = x;
		this.y = y;
		this.snakeFrame = snakeFrame;
	}
	
	public Snake(int x, int y,Direction dir, MainFrame03 snakeFrame) {
		this(x,y,snakeFrame);
		this.dir = dir ;
	}

	public void draw(Graphics g){
		
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(c);
	}

	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT :
			this.x = x - WIDTH;
			break;
		case KeyEvent.VK_UP :
			this.y = y - HEIGHT;
			break;
		case KeyEvent.VK_RIGHT :
			this.x = x + WIDTH;
			break;
		case KeyEvent.VK_DOWN :
			this.y = y + WIDTH;
			break;
			
		}
		
		//处理蛇的边界
		if(x<=0){
			x = 0;
		}
		else if(x>MainFrame03.GAME_WIDTH - WIDTH){
			x = MainFrame03.GAME_WIDTH - WIDTH;
		}
		
		if(y<=0){
			y = 0;
		}
		else if(y>MainFrame03.GAME_HEIGHT - HEIGHT){
			y = MainFrame03.GAME_HEIGHT - HEIGHT ;
		}
	}
	
	
}
