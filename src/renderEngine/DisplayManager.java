package renderEngine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import Music.LameTest;
import Music.StructureOptions;
import Music.VorbisTest;
import Music.WavFormS;
import engineTester.MyPoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {

	// The window handle
	public long window;
	public int width=1280,heihght=768;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeihght() {
		return heihght;
	}

	public void setHeihght(int heihght) {
		this.heihght = heihght;
	}

	public long getWindow() {
		return this.window;
	}

	public void setWindow(long window) {
		this.window = window;
	}

	public boolean isKeyPressed(int keycode){
        return glfwGetKey(window,keycode)==GLFW_PRESS;
    }
	public void run() {
		//System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		prepare();
		//loop();

		// Free the window callbacks and destroy the window
		//glfwFreeCallbacks(window);
		//glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		//glfwTerminate();
		//glfwSetErrorCallback(null).free();
	}

	
	
	
	/**
	 * 
	 */
	public void init() {
		//MyPoint mypoint = new MyPoint(0,0);
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1280, 768, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		setWidth(1280);setHeihght(768);
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			if ( key == GLFW_KEY_1 && action == GLFW_RELEASE )
			{
				Runnable task = ()-> {
		            try {
		                TimeUnit.SECONDS.sleep(1);
		                try {
		    				StructureOptions structureOptions=new StructureOptions();

		                    FileInputStream inFile1 = new FileInputStream(structureOptions.file);
		                    byte[] b = new byte[inFile1.available()];
		                    inFile1.read(b,0,inFile1.available());
	                    String s1 = new String(b);

		                    String[] words = s1.split("\n");


		                    for(int i=0;i< words.length;++i)
		                    {
		                        if(Objects.equals(words[i], "//IN"))
		                        {
		                            ++i;
		                            while(!Objects.equals(words[i], "//OUT")){
		                                if(Objects.equals(words[i], "//OUT"))break;
		                                System.out.println(words[i]);
		                                structureOptions.listplayAudio.add(words[i]);
		                                ++i;
		                            }
		                        }
		                    }
		                    try {
		    					inFile1.close();
		    				} catch (IOException e1) {
		    					// TODO Auto-generated catch block
		    					e1.printStackTrace();
		    				}
		                    System.out.println("\n");
		                    //System.out.println((structureOptions.listplayAudio.get(0)));
		                    //List<String> list = new ArrayList<>();
		                    for(int y=0;y<structureOptions.listplayAudio.size();++y) {
		                        File f = new File(structureOptions.listplayAudio.get(y)); // current directory
		                        File[] files = f.listFiles();
		                        for (File file : files) {

		                            if(file.isFile()) {
		                                if(file.getName().contains(".mp3")) {
		                                    System.out.print("file:\t");
		                                    System.out.println(file.getName());
		                                    try {
		    									LameTest.test(file.getAbsolutePath());
	    								} catch (IOException e) {
		    									// TODO Auto-generated catch block
		    									e.printStackTrace();
		    								}
		                                }
		                                if(file.getName().contains(".ogg")) {
		                                    System.out.print("file:\t");
		                                    System.out.println(file.getName());
		                                    //list.add(file.getAbsolutePath());
		                                    try {
		    									VorbisTest.play(file.getAbsolutePath());
		    								} catch (Exception e) {
		    									// TODO Auto-generated catch block
		    									e.printStackTrace();
		    								}
		                                }
		                                if(file.getName().contains(".wav")) {
		                                    System.out.print("file:\t");
		                                    System.out.println(file.getName());
		                                    //list.add(file.getAbsolutePath());
		                                    try {
		    									WavFormS.play(file.getAbsolutePath());
		    								} catch (IOException e) {
		    									// TODO Auto-generated catch block
		    									e.printStackTrace();
		    								}
		                                }
		                            }
		                        }
		                    }
		    				}catch(IOException e) {
		    					e.printStackTrace();
		    					}
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        };
		        Thread thread = new Thread(task);
		        thread.start();

			}


			if ( key == GLFW_KEY_2 && action == GLFW_RELEASE )
				System.out.print("2");
			if ( key == GLFW_KEY_3 && action == GLFW_RELEASE )
				System.out.print("3");
			if ( key == GLFW_KEY_4 && action == GLFW_RELEASE )
				System.out.print("4");
		});
		//window.isKeyPressed(GLFW.GLFW_KEY_W);
		// Get the thread stack and push a new frame
		
		glfwSetCursorPosCallback(window, (window,x,y )->{
			System.out.println("x "+x +" y"+y);
		});
		
		
		//glfwSetCursorPosCallback(window, GLFWCursorPosCallbackI);
		
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(getWindow());
		
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
		
		glfwSetWindowSizeCallback(window, (window,width,height )->{
			glViewport(0, 0, width, height);
		});
			
		
		
	}

	public void prepare() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		
	}
	public void close() {
		// Free the window callbacks and destroy the window
				glfwFreeCallbacks(window);
				glfwDestroyWindow(window);

				// Terminate GLFW and free the error callback
				glfwTerminate();
				glfwSetErrorCallback(null).free();
	}
}
