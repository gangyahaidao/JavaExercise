package chap03.InClass;

import java.util.Scanner;

public class Exer016 {

	public static void main(String[] args) {
		System.out.println("请输入一串需要加密的字符串：");
		
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		char[] arr = input.toCharArray();
		for(int i = 0; i < arr.length; i++) {
			arr[i] = (char) (arr[i] ^ 20000);
		}
		System.out.println("加密后数据为 = " + new String(arr));
		
		System.out.println("现在进行解密:");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < arr.length; i++) {
			arr[i] = (char) (arr[i]^20000);
		}
		System.out.println("解密数据为 = " + new String(arr));
	}
}


