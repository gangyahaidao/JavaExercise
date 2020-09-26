package Snake09;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;
/**
 * javac -encoding UTF-8  -cp ".;../libs/json-20190722.jar" .\Snake09\SnakeServerSocket.java .\Snake09\Snake.java .\Snake09\SnakeBit.java .\Snake09\MainFrame.java
 * 
 * 执行命令：
 * java -cp ".;../libs/json-20190722.jar" Snake09.SnakeServerSocket
 * */

public class SnakeServerSocket {
	private static Socket clientA = null;
	private static Socket clientB = null;
	
	// 一个食物对象，用蛇的身体一节对象来描述
	private static SnakeBit food;
	
	class SnakeClientThread implements Runnable {
		private Socket client;
		
		public SnakeClientThread(Socket clientA) {
			this.client = clientA;
		}

		@Override
		public void run() {
			produceFood();
			while(true) {
				try {
					System.out.println("准备接收数据");
					InputStream input = client.getInputStream();
					byte[] buf = new byte[10240];
					int len = input.read(buf);
					String msg = new String(buf, 0, len);
					System.out.println("msg = " + msg);
					JSONObject recvObj = new JSONObject(msg);
					boolean hasEatenFood = recvObj.getBoolean("hasEatenFood");
					if(hasEatenFood) {
						produceFood();
					}
					// 在发送的数据中添加食物对象信息
					JSONObject obj = new JSONObject();
					obj.put("snake", recvObj.get("snake").toString());
					
					JSONObject foodObj = new JSONObject();
					foodObj.put("X", food.getX());
					foodObj.put("Y", food.getY());
					foodObj.put("color", food.getColor().getRGB());					
					obj.put("food", foodObj);
					
					// 将接收到的msg内容发送给B
					if(this.client == clientA && clientB != null) {
						OutputStream out = clientB.getOutputStream();
						out.write(obj.toString().getBytes());
					}
					if(this.client == clientB && clientA != null) { // 如果是B线程，将消息发送到A
						OutputStream out = clientA.getOutputStream();
						out.write(obj.toString().getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 随机生成一个食物
	 * */
	private SnakeBit produceFood() {
		int x1 = (int)Math.floor(Math.random()*(MainFrame.GAME_WIDTH/MainFrame.GAP_SIZE-2)+1)*MainFrame.GAP_SIZE;
		int y1 = (int)Math.floor(Math.random()*(MainFrame.GAME_HEIGHT/MainFrame.GAP_SIZE-2)+1)*MainFrame.GAP_SIZE;
		food = new SnakeBit(x1, y1, Color.GREEN);		
		return food;
	}
	
	public static void main(String[] args) {				
		System.out.println("服务端启动，准备接收客户端的连接");
		SnakeServerSocket serverSocket = new SnakeServerSocket();
		
		try {
			ServerSocket server = new ServerSocket(6666);
			
			while(true) {
				Socket client = server.accept(); // 等待客户端连接
				if(clientA == null) {
					clientA = client;
					SnakeClientThread clientThreadA = serverSocket.new SnakeClientThread(clientA);
					new Thread(clientThreadA).start();
					System.out.println("客户端A连接");
				} else if(clientB == null) {
					clientB = client;
					SnakeClientThread clientThreadB = serverSocket.new SnakeClientThread(clientB);
					new Thread(clientThreadB).start();
					System.out.println("客户端B连接");
				} else {
					System.out.println("客户端数量已满，不支持新的连接");
				}
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
