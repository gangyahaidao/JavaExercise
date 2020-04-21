package snake07;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Desc：建立socket通信服务端，接收所有客户端消息，并将所有蛇的运行动态实时转发到连接的所有客户端
 * */
public class SnakeServerSocket {
	private static Socket clientA = null;
	private static Socket clientB = null;

	public static void main(String[] args) throws IOException {
		System.out.println("服务端启动，准备接收客户端的连接");
		
		SnakeServerSocket serverSocket = new SnakeServerSocket();
		ServerSocket server = new ServerSocket(6666); // 绑定服务端端口
		
		// 建立循环接收
		while(true) {
			Socket client = server.accept(); // 等待客户端的连接
			if(clientA == null) { // 第一个连接的用户为A
				clientA = client;
				SocketClientThread clientThread = serverSocket.new SocketClientThread(client, clientB);
				new Thread(clientThread).start(); // 在一个独立的线程中运行客户端的数据接收与发送
			} else if(clientB == null) { // 第二个连接的用户为B
				clientB = client;
				SocketClientThread clientThread = serverSocket.new SocketClientThread(client, clientA);
				new Thread(clientThread).start(); // 在一个独立的线程中运行客户端的数据接收与发送
			} else {
				System.out.println("当前只支持两个用户同时在线");
			}
		}				
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
					String msg = new String(buf, 0, len); // 将字节数组转换成字符串打印
					System.out.println("接收到客户端 = " + client + "，数据 = " + msg);
					
					// 将json格式的消息发送到另一个客户端
					if(otherClient != null && otherClient.isConnected()) { // 如果另一个客户端已经连接并且处于连接状态，则发送数据
						OutputStream out = otherClient.getOutputStream();
						out.write(msg.getBytes());						
					}					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
