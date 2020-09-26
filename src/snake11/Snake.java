package Snake09;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 蛇类
 * */
public class Snake extends KeyAdapter {

	// 定义四个方向常量
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;
	public static final int NONE = 0;
	
	// 定义一个数组，用来存储蛇的身体
	public SnakeBit snakeBody[];
	public int snakeLength = 0;
	
	private Graphics g;
	private MyPanel mypanel; // 蛇所在的对象
	
	// socket客户端连接对象
	private Socket client;
	
	// 一个食物对象，用蛇的身体一节对象来描述
	SnakeBit food;
	
	/**
	 * 定义构造函数，在屏幕中间绘制一个蛇头
	 * */ 
	public Snake(int x, int y, MyPanel panel) {
		// 建立到服务器的连接
		try {
			this.client = new Socket("127.0.0.1", 6666);			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		snakeBody = new SnakeBit[100]; // 可以用ArrayList进行优化
		// 给蛇的身体安装上蛇头
		Color color = generateRandomColor();
		SnakeBit bit = new SnakeBit(x, y, color);
		snakeBody[0] = bit;
		snakeLength += 1; // 蛇的总长+1
		
		this.mypanel = panel;
		new Thread(new SnakeAuto()).start();
	}
	
	class SnakeAuto implements Runnable {		
		private JSONArray preSnakeBodyObj = null; // 保存上一次蛇的位置信息
		
		@Override
		public void run() {
			while(true) {
				snakeMove(); // 进行一次移动，同时将蛇最新的位置信息发送发到服务端
				
				// 接收发送过来的另一个客户端的蛇位置信息，并做绘制
				try {					
					InputStream input = client.getInputStream();
					if(input.available() > 0) {
						byte[] buf = new byte[10240];
						int len = input.read(buf); // 如果没有数据，此方法会阻塞在这里
						String recvStr = new String(buf, 0, len);
						System.out.println("recv = " + recvStr);
						
						// {"snake":[{"color":-65536,"x":400,"y":525,"direction":3}]}
						JSONObject recvObj = new JSONObject(recvStr);	
						String snakeStr = recvObj.getString("snake");
						JSONArray snakeBody = new JSONArray(snakeStr);
						
						JSONObject foodObj = recvObj.getJSONObject("food");						
						food = new SnakeBit(foodObj.getInt("X"), 
								foodObj.getInt("Y"),
								new Color(foodObj.getInt("color")));
						drawSnakeXY(food.getX(), food.getY(), food.getColor());
						
						// 将之前蛇的显示清空
						if(preSnakeBodyObj != null) {
							for(int i = 0; i < preSnakeBodyObj.length(); i++) {
								JSONObject obj = preSnakeBodyObj.getJSONObject(i);
								deleteSnakeXY(obj.getInt("x"), 
										obj.getInt("y"), 
										Color.WHITE);
							}	
						}										
						preSnakeBodyObj = snakeBody; // 清空之后再进行保存
						
						// 遍历snakeBody对象，在界面进行绘制
						for(int i = 0; i < snakeBody.length(); i++) {
							JSONObject obj = snakeBody.getJSONObject(i);
							drawSnakeXY(obj.getInt("x"), 
									obj.getInt("y"), 
									new Color(obj.getInt("color")));
						}
					}					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
		}		
	}
	/**
	 * 控制蛇的运动，自动运动功能
	 * */
	public void snakeMove() {
		if(snakeBody[0].getDirection() != NONE) { // 已经开始运动
			// 1.更新下一步蛇头的位置
			// 2.判断边界是否撞墙
			// 3.向蛇数组中的最前面插入一个新的蛇头
			// 4.将蛇数组的最后一节去掉
			SnakeBit oldBit = snakeBody[0];
			SnakeBit newBit = new SnakeBit(oldBit.getX(), oldBit.getY(), oldBit.getColor()); // 新的蛇头对象
			newBit.setDirection(oldBit.getDirection());			
			
			if(oldBit.getDirection() == LEFT) {
				newBit.setX(oldBit.getX() - MainFrame.GAP_SIZE); // 新的蛇头x坐标是在旧蛇头的基础上向左移动一格
			} else if(oldBit.getDirection() == RIGHT) {
				newBit.setX(oldBit.getX() + MainFrame.GAP_SIZE);
			} else if(oldBit.getDirection() == UP) {
				newBit.setY(oldBit.getY() - MainFrame.GAP_SIZE);
			} else if(oldBit.getDirection() == DOWN) {
				newBit.setY(oldBit.getY() + MainFrame.GAP_SIZE);
			}			
			
			if (newBit.getX() <= 0 || newBit.getX() >= (MainFrame.GAME_WIDTH-MainFrame.GAP_SIZE)) {
				System.out.println("撞墙了");
				JOptionPane.showMessageDialog(mypanel, "撞墙了，GameOver", "结束", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			if(newBit.getY() <= 0 || newBit.getY() >= (MainFrame.GAME_HEIGHT - MainFrame.GAP_SIZE)) {
				System.out.println("撞墙了");
				JOptionPane.showMessageDialog(mypanel, "撞墙了，GameOver", "结束", JOptionPane.ERROR_MESSAGE);
				System.exit(0); // exit(0);
			}
			
			boolean hasEatenFood = false;		
			if(food != null && newBit.getX() == food.getX() && newBit.getY() == food.getY()) {				
				food = null;
				hasEatenFood = true;
			}
			
			oldBit.setColor(Color.ORANGE);
			drawSnakeXY(oldBit.getX(), oldBit.getY(), oldBit.getColor());
			
			for(int i = snakeLength; i > 0; i--) {
				snakeBody[i] = snakeBody[i-1];
			}
			snakeBody[0] = newBit;
			snakeLength += 1; // 插入新的蛇头之后，总长+1
			
			if(hasEatenFood == false) {
				deleteSnakeXY(snakeBody[snakeLength-1].getX(), snakeBody[snakeLength-1].getY(), Color.WHITE);
				snakeBody[snakeLength-1] = null;
				snakeLength -= 1; // 完成了删除最后一节
			}
			drawSnakeXY(newBit.getX(), newBit.getY(), newBit.getColor()); // 重新绘制蛇头
			
			// 将当前蛇运动信息发送到服务器
			try {
				OutputStream out = this.client.getOutputStream();
				
				JSONObject jsonObj = new JSONObject();
				JSONArray jsonArr = new JSONArray();
				for(int i = 0; i < snakeLength; i++) {
					SnakeBit bit = snakeBody[i];
					JSONObject obj = new JSONObject();
					obj.put("x", bit.getX());
					obj.put("y", bit.getY());
					obj.put("direction", bit.getDirection());
					obj.put("color", bit.getColor().getRGB());
					jsonArr.put(obj);
				}
				jsonObj.put("snake", jsonArr);
				// {"snake":[{"color":-65536,"x":25,"y":725,"direction":3}], "hasEatenFood": true}
				jsonObj.put("hasEatenFood", hasEatenFood);							
				System.out.println("客户端发送的数据 = " + jsonObj.toString());
				
				out.write(jsonObj.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void snakeInit(Graphics g) {
		this.g = g;
		// 绘制蛇头
		g.setColor(snakeBody[0].getColor());
		g.fillRoundRect(snakeBody[0].getX(),
				snakeBody[0].getY(), 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/5, 
				MainFrame.GAP_SIZE/5);
	}
	
	/**
	 * 擦除指定坐标的方块，坐标是方块的左上角的值
	 * */
	public void deleteSnakeXY(int x, int y, Color color) {
		g.setColor(color);
		g.fillRect(x, y, MainFrame.GAP_SIZE, MainFrame.GAP_SIZE);
	}
	
	/**
	 * 在指定坐标绘制一个新的方块
	 * */
	public void drawSnakeXY(int x, int y, Color color) {
		g.setColor(color);
		g.fillRoundRect(x, y, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/5, 
				MainFrame.GAP_SIZE/5);
	}
	
	/**
	 * 生成随机颜色
	 * */
	public Color generateRandomColor() {
		Random random = new Random();
		int r = random.nextInt()%255;
		int g = random.nextInt()%255;
		int b = random.nextInt()%255;
		Color color = new Color(Math.abs(r), Math.abs(g), Math.abs(b));
		return color;
	}
	
	/**监听上下左右按键操作*/
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();		
		
		switch(key) { // 重新计算下一次蛇头的位置
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
			System.out.println("请使用方向键控制");
			break;
		}	
	}
}
