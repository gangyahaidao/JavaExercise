package chap03.InClass;

import java.util.Scanner;

public class Exer016 {

	public static void main(String[] args) {
		System.out.println("������һ����Ҫ���ܵ��ַ�����");
		
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		char[] arr = input.toCharArray();
		for(int i = 0; i < arr.length; i++) {
			arr[i] = (char) (arr[i] ^ 20000);
		}
		System.out.println("���ܺ�����Ϊ = " + new String(arr));
		
		System.out.println("���ڽ��н���:");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < arr.length; i++) {
			arr[i] = (char) (arr[i]^20000);
		}
		System.out.println("��������Ϊ = " + new String(arr));
	}
}


