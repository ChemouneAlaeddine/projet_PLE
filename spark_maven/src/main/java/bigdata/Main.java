package bigdata;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import bigdata.Computing;

public class Main {
	
	public static int run(String pathIn, String pathOut) throws IOException {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject").setMaster("local[2]");
	    JavaSparkContext sc = new JavaSparkContext(conf);
		
		//FileSystem hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
		//Path file = new Path("hdfs://localhost:9000/achemoune/file.txt");
		
		RemoteIterator<LocatedFileStatus> fileStatusListIterator = FileSystem.get(sc.hadoopConfiguration()).listFiles(new Path(pathIn), true);
		
		while(fileStatusListIterator.hasNext()) {
			LocatedFileStatus fileEntry = fileStatusListIterator.next();
			if(!fileEntry.isDirectory() && fileEntry.getPath().getName().endsWith(".hgt")) {
				Computing.createPng(Computing.hgt2mat(fileEntry, 1201), pathOut+fileEntry.getPath().getName().substring(0, fileEntry.getPath().getName().indexOf(".")+1)+"png");
			}
		}
		
	    return 0;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		int result = run("/net/cremi/achemoune/Bureau/projet_PLE/intro","/net/cremi/achemoune/Bureau/");
		System.exit(result);
	    
	}
}
