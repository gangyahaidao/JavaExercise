package chap03.InClass;

import java.util.Scanner;

public class Exer015 {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("������һ������ԭʼ�ַ�����");
		String password = scan.nextLine();
		char[] array = password.toCharArray();
		for(int i = 0; i < array.length; i++) {
			array[i] = (char) (array[i]^20000);
		}
		System.out.println("���ܽ�����£�" + new String(array));
		
		System.out.println("��ʼ���н��ܣ�");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < array.length; i++) {
			array[i] = (char) (array[i]^20000);
		}
		System.out.println("���ܽ�����£�" + new String(array));
	}
}
