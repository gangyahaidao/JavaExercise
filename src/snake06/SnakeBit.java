package snake06;

import java.awt.Color;

/**
 * 用来表示蛇长度的每一个最小组成部分
 * */
public class SnakeBit {

	private int x;
	private int y;
	private int direction;
	private Color color;
	
	public SnakeBit() {
		
	}
	public SnakeBit(int x, int y) {
		this.x = x;
		this.y = y;
		this.color = Color.ORANGE;
	}
	
	public SnakeBit(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public SnakeBit(int x, int y, Color color, int direction) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.direction = direction;
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
