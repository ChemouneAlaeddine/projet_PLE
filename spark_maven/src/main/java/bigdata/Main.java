package bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.util.Tool;

public class Main {
	
	
	private static JavaSparkContext sc;
	private static ourHBase.HBaseProg hb;
	

	public static int run(String pathIn) throws Exception {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject");
	    sc = new JavaSparkContext(conf);
	    
	    hb = new ourHBase.HBaseProg();
	    
	    JavaPairRDD<String, PortableDataStream> files = sc.binaryFiles(pathIn);
	    
	    
		files.foreach(file -> {
			
			try {
					String fileName = file._1.split("/")[file._1.split(("/")).length-1];
					
					byte[] arrayData = file._2.toArray();
				    
					if(fileName.equals("N00E128.hgt") || fileName.equals("N63E008.hgt")){
						return;
					}
				    HgtInfos dataFile = Computing.hgt2data(arrayData, fileName);
				    
				    byte[] bi = Computing.createPng(dataFile.getHeights(), fileName);
				    
				    hb.addRow(dataFile.getFileName(), dataFile.getY(), dataFile.getX(), bi);

			}catch(Exception e) {}
        });
		
		
		
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int result = run("hdfs://ripoux:9000/user/raw_data/dem3");
		System.exit(result);
	}
}
