package engineTester;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;


import java.awt.Font;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import org.joml.Vector2f;
import org.joml.Vector3f;

import Entity.Camera;
import Entity.Entity;
import Entity.Light;
import GUI.GUIRenderer;
import GUI.GuiTexture;
import Text.GenerateImage;
import Text.TextRenderer;
import Text.TextTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJloader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	public static DisplayManager display = new DisplayManager();
	public static void main(String[] args) throws Exception {
		//display.init();
		//display.prepare();
		
		// TODO Auto-generated method stub
			
			display.run();
			Loader loader = new Loader();
			
			StaticShader shader = new StaticShader("shaders/vertex.glsl","shaders/fragment.glsl");
			Renderer renderer = new Renderer(display,shader);
//			float[] vertices = {
//				    -0.5f, 0.5f, 0f,
//				    -0.5f, -0.5f, 0f,
//				    
//				    0.5f, -0.5f, 0f,
//				    0.5f,0.5f, 0f
//				  };
//			int[] indices = {
//					0,1,3,
//					3,1,2
//			};
//			float[] textureCoords = {
//					0,0,
//					0,1,
//					1,1,
//					1,0
//			};
			//RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
			RawModel model = OBJloader.loadObjModel("dragon", loader);
			
			ModelTexture texture =new ModelTexture(loader.loadTexture("src/res/img1.png"));
			TexturedModel staticModel = new TexturedModel(model,texture);
			
			ModelTexture texture1 = staticModel.getTexture();
			texture1.setShineDamper(10);
			texture1.setReflectivity(1);
			
			
			Entity entity = new Entity(staticModel,new Vector3f(0,1,0),0,0,0,1);
			Light light = new Light(new Vector3f(1,1,1),new Vector3f(1,1,1));
			
			Camera camera = new Camera();
			
			//List<GuiTexture> guis = new ArrayList<GuiTexture>();
			//GuiTexture gui = new GuiTexture(loader.loadTexture("src/res/img2.png"),new Vector2f(-0.5f,0.5f),new Vector2f(0.5f,0.5f));
			//guis.add(gui);
			//GUIRenderer guirenderer = new GUIRenderer(loader);

			
			
			List<TextTexture> texts = new ArrayList<TextTexture>();
			Font font = new Font("C:\\Windows\\Fonts\\Arial.ttf", Font.PLAIN, 64);
			GenerateImage textr = new GenerateImage("JetBrainsMonoNL-Light.ttf");
			ByteBuffer textbuf = textr.createImageGrid(1920, 1080,"",40);
			Runnable task = () ->{
			try {
				textr.createImageGrid1(1920, 1080, "", 40);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			};
			Thread thread = new Thread(task);
			thread.start();
			TextTexture text = new TextTexture(new Vector2f(0f,0f),new Vector2f(1f,1f),loader.loadTextureBuf(textbuf));
			Entity entity1 = new Entity(text,new Vector3f(0,0,0),0,0,0,1);
			texts.add(text);
			TextRenderer textrenderer = new TextRenderer(loader);
			
//			Runnable task = ()-> {
//	            try {
//	                TimeUnit.SECONDS.sleep(1);
//	                StructureOptions structureOptions = new StructureOptions();
//	                structureOptions.starterWriterFileStructure();
//	                MenuAudio menuAudio = new MenuAudio();
//	                menuAudio.mainLoop();
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	        };
//	        Thread thread = new Thread(task);
//	        thread.start();
			
			
			
			
			while ( !glfwWindowShouldClose(display.window) ) {
				
				
				entity.increaseRotation(0, 1, 0);
				//entity1.increasePosition(0, -1, 0);
				camera.move(display);
				
				renderer.prepare();
				
				shader.start();
				shader.loadLight(light);
				
				shader.loadViewMatrix(camera);
				
				renderer.render(entity,shader);
				//renderer.render(entity1, shader);
				
				shader.stop();
				//guirenderer.render();
				textrenderer.renderGrid(texts);
				glfwSwapBuffers(display.window); // swap the color buffers
				
				glfwPollEvents();
				
			
			}
			//guirenderer.cleanUp();
			textrenderer.cleanUp();
			shader.cleanUp();
			loader.cleanUP();
			
			display.close();
			//thread.join();
		
	}

}
