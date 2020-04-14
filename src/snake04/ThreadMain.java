package snake04;

public class ThreadMain {

	public static void main(String[] args) {		
		new Thread(new ChildThread()).start();
		new Thread(new ChildThread2()).start();
		
		while(true);
	}
	
	public static class ChildThread implements Runnable {
		@Override
		public void run() {
			while(true) {
				System.out.println("这是第一个while(1)");
			}
		}		
	}
	
	public static class ChildThread2 implements Runnable {
		@Override
		public void run() {
			while(true) {
				System.out.println("这是第二个while(1)");
			}
		}		
	}
	

}
