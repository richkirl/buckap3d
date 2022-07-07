package Entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import renderEngine.DisplayManager;

public class Camera {
	private Vector3f position = new Vector3f(0,5,20);
	private float pitch;
	private float yaw;
	private float roll;
	public long window;
	//public DisplayManager w;
	public Camera() {	}
	//Keyboard
	public void move(DisplayManager w) {
		this.window = w.getWindow();
		//window.getWindow().isKeyPressed()
		if(w.isKeyPressed(GLFW.GLFW_KEY_W)) {
			position.z-=0.02f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_D)) {
			position.x+=0.02f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_A)) {
			position.x-=0.02f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_S)) {
			position.z+=0.02f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_Z)) {
			position.y+=0.02f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_X)) {
			position.y-=0.02f;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
}
