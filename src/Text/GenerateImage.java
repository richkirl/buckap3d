package Text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class GenerateImage {
	private String filepath="Arial";
    private int fontSize=64;

    private int width, height, lineHeight;
    private Map<Integer, CharInfo> characterMap;
	public GenerateImage(String string) {
		this.filepath=string;
		//this.characterMap = new HashMap<>();
		//this.string=string;
		//createImage();
	}
public ByteBuffer createImage(String string) throws IOException {
        
        Font font = new Font(filepath, Font.PLAIN, 64);
        
        // Create fake image to get font information
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();

        //int estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;
        width = 0;
        //height = (int) (fontMetrics.getHeight()* 1.4f);
        height = (int) (fontMetrics.getHeight()/1.4f);
        //int x = 0;
        //int y = (int)(fontMetrics.getHeight() * 1.4f);

        for (int i=0; i < string.length(); i++) {
            //if (font.canDisplay(i)) {
                // Get the sizes for each codepoint glyph, and update the actual image width and height
                CharInfo charInfo = new CharInfo(width,height, fontMetrics.charWidth(string.charAt(i)), fontMetrics.getHeight());
                characterMap.put(string.indexOf(i), charInfo);
                width += fontMetrics.charWidth(string.indexOf(i))/2.4f;
                if(string.charAt(i)=='\n')
                	height += g2d.getFontMetrics().getHeight();

                //x += charInfo.width;
                
            }
        
        height += fontMetrics.getHeight() / 1.4f;
        g2d.dispose();

        // Create the real texture
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        fontMetrics = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        int startX = 0;
        int y=0;
        for (String line : string.split("\n"))
            g2d.drawString(line, startX/2.4f, y += g2d.getFontMetrics().getHeight());
        //}
        g2d.dispose();

        ByteBuffer buf = null;
        try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            out.flush();
            byte[] data = out.toByteArray();
            buf = ByteBuffer.allocateDirect(data.length);
            buf.put(data, 0, data.length);
            buf.flip();
        }
        //uploadTexture(img);
        
        return buf;
        //loader.loadTextureBuf(pixels);
    }
	public ByteBuffer createImageGrid(int w,int h,String s,int size) throws IOException {
		
		Font font = new Font("src/res/JetBrainsMonoNL-Light.ttf", Font.PLAIN, size);
		
		//800=x 600=y {20=x 20=y} = 40elements 30lines
        // Create fake image to get font information
		
		
		
		
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        width = 0;
        //height = (int) (fontMetrics.getHeight()* 1.4f);
        height = 0;
        
       
        int count=0;
        for(int y=0;y<h;y++)
        {
        	if((h/size-20)==28)break;
        	for(int x=0;x<w;x++) {
        		count++;
        		
        		
        		width+=size;
        	}
        	height+=size-20;
        }
        
        
        g2d.dispose();

        // Create the real texture
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.GREEN);
        
        width=0;
        height=size;
        count=600000;
        System.out.println(count);
        int xx=0;
        for(int y=0;y<h;y++)
        {
        	if((h/size-20)==28)break;
        	for(int x=0;x<w;x++) {
        		
        		
        		g2d.drawString(""+ (char)count, width, height);
        		count++;xx++;
        		
        		if(x == w/size) {width=0;height+=size-20;break;}
        		
        		width+=size;
        	}
        	height+=size-20;
        }
        g2d.dispose();
        System.out.println(xx);
        ByteBuffer buf = null;
        try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            out.flush();
            byte[] data = out.toByteArray();
            buf = ByteBuffer.allocateDirect(data.length);
            buf.put(data, 0, data.length);
            buf.flip();
        }
        
		
		return buf;
		
	}
	
	public void createImageGrid1(int w,int h,String s,int size) throws IOException {
		
		Font font = new Font("C:\\eclipse-workspace\\3DEngine\\src\\res\\JetBrainsMonoNL-Light.ttf", Font.PLAIN, size);
		
		//800=x 600=y {20=x 20=y} = 40elements 30lines
        // Create fake image to get font information
		
		int count=0;
		int countImages=0;//width=size|height=size-20
		while(countImages!=26) {
		
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        
        width = 0;
        
        height = 0;
        
       
        
        for(int y=0;y<h;y++)
        {
        	if((h/size-20)==28)break;
        	for(int x=0;x<w;x++) {
        		
        		width+=size;
        	}
        	height+=size-20;
        }
        
        
        g2d.dispose();

        // Create the real texture
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.GREEN);
        
        width=0;
        height=size;
        //count=600000;
        System.out.println(count);
        int xx=0;
        for(int y=0;y<h;y++)
        {
        	if((h/size-20)==28)break;
        	for(int x=0;x<w;x++) {
        		
        		
        		g2d.drawString(""+ (char)count, width, height);
        		count++;xx++;
        		
        		if(x == w/size) {width=0;height+=size-20;break;}
        		
        		width+=size;
        	}
        	height+=size-20;
        }
        g2d.dispose();

        ByteBuffer buf = null;
        try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "bmp", new File("tmp"+countImages+".bmp"));
            out.flush();
            byte[] data = out.toByteArray();
            buf = ByteBuffer.allocateDirect(data.length);
            buf.put(data, 0, data.length);
            buf.flip();
        }
        countImages++;
        img=null;
		
		}
		System.gc();
	}
	
}
