
package bigdata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Computing {

    private static final int SIZE = 1201;
    private static final int MAX_HEIGHT = 8000;
    
    public static HgtInfos hgt2data(byte[] file, String filen) throws IOException {
        
    	InputStream inputStream = new ByteArrayInputStream(file);
		
			byte[] buffer = new byte[2];
	
			filen = filen.substring(filen.length()-11);
			int lat = Integer.parseInt(filen.substring(1,3));
			int lng = Integer.parseInt(filen.substring(4,7));
			int minh = 0;
			int maxh = MAX_HEIGHT;
			byte[] heights = new byte[SIZE*SIZE];
	
			if (filen.indexOf(0)=='S' || filen.indexOf(0)=='s') lat *= -1;
			if (filen.indexOf(3)=='W' || filen.indexOf(3)=='w') lng *= -1;
	
			int unsigned0 = 0;
			int unsigned1 = 0;
	
			for (int i = 0; i < SIZE; ++i){
			    for (int j = 0; j < SIZE; ++j) {
			    	
			    	if(inputStream.read(buffer) < 0) {
	                    heights[(i*SIZE)+j] = 0;
	                    continue;
	                }
	
					unsigned0 = ((int)buffer[0] < 0) ? (int)buffer[0] + 256 : (int)buffer[0];
					unsigned1 = ((int)buffer[1] < 0) ? (int)buffer[1] + 256 : (int)buffer[1];
					heights[(i*SIZE)+j] = (byte)((unsigned0 << 8) | unsigned1);
		
					if (heights[(i*SIZE)+j] > (byte)maxh)
						heights[(i*SIZE)+j] = (byte)maxh;
					minh = Math.min(minh, heights[(i*SIZE)+j]);
					maxh = Math.max(maxh, heights[(i*SIZE)+j]);
			    }
			}
			int x = lng + 180;
			int y = 90 - lat;
			return new HgtInfos(filen, heights, y, x);
		}

    public static byte[] createPng(byte[] list, String fileName) throws IOException {

	BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        
		for (int j = 0; j < SIZE; ++j){
		    for (int i = 0; i < SIZE; ++i) {
			img.setRGB(i, j, getColor(list[(j*SIZE)+i]));
		    }
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(img, "png", baos);
	    baos.flush();
	    byte[] imageInByte = baos.toByteArray();
	    baos.close();
	    
	    return imageInByte;
    }
    
    
    public static int getColor(int height) {
    	
    	int result = 0;
    	
    	if(height < 256)
    		result = new Color(255,255,255).getRGB();
    	if(height < 200)
    		result = new Color(255,255,102).getRGB();
    	if(height < 150)
    		result = new Color(153,255,51).getRGB();
    	if(height < 100)
    		result = new Color(0,153,0).getRGB();
    	if(height < 50)
    		result = new Color(0,102,0).getRGB();
    	if(height < 25)
    		result = new Color(0,255,255).getRGB();
    	
    	return result;
    }
}
