package chap03.InClass;

import java.util.Scanner;

public class Exer016 {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("������һ��������");
		int number = scan.nextInt();
		String check = (number%2 == 0) ? "���������ż��" : "�������������";
		System.out.println("check = " + check);
	}
}


