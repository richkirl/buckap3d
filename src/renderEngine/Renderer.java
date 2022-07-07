package renderEngine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import Entity.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;


public class Renderer {
	private static final float FOV=70;
	private static final float NEAR_PLANE=0.1f;
	private static final float FAR_PLANE=1000;
	private Matrix4f projectionMatrix;
	public Renderer(DisplayManager window,StaticShader shader) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix(window);
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0.1f, 0.1f, 1);
	}
	public void render(Entity entity,StaticShader shader) {
		TexturedModel model = entity.getModel();
		RawModel rawmodel = model.getRawModel();
		//GL30.glActiveTexture(id);
		GL30.glBindVertexArray(rawmodel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Matrix4f transformationMatrix = 
		Maths.createTransformationMatrix(entity.getPosition(), 
		entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_2D, model.getTexture().getID());
		GL11.glDrawElements(GL13.GL_TRIANGLES, rawmodel.getVertexCount(),GL13.GL_UNSIGNED_INT,0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	private void createProjectionMatrix(DisplayManager window) {
		float aspectRatio = (float)window.width/(float)window.heihght;
		float y_scale = (float) ((1f/Math.tan(Math.toRadians(FOV/2f)))*aspectRatio);
		float x_scale = y_scale/aspectRatio;
		float frustum_lenght = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-((FAR_PLANE+NEAR_PLANE)/frustum_lenght));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2*FAR_PLANE*NEAR_PLANE)/frustum_lenght));
		projectionMatrix.m33(0);
		
		
		
		
		
	}
}
