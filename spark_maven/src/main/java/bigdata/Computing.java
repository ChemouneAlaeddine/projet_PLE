package bigdata;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.input.PortableDataStream;

public class Computing {
    public static HgtInfos hgt2mat(FileStatus file, int lengMat) throws IOException {
    	
    	InputStream inputStream = new FileInputStream("/net/cremi/achemoune/Bureau/projet_PLE/intro/"+file);

		int height[][] = new int[lengMat][lengMat];
	
		if(!file.isDirectory()){
			
			byte[] buffer = new byte[2];
			String filen = new String(file.getPath().toString());
	
			filen = filen.substring(filen.length()-11);
			//System.out.println(filen.substring(1, 3));
			//System.out.println(filen.substring(4, 7));
			double lat = Double.parseDouble(filen.substring(1,3));
			double lng = Double.parseDouble(filen.substring(4,7));
			int minh = 0;
			int maxh = 8000;
	
			if (filen.indexOf(0)=='S' || filen.indexOf(0)=='s') lat *= -1;
			if (filen.indexOf(3)=='W' || filen.indexOf(3)=='w') lng *= 1;
	
			int unsigned0 = 0;
			int unsigned1 = 0;
	
			for (int i = 0; i < lengMat; ++i){
			    for (int j = 0; j < lengMat; ++j) {
			    	
			    	if(inputStream.read(buffer) < 0) {
	                    System.out.println("Error reading file!");
	                    System.exit(-1);
	                }
	
					unsigned0 = ((int)buffer[0] < 0) ? (int)buffer[0] + 256 : (int)buffer[0];
					unsigned1 = ((int)buffer[1] < 0) ? (int)buffer[1] + 256 : (int)buffer[1];
					height[i][j] = (unsigned0 << 8) | unsigned1;
		
					if (height[i][j] > maxh)// || height[i][j] < 0)
					    height[i][j] = maxh;
					minh = Math.min(minh, height[i][j]);
					maxh = Math.max(maxh, height[i][j]);
			    }
			}
			//System.out.println("fileName: "+filen+"\nlat: "+lat+"\nlng: "+lng);
			return new HgtInfos(filen, height, lat, lng);
		}
		
	    System.out.println("Empty file !");
	    return null;
    }

    public static void createPng(int[][] matrice, String path) throws IOException {
		BufferedImage img = new BufferedImage(matrice.length, matrice.length, BufferedImage.TYPE_INT_ARGB);
	
		File ff = null;
	
		for (int j = 0; j < matrice.length; ++j){
		    for (int i = 0; i < matrice.length; ++i) {
		    	//System.out.println(matrice[j][i]);
				img.setRGB(i, j, getColor(matrice[j][i]));
		    }
		}
	
		try{
		    ff = new File(path.toString());
		    ImageIO.write(img, "png", ff);
		}catch(IOException e){
		    System.out.println("Error: " + e);
		}
    }
    
    public static int getColor(int height) {
    	
    	int result = 0;
    	
    	if(height < 2000)
    		result = new Color(255,255,255).getRGB();
    	if(height < 1500)
    		result = new Color(255,255,102).getRGB();
    	if(height < 1000)
    		result = new Color(153,255,51).getRGB();
    	if(height < 500)
    		result = new Color(0,153,0).getRGB();
    	if(height < 250)
    		result = new Color(0,102,0).getRGB();
    	if(height < 100)
    		result = new Color(0,255,255).getRGB();
    	
    	return result;
    }
}
