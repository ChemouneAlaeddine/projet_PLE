package bigdata;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.ToolRunner;

import scala.Tuple2;

public class Main {
	
	public static int run(String pathIn) throws Exception {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject").setMaster("local[4]");
	    JavaSparkContext sc = new JavaSparkContext(conf);
		
	    //ourHBase.HBaseProg hb = new ourHBase.HBaseProg();
	    
	    //ToolRunner.run(HBaseConfiguration.create(), hb, null);	    
	    
	    JavaPairRDD<String, PortableDataStream> files = sc.binaryFiles(pathIn);
	    
		files.foreach(file -> {
		    byte[] arrayData = file._2.toArray();
		    String fileName = file._1.split("/")[file._1.split(("/")).length-1];
		    
		    HgtInfos dataFile = Computing.hgt2data(arrayData, fileName);
		    
		    //BufferedImage bi = Computing.createPng(dataFile.getHeights());
		    
		    ourHBase.HBaseProg.addRow(dataFile.getFileName(), dataFile.getLat(), dataFile.getLng(), dataFile.getHeights());
		    //for(int i=0; i< arrayData.length; i++)
		    	//System.out.println("===============================\n"+arrayData[i]+"\n===============================");
		    System.out.println("===============================\n"+fileName+" "+dataFile.getLat()+" "+dataFile.getLng()+"\n===============================");
        });
		
		
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int result = run("hdfs://ripoux:9000/user/raw_data/dem3/N44W002.hgt");
		System.exit(result);
	}
}
