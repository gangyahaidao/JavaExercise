package snake08;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Desc：建立socket通信服务端，接收所有客户端消息，并将所有蛇的运行动态实时转发到连接的所有客户端
 * 
 *  编译命令：
 * cd src进入src目录
 * javac -encoding UTF-8  -cp ".;../libs/json-20190722.jar" .\snake08\SnakeServerSocket.java .\snake08\Snake.java .\snake08\SnakeBit.java
 * 
 * 执行命令：
 * java -cp ".;../libs/json-20190722.jar" snake08.SnakeServerSocket
 */
public class SnakeServerSocket {
	private static Socket clientA = null;
	private static Socket clientB = null;
	private static SnakeBit food = null; // 随机生成的食物对象

	public static void main(String[] args) throws IOException {
		System.out.println("服务端启动，准备接收客户端的连接");
		
		SnakeServerSocket serverSocket = new SnakeServerSocket();
		ServerSocket server = new ServerSocket(6666); // 绑定服务端端口
		
		// 建立循环接收
		while(true) {
			Socket client = server.accept(); // 等待客户端的连接
			if(clientA == null) { // 第一个连接的用户为A
				clientA = client;
				SocketClientThread clientThreadA = serverSocket.new SocketClientThread(client, clientB);
				new Thread(clientThreadA).start(); // 在一个独立的线程中运行客户端的数据接收与发送
				System.out.println("客户端A连接服务成功");
			} else if(clientB == null) { // 第二个连接的用户为B
				clientB = client;
				SocketClientThread clientThreadB = serverSocket.new SocketClientThread(client, clientA);
				new Thread(clientThreadB).start(); // 在一个独立的线程中运行客户端的数据接收与发送
				System.out.println("客户端B连接服务成功");				
			} else {
				System.out.println("当前只支持两个用户同时在线");
			}
		}				
	}
	
	/**
	 * 随机生成一个食物
	 * */
	private static SnakeBit produceFood() {
		// 随机生成一个食物
		int x1 = (int)Math.floor(Math.random()*(MainFrame.GAME_WIDTH/MainFrame.GAP_SIZE-2)+1)*MainFrame.GAP_SIZE;
		int y1 = (int)Math.floor(Math.random()*(MainFrame.GAME_HEIGHT/MainFrame.GAP_SIZE-2)+1)*MainFrame.GAP_SIZE;
		food = new SnakeBit(x1, y1, Color.GREEN);
		
		return food;
	}
	
	/**
	 * Desc：创建一个内部类，表示连接的客户端线程，用于接收和发送数据
	 * */
	public class SocketClientThread implements Runnable {		
		private Socket client = null;
		private Socket otherClient = null;
		
		public SocketClientThread(Socket client, Socket otherClient) {
			this.client = client;
			this.otherClient = otherClient;
		}

		@Override
		public void run() {
			while(true) {
				try {
					InputStream input = client.getInputStream();
					
					byte[] buf = new byte[1024];
					int len = input.read(buf); // 读取客户端发送过来的贪吃蛇位置信息
					System.out.println("recv len = " + len);					
					String msg = new String(buf, 0, len); // 将字节数组转换成字符串打印
					System.out.println("接收到客户端 = " + client + "，数据 = " + msg);										
					
					// 在接收到一个客户端消息的同时向另一个客户端发送json格式消息
					if(otherClient != null && otherClient.isConnected()) { // 如果另一个客户端已经连接并且处于连接状态，则发送数据
						JSONObject recvObj = new JSONObject(msg);
						if(!recvObj.has("food")) { // 如果接收的消息中不存在food字段，说明需要新生成食物
							food = produceFood();
						}
						
						// 拼接发送的json消息
						JSONObject jsonObj = new JSONObject();
						boolean isAlive = recvObj.getBoolean("isAlive");
						if(isAlive) {
							jsonObj.put("isAlive", true);							
							jsonObj.put("food", food);						
							jsonObj.put("snakeBody", recvObj.getJSONArray("snakeBody"));
						} else {
							jsonObj.put("isAlive", false);
						}
												
						OutputStream out = otherClient.getOutputStream();						
						out.write(jsonObj.toString().getBytes()); // 发送到另一个客户端
						out.flush();
					}					
				} catch (IOException e) {
					e.printStackTrace();
					break; // 退出客户端线程
				}
			}
			System.out.println("服务端客户线程退出");
		}
		
	}

}
