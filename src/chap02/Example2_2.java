package chap02;

public class Example2_2 {
	/**
	 * 功能：变量的定义及输出练习；强制类型转换导致精度丢失问题
	 * 
	 */
	public static void main(String args[]) {
		byte b = 22;
		int n = 129;
		float f = 123456.6789f;
		double d = 123456789.123456789;

		System.out.println("b=  " + b);
		System.out.println("n=  " + n);
		System.out.println("f=  " + f);
		System.out.println("d=  " + d);
		b = (byte) n; // 导致精度的损失.
		f = (float) d; // 导致精度的损失
		System.out.println("b=  " + b);
		System.out.println("f=  " + f);
	}
}
