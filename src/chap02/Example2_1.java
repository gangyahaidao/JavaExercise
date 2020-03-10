package chap02;

public class Example2_1 {
	/**
	 * Java�е�unicode�ַ����������
	 */
	public static void main(String args[]) {
		char chinaWord = '好', japanWord = '文';
		char you = '\u4F60';
		int position = 20320;

		System.out.println("����:" + chinaWord + "��λ��:" + (int) chinaWord);
		System.out.println("����:" + japanWord + "��λ��:" + (int) japanWord);
		System.out.println(position + "λ���ϵ��ַ���:" + (char) position);
		position = 21319;
		System.out.println(position + "λ���ϵ��ַ���:" + (char) position);
		System.out.println("you:" + you);
	}
}
