package bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.awt.image.BufferedImage;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.ToolRunner;

public class Main {
	
	private static JavaSparkContext sc;
	

	public static int run(String pathIn) throws Exception {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject");
	    sc = new JavaSparkContext(conf);
		
	    ourHBase.HBaseProg hb = new ourHBase.HBaseProg();
	    
	    ToolRunner.run(HBaseConfiguration.create(), hb, null);	    
	    
	    JavaPairRDD<String, PortableDataStream> files = sc.binaryFiles(pathIn);
	    
	    
		files.foreach(file -> {
			
			try {
				String fileName = file._1.split("/")[file._1.split(("/")).length-1];
				
				byte[] arrayData = file._2.toArray();
			    
				if(!fileName.equals("N00E128.hgt") && !fileName.equals("N63E008.hgt")){
					if(arrayData != null) {
				
					if(arrayData.length == 2884802) {
					
				    HgtInfos dataFile = Computing.hgt2data(arrayData, fileName);
				    
				    //BufferedImage bi = Computing.createPng(dataFile.getHeights(), fileName);
				    
				    ourHBase.HBaseProg.addRow(dataFile.getFileName(), dataFile.getLat(), dataFile.getLng(), dataFile.getHeights());
					}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
        });
		
		
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int result = run("hdfs://young:9000/user/raw_data/dem3");
		System.exit(result);
	}
}
