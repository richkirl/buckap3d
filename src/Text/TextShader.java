package Text;

import org.joml.Matrix4f;

import shaders.ShaderProgram;

public class TextShader extends ShaderProgram {
	private static final String VERTEX_FILE="src/Text/vertexText.glsl";
	private static final String FRAGMENT_FILE="src/Text/fragmentText.glsl";
	private int location_transformationMatrix;
	public TextShader() throws Exception {
		super(VERTEX_FILE, FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	@Override
	protected void getAllUniformLocation() {
		// TODO Auto-generated method stub
		location_transformationMatrix=super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		// TODO Auto-generated method stub
		super.bindAttribute(0, "position");
		//super.bindAttribute(1, "guiTexture");
	}

}
