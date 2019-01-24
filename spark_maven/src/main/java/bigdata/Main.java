package bigdata;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
/*import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;*/
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.ToolRunner;

import scala.Tuple2;

//import bigdata.Computing;

public class Main {
	
	public static int run(String pathIn, String pathOut) throws Exception {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject").setMaster("local[4]");
	    JavaSparkContext sc = new JavaSparkContext(conf);
		
	    ourHBase.HBaseProg hb = new ourHBase.HBaseProg();
	    
	    ToolRunner.run(HBaseConfiguration.create(), hb, null);
	    
		//FileSystem hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
		//Path file = new Path("hdfs://localhost:9000/achemoune/file.txt");
	    
		//RemoteIterator<LocatedFileStatus> fileStatusListIterator = FileSystem.get(sc.hadoopConfiguration()).listFiles(new Path(pathIn), true);
		
		//FileSystem fs = FileSystem.get(new Configuration());
	    
	    /*while(fileStatusListIterator.hasNext()) {
			LocatedFileStatus fileEntry = fileStatusListIterator.next();
			//FileStatus fil = fs.listStatus(pathIn);
			if(!fileEntry.isDirectory() && fileEntry.getPath().getName().endsWith(".hgt")) {
				Computing.createPng(Computing.hgt2mat(pathIn+fileEntry.getPath().getName(), 1201).matrice, pathOut+fileEntry.getPath().getName().substring(0, fileEntry.getPath().getName().indexOf(".")+1)+"png");
			}
		}*/
	    
	    
	    
	    JavaPairRDD<String, PortableDataStream> files = sc.binaryFiles("hdfs://ripoux:9000/user/raw_data/dem3/N44W001.hgt");
	    
		files.foreach(file -> {
			//System.out.println(file._1+"  ==============================  "+file._2);
		    byte[] data = file._2.toArray();
		    String filename = file._1.split("/")[file._1.split(("/")).length-1];
		    //filename = filename.split(".")[0];
		    //System.out.println("=======================\n"+filename+"\n=======================");
		    HgtInfos ex = Computing.hgt2mat(data, 1201, filename);
		    //System.out.println("=======================\n"+file.toString()+"\n=======================");
		    /*System.out.println("=======================\n"+ex.getLat()+"\n=======================");
		    System.out.println("=======================\n"+ex.getLng()+"\n=======================");
		    System.out.println("=======================\n"+ex.getFileName()+"\n=======================");*/
		    
		    BufferedImage bt = Computing.createPng(Computing.hgt2mat(data, 1201, filename).getMatrice(), pathOut+filename.substring(0, filename.indexOf(".")+1)+"png");
		    
		    
		    ourHBase.HBaseProg.addRow(ex.getFileName(), ex.getLat(), ex.getLng(), bt);
		    //String[] str = {filename, Double.toString(ex.getLat()), Double.toString(ex.getLng()), file._2.toString()};
		    
		    

		    
        });
		
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		
		//int result = run("hdfs://young:9000/user/raw_data/dem3","/net/cremi/achemoune/Bureau/");
		int result = run("/net/cremi/achemoune/Bureau/projet_PLE/intro","/net/cremi/achemoune/Bureau/");
		System.exit(result);
		//System.out.println("Hellooo !!");
	    
	}
}
