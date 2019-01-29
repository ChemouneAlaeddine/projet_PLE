package bigdata;

import org.apache.hadoop.util.ToolRunner;
import org.apache.spark.SparkConf;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

public class Main {
	
	
	private static JavaSparkContext sc;
	private static ourHBase.HBaseProg hb;
	

	public static int run(String pathIn) throws Exception {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject");
	    sc = new JavaSparkContext(conf);
	    
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
				    
				    hb.addRow(dataFile.getY(), dataFile.getX(), bi);

			}catch(Exception e) {}
        });
		
		
		
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		hb = new ourHBase.HBaseProg();
	    ToolRunner.run(HBaseConfiguration.create(), hb, args);
		int result = run("hdfs://young:9000/user/raw_data/dem3");
		System.exit(result);
	}
}
