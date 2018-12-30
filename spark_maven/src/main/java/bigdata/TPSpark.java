package bigdata;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TPSpark {
	public static void main(String[] args) throws IOException {
		
		//SparkConf conf = new SparkConf().setAppName("TP Spark");
		//JavaSparkContext context = new JavaSparkContext(conf);
		
		/*if (args.length == 0) {
            System.out.println("No files provided.");
            System.exit(0);
        }*/
		
		//JavaRDD<String> textFile = context.textFile(args[0]);
		File f = new File(/*args[0]*/"/home/chemoune/Bureau/bigdata/projet/N43W002.hgt");
		FileInputStream fileInputStream = new FileInputStream(f);
		
		int srtm_ver = 1201;
	    int height[][] = new int[1201][1201];
	    System.out.println("P2\n"+srtm_ver+" "+srtm_ver);
	    
	    if(f.exists() && !f.isDirectory())
	    {
	    	byte[] buffer = new byte[2]; 
	        String filen = new String(f.toString());
	        
	        filen = filen.substring(filen.length()-11);
	        //System.out.println(filen.substring(1, 3));
	        //System.out.println(filen.substring(4, 7));
	        double lat = Double.parseDouble(filen.substring(1,3));
	        double lng = Double.parseDouble(filen.substring(4,7));
		    int minh = 0;
		    int maxh = 255;
	    		
		    if (filen.indexOf(0)=='S' || filen.indexOf(0)=='s') lat *= -1;
	        if (filen.indexOf(3)=='W' || filen.indexOf(3)=='w') lng *= -1;
	        
	        int buff0 = 0;
	        int buff1 = 0;
	        
	        for (int i = 0; i<srtm_ver; ++i){
	            for (int j = 0; j < srtm_ver; ++j) {
	            	
	            	if(fileInputStream.read(buffer) < 0) {
	                    System.out.println("Error reading file!");
	                    System.exit(-1);
	                }
	            	
	            	buff0 = ((int)buffer[0] < 0) ? (int)buffer[0] + 256 : (int)buffer[0];
	            	buff1 = ((int)buffer[1] < 0) ? (int)buffer[1] + 256 : (int)buffer[1];
	        	    height[i][j] = (buff0 << 8) | buff1;
	        	    
	        	    if (height[i][j] > maxh)// || height[i][j] < 0)
	        		    height[i][j] = maxh;
	        	    minh = Math.min(minh, height[i][j]);
	        	    maxh = Math.max(maxh, height[i][j]);
	            }
	        }
	    	
	        maxh -= minh;
	        //System.out.println("maxh = "+maxh);
	        //System.out.println("minh = "+minh);
	        
	        //create buffered image object img
	        BufferedImage img = new BufferedImage(srtm_ver, srtm_ver, BufferedImage.TYPE_INT_ARGB);
	        //file object
	        File ff = null;
	        
	        for (int j = 0; j<srtm_ver; ++j){
	            for (int i = 0; i < srtm_ver; ++i) {
		           int val = height[j][i] - minh;
		           //System.out.print(val+" ");
		           
		            int a = val;//(int)(Math.random()*256); //alpha
		            int r = val;//(int)(Math.random()*256); //red
		            int g = val;//(int)(Math.random()*256); //green
		            int b = val;//(int)(Math.random()*256); //blue
		    
		            int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
		    
		            img.setRGB(i, j, p);
	            }
	            System.out.println("\n");
	        }
	        //write image
	        try{
	          ff = new File("/home/chemoune/Bureau/Output.png");
	          ImageIO.write(img, "png", ff);
	        }catch(IOException e){
	          System.out.println("Error: " + e);
	        }
	    	
	    }else {
	    	System.out.println("Empty file !");
	    }
	    
	    
	    /*
	    int max = 0;
	    for (int i = 0; i<srtm_ver; ++i)
	        for (int j = 0; j < srtm_ver; ++j) {
	            if (height[i][j] < 30000)
	            	max = Math.max(height[i][j], max);
	        }
	    System.out.println("P2");
	    System.out.println(srtm_ver+" "+srtm_ver);
	    System.out.println(max);
	    for (int i = 0; i<srtm_ver; ++i) {
	        for (int j = 0; j < srtm_ver; ++j) {
	            System.out.print(height[i][j]+" ");
	        }
	        System.out.print("");
	    }
	    */
	    
	    
	    System.out.println("Succefully.");
	}
}