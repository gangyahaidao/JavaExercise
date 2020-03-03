package chap03.InClass;

import java.util.Scanner;

public class Exer015 {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("请输入一个加密原始字符串：");
		String password = scan.nextLine();
		char[] array = password.toCharArray();
		for(int i = 0; i < array.length; i++) {
			array[i] = (char) (array[i]^20000);
		}
		System.out.println("加密结果如下：" + new String(array));
		
		System.out.println("开始进行解密：");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < array.length; i++) {
			array[i] = (char) (array[i]^20000);
		}
		System.out.println("解密结果如下：" + new String(array));
	}
}
