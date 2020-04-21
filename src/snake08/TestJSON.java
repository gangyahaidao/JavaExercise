package snake08;

import org.json.JSONObject;

public class TestJSON {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		JSONObject obj = new JSONObject();
		SnakeBit food = null;
		obj.put("food", food);
		obj.put("isAlive", true);
		System.out.println("has = " + obj.has("food"));
		System.out.println(obj.toString());
	}

}
