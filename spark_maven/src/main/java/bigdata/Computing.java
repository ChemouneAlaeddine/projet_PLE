package bigdata;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.fs.FileStatus;

public class Computing {
    public static int[][] hgt2mat(FileStatus hgtFile, int lengMat) throws IOException {

		int srtm_ver = 1201;
		int height[][] = new int[1201][1201];
	
		if(!hgtFile.isDirectory()){
			
			byte[] buffer = new byte[2];
			String filen = new String(hgtFile.getPath().toString());
	
			filen = filen.substring(filen.length()-11);
			//System.out.println(filen.substring(1, 3));
			//System.out.println(filen.substring(4, 7));
			double lat = Double.parseDouble(filen.substring(1,3));
			double lng = Double.parseDouble(filen.substring(4,7));
			int minh = 0;
			int maxh = 255;
	
			if (filen.indexOf(0)=='S' || filen.indexOf(0)=='s') lat *= -1;
			if (filen.indexOf(3)=='W' || filen.indexOf(3)=='w') lng *= -1;
	
			int unsigned0 = 0;
			int unsigned1 = 0;
	
			for (int i = 0; i<srtm_ver; ++i){
			    for (int j = 0; j < srtm_ver; ++j) {
	
					unsigned0 = ((int)buffer[0] < 0) ? (int)buffer[0] + 256 : (int)buffer[0];
					unsigned1 = ((int)buffer[1] < 0) ? (int)buffer[1] + 256 : (int)buffer[1];
					height[i][j] = (unsigned0 << 8) | unsigned1;
		
					if (height[i][j] > maxh)// || height[i][j] < 0)
					    height[i][j] = maxh;
					minh = Math.min(minh, height[i][j]);
					maxh = Math.max(maxh, height[i][j]);
			    }
			}
		}else {
		    System.out.println("Empty file !");
		    System.exit(-1);
		}
		
		return height;
    }

    public static void createPng(int[][] matrice, String path) throws IOException {
		BufferedImage img = new BufferedImage(matrice.length, matrice.length, BufferedImage.TYPE_INT_ARGB);
	
		File ff = null;
	
		for (int j = 0; j<matrice.length; ++j){
		    for (int i = 0; i < matrice.length; ++i) {
				int val = matrice[j][i];
		
				int a = val;//(int)(Math.random()*256); //alpha
				int r = val;//(int)(Math.random()*256); //red
				int g = val;//(int)(Math.random()*256); //green
				int b = val;//(int)(Math.random()*256); //blue
		
				int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
		
				img.setRGB(i, j, p);
		    }
		}
	
		try{
		    ff = new File(path.toString());
		    ImageIO.write(img, "png", ff);
		}catch(IOException e){
		    System.out.println("Error: " + e);
		}
    }
}
