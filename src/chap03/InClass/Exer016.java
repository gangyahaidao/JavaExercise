package chap03.InClass;

import java.util.Scanner;

public class Exer016 {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("请输入一个整数：");
		int number = scan.nextInt();
		String check = (number%2 == 0) ? "这个数字是偶数" : "这个数字是奇数";
		System.out.println("check = " + check);
	}
}


