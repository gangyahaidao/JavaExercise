package snake08;

import java.awt.Color;

/**
 * 表示蛇的每一节
 * */
public class SnakeBit {

	// 当前坐标
	private int x;
	private int y;
	
	// 每一节的方向
	private int direction;
	
	// 每一节的颜色
	private Color color;

	public SnakeBit() {
		
	}
	public SnakeBit(int x, int y, Color color) {
		// TODO 自动生成的构造函数存根
		this.x = x;
		this.y = y;
		this.direction = Snake.NONE;
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
