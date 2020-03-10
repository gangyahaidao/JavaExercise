package chap03;

public class Example3_3 {
	public static void main(String args[]) {
		int arr[][] = new int[10][];
		
		for(int i = 0; i < arr.length; i++) {
			arr[i] = new int[i+1];
			for(int j = 0; j < i+1; j++) {
				if(i == 0 || j == 0 || i == j) {
					arr[i][j] = 1;
				} else {
					arr[i][j] = arr[i-1][j] + arr[i-1][j-1];
				}
			}
		}
		
		for(int[] row : arr) {
			for(int col : row) {
				System.out.print("" + col + '\t');
			}
			System.out.println();
		}
	}
		
}
