package chap02;

public class Example2_1 {
	/**
	 * Java中的unicode字符集编码测试
	 */
	public static void main(String args[]) {
		char chinaWord = '好', japanWord = 'ぁ';
		char you = '\u4F60';
		int position = 20320;

		System.out.println("汉字:" + chinaWord + "的位置:" + (int) chinaWord);
		System.out.println("日文:" + japanWord + "的位置:" + (int) japanWord);
		System.out.println(position + "位置上的字符是:" + (char) position);
		position = 21319;
		System.out.println(position + "位置上的字符是:" + (char) position);
		System.out.println("you:" + you);
	}
}
