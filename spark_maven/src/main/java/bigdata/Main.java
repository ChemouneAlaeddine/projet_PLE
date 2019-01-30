package bigdata;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import bigdata.ourHBase.HBaseProg;


public class Main {
	
	private static ourHBase.HBaseProg hb;
	private static final String pathIn = "hdfs://young:9000/user/raw_data/dem3";
	
	public void run(String[] args) throws Exception {
		
		SparkConf conf = new SparkConf().setAppName("MapProject");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		
	    JavaPairRDD<String, PortableDataStream> files = jsc.binaryFiles(pathIn);
	    
		files.foreach(file -> {
			
					String fileName = file._1.split("/")[file._1.split(("/")).length-1];
					
					byte[] arrayData = file._2.toArray();
				    
				    HgtInfos dataFile = Computing.hgt2data(arrayData, fileName);
				    
				    byte[] bi = Computing.createPng(dataFile.getHeights());
				    
				    HBaseProg.addRow(dataFile.getY(), dataFile.getX(), bi);

        });
		jsc.close();
	}
	
	public static void main(String[] args) throws Exception {
		hb = new ourHBase.HBaseProg();
		ToolRunner.run(HBaseConfiguration.create(), hb, args);
	}
}