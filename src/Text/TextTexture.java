package Text;

import org.joml.Vector2f;

public class TextTexture {
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private int width;
	private int height;
	public TextTexture(Vector2f position, Vector2f scale,int texture) {
		
		//this.width=width;
		//this.height=height;
		this.position = position;
		this.scale = scale;
		this.texture = texture;
	}
	public int getTexture() {
		return texture;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
}
