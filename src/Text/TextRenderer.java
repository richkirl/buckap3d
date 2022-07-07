package Text;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;



import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;

public class TextRenderer {
	private final RawModel quad;
	private TextShader shader;
	public TextRenderer(Loader loader) throws Exception {
		float[] positions = {
				-1f,   1f,
				-1f,  -1f,
				1f,   1f,
				1f,  -1f};
		quad = loader.loadToVAO(positions);
		shader = new TextShader();
	}
	
	public void renderText(List<TextTexture> texts) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(TextTexture text: texts) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, text.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(text.getPosition(), text.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		//GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	public void renderGrid(List<TextTexture> texts) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(TextTexture text: texts) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, text.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(text.getPosition(), text.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	public void cleanUp() {
		shader.cleanUp();
	}
}
