package renderEngine;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import models.RawModel;
import textures.ModelTexture;



public class Loader {
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();
	
	public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices) {
		int vaoID = createVAO();
		bindingIndicesBuffer(indices);
		storeDataInAttribList(0,3,positions);
		storeDataInAttribList(1,2,textureCoords);
		storeDataInAttribList(2,3,normals);
		unbindVAO();
		return new RawModel(vaoID,indices.length);
		
	}
	public RawModel loadToVAO(float[] positions,float[] textureCoords,int[] indices) {
		int vaoID = createVAO();
		bindingIndicesBuffer(indices);
		storeDataInAttribList(0,3,positions);
		storeDataInAttribList(1,2,textureCoords);
		
		unbindVAO();
		return new RawModel(vaoID,indices.length);
		
	}
	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		
		storeDataInAttribList(0,2,positions);
		
		unbindVAO();
		return new RawModel(vaoID,positions.length/2);
		
	}
	public int loadTexture(String fileName) throws Exception{
		int width,height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(fileName,w,h,c,4);
            //STBEasyFont.stb_easy_font_height()
            //stbi_load_from_memory
            //buffer = STBImage.stbi_load_from_memory(filename,w,h,c,4);
            if(buffer == null)
                throw new Exception("Image file"+fileName+"not loaded"+STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();

        }
        
        int id = GL11.glGenTextures();
        
        textures.add(id);
       
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
        
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT,1);
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buffer);
        GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_MAG_FILTER, GL21.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
       
        
        
        STBImage.stbi_image_free(buffer);
        
        return id;

	}
	public int loadTextureBuf(ByteBuffer buf) throws Exception{
		int width,height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            //buffer = STBImage.stbi_load(fileName,w,h,c,4);
            //STBEasyFont.stb_easy_font_height()
            //stbi_load_from_memory
            buffer = STBImage.stbi_load_from_memory(buf,w,h,c,4);
            if(buffer == null)
                throw new Exception("Image file"+"not loaded"+STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();

        }
        
        int id = GL11.glGenTextures();
        
        textures.add(id);
       
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
        
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT,1);
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buffer);
        GL21.glTexParameteri(GL21.GL_TEXTURE_2D, GL21.GL_TEXTURE_MAG_FILTER, GL21.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
       
        
        
        STBImage.stbi_image_free(buffer);
        
        return id;

	}
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
		
	}
	private void storeDataInAttribList(int attributNumber,int coordinateSize,float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(attributNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindingIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer =storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
		
	}
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
		
	}
	
	public void cleanUP() {
		for(int vao:vaos)
			GL30.glDeleteVertexArrays(vao);
		for(int vbo:vbos)
			GL15.glDeleteBuffers(vbo);
		for(int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}
}


