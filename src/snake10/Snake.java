package snake10;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
	public static int SPEED = 1000; // 蛇移动时间毫秒
	
	// 定义一个数组，用来存储蛇的身体
	public SnakeBit snakeBody[];
	public int snakeLength = 0;
	public JSONArray preOtherSnakeObj = null;
	
	private Graphics g;
	private MyPanel mypanel; // 蛇所在的对象
	
	// 一个食物对象，用蛇的身体一节对象来描述
	SnakeBit food = null;
	
	// socket通信客户端
	Socket client;
	OutputStream out;
	
	/**
	 * 定义构造函数，在屏幕中间绘制一个蛇头
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * */ 
	public Snake(int x, int y, MyPanel panel) {
		// 初始化连接服务端socket
		try {
			this.client = new Socket("127.0.0.1", 6666);
			out = client.getOutputStream(); // 获取输出流对象，进行数据输出
			System.out.println("连接服务器成功");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 给蛇身体对象分配存储空间，最大是100节
		snakeBody = new SnakeBit[100];
		// 给蛇的身体安装上蛇头
		SnakeBit bit = new SnakeBit(x, y, Color.RED);		
		snakeBody[0] = bit;
		snakeLength += 1; // 蛇的总长+1
		
		this.mypanel = panel;
		new Thread(new SnakeAuto()).start();
	}
	
	/**
	 * Desc:驱动蛇自动运行线程
	 * */
	class SnakeAuto implements Runnable {
		@Override
		public void run() {
			while(true) {
				snakeMove(); // 向前移动一格，并且将当前新的状态发送到服务器
				
				long millisTime = System.currentTimeMillis();
				// 接收对手蛇运动的消息
				try {
					InputStream input = client.getInputStream();
					if(input.available() > 0) {
						byte[] b = new byte[10240];
						int len = input.read(b);
						String msg = new String(b, 0, len);
						
						System.out.println("收到对方蛇运动信息 = " + msg);
						JSONObject jsonObj = new JSONObject(msg);					
						
						// 重新绘制对手的蛇对象
						if(jsonObj.getBoolean("isAlive")) {
							
							// 绘制食物
							JSONObject foodObj = jsonObj.getJSONObject("food");
							//System.out.println("foodObj = " + foodObj + ", client food = " + food);
							if(food == null) { // 当前界面中还没有食物
								drawSnakeXY(foodObj.getInt("x"), foodObj.getInt("y"), new Color(foodObj.getInt("color")));
								food = new SnakeBit(foodObj.getInt("x"), foodObj.getInt("y"), new Color(foodObj.getInt("color")));
							} else {
								if(food.getX()!=foodObj.getInt("x") || food.getY()!=foodObj.getInt("y")) { // 说明食物的位置改变了，就是被另一个蛇吃掉了，需要重新绘制
									deleteSnakeXY(food.getX(), food.getY(), Color.WHITE); // 食物设置为背景色清除掉								
									food.setX(foodObj.getInt("x")); // 更新食物对象的位置值
									food.setY(foodObj.getInt("y"));
									drawSnakeXY(foodObj.getInt("x"), foodObj.getInt("y"), new Color(foodObj.getInt("color")));								
								}
							}							
							
							// 如果另一条蛇还活着，将之前的蛇擦除掉，在新的位置完全重新绘制
							JSONArray snakeArr = jsonObj.getJSONArray("snakeBody");
							if(preOtherSnakeObj != null) { // 如果不是第一次绘制，则需要擦除之前的蛇							
								for (int i = 0; i < preOtherSnakeObj.length(); i++) {
									JSONObject obj = preOtherSnakeObj.getJSONObject(i);
									deleteSnakeXY(obj.getInt("x"), obj.getInt("y"), Color.WHITE);								
								}
							}
							for (int j = 0; j < snakeArr.length(); j++) { // 绘制新的蛇
								JSONObject obj = snakeArr.getJSONObject(j);								
								drawSnakeXY(obj.getInt("x"), obj.getInt("y"), new Color(obj.getInt("color")));
							}
							preOtherSnakeObj = snakeArr;
						} else { // 对方蛇死掉了
							for (int i = 0; i < preOtherSnakeObj.length(); i++) { // 界面中清除对方死掉的蛇
								JSONObject obj = preOtherSnakeObj.getJSONObject(i);
								deleteSnakeXY(obj.getInt("x"), obj.getInt("y"), Color.WHITE);								
							}
						}	
					}								
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					Thread.sleep(SPEED - (System.currentTimeMillis()-millisTime));// 将绘制的时间去除掉，保证每次绘制是固定的时间间隔
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
			SnakeBit oldBit = snakeBody[0]; // 存储旧的蛇头数据
			SnakeBit newBit = new SnakeBit(oldBit.getX(), oldBit.getY(), oldBit.getColor()); // 新的蛇头对象
			JSONObject jsonObj = new JSONObject(); // 封装发送的json消息对象
			jsonObj.put("isAlive", true); // 默认蛇还是活的
			
			// 更新新的蛇头XY位置
			if(oldBit.getDirection() == LEFT) {
				newBit.setX(oldBit.getX() - MainFrame.GAP_SIZE); // 新的蛇头x坐标是在旧蛇头的基础上向左移动一格
			} else if(oldBit.getDirection() == RIGHT) {
				newBit.setX(oldBit.getX() + MainFrame.GAP_SIZE);
			} else if(oldBit.getDirection() == UP) {
				newBit.setY(oldBit.getY() - MainFrame.GAP_SIZE);
			} else if(oldBit.getDirection() == DOWN) {
				newBit.setY(oldBit.getY() + MainFrame.GAP_SIZE);
			}
			// 设置新蛇头的方向和颜色
			newBit.setDirection(oldBit.getDirection());
			
			// 判断蛇下一步是否会撞墙
			if (newBit.getX() <= 0 || newBit.getX() >= (MainFrame.GAME_WIDTH-MainFrame.GAP_SIZE)) {
				System.out.println("撞墙了");
				JOptionPane.showMessageDialog(mypanel, "撞墙了，GameOver", "结束", JOptionPane.ERROR_MESSAGE);
				jsonObj.put("isAlive", false); // 向服务端发送蛇死掉的消息
			}
			if(newBit.getY() <= 0 || newBit.getY() >= (MainFrame.GAME_HEIGHT - MainFrame.GAP_SIZE)) {
				System.out.println("撞墙了");
				JOptionPane.showMessageDialog(mypanel, "撞墙了，GameOver", "结束", JOptionPane.ERROR_MESSAGE);
				jsonObj.put("isAlive", false); // 向服务端发送蛇死掉的消息
			}
			
			// 新的位置是否有食物
			if(food != null && newBit.getX() == food.getX() && newBit.getY() == food.getY()) {
				snakeBody[snakeLength] = food;
				snakeLength += 1;
				// 将食物对象设置为null发送服务器，这样服务器就知道需要新生成一个食物
				food = null;
			}
			// 重新绘制，把旧蛇头变成身体的一部分
			drawSnakeXY(oldBit.getX(), oldBit.getY(), Color.ORANGE);
			oldBit.setColor(Color.ORANGE); // 将蛇头对象数据也修改成身体一样的颜色
			
			// 将当前蛇数组中的所有数据向后移动一格
			for(int i = snakeLength; i > 0; i--) {
				snakeBody[i] = snakeBody[i-1];
			}
			snakeBody[0] = newBit;
			snakeLength += 1; // 插入新的蛇头之后，总长+1
			
			// 将蛇最后一节删除掉
			deleteSnakeXY(snakeBody[snakeLength-1].getX(), snakeBody[snakeLength-1].getY(), Color.WHITE);
			snakeBody[snakeLength-1] = null;
			snakeLength -= 1; // 完成了删除最后一节并回收空间的操作
			
			drawSnakeXY(newBit.getX(), newBit.getY(), newBit.getColor()); // 新的位置绘制蛇
			
			// 将当前蛇的各部分位置信息发送到服务器
			// 数据格式为：[{"color":0xffff0000,"x":425,"y":150,"direction":1},{"color":0x0000ffff,"x":450,"y":150,"direction":1}]
			JSONArray jsonArr = new JSONArray();
			for(int i = 0; i < snakeLength; i++) {				
				JSONObject obj = new JSONObject();
				obj.put("x", snakeBody[i].getX());
				obj.put("y", snakeBody[i].getY());
				obj.put("direction", snakeBody[i].getDirection());
				obj.put("color", snakeBody[i].getColor().getRGB());
				jsonArr.put(obj);
			}
			jsonObj.put("snakeBody", jsonArr);
			
			if(food != null) { // 需要服务器重新生成事物的时候，此字段为null
				JSONObject foodObj = new JSONObject();
				foodObj.put("x", food.getX());
				foodObj.put("y", food.getY());
				foodObj.put("color", food.getColor().getRGB());
				jsonObj.put("food", foodObj);
			}			
			// 将数据发送到服务端
			if(client.isConnected()) { // 如果客户端还处于连接状态
				try {
					System.out.println("发送自身状态数据");
					out.write(jsonObj.toString().getBytes());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(jsonObj.getBoolean("isAlive") == false) { // 如果蛇死掉了，则关闭连接socket，退出游戏
				try {
					client.shutdownOutput();
					client.close();
					
					System.exit(0); // 结束程序
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
	public void snakeInit(Graphics g) {
		this.g = g;
		// 绘制蛇头
		g.setColor(Color.RED);
		g.fillRoundRect(snakeBody[0].getX(),
				snakeBody[0].getY(), 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE, 
				MainFrame.GAP_SIZE/5, 
				MainFrame.GAP_SIZE/5);
		snakeBody[0].setDirection((int)Math.floor(Math.random()*4+1)); // 随机给蛇设置一个初始方向
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
