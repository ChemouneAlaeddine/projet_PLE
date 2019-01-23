package bigdata;

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

import scala.Tuple2;

//import bigdata.Computing;

public class Main {
	
	/*public static class HBaseProg extends Configured implements Tool {
		private static final byte[] FAMILY = Bytes.toBytes("familyName");
		private static final byte[] ROW    = Bytes.toBytes("rowName");
		private static final byte[] TABLE_NAME = Bytes.toBytes("tableName");

		public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
		    if (admin.tableExists(table.getTableName())) {
			admin.disableTable(table.getTableName());
			admin.deleteTable(table.getTableName());
		    }
		    admin.createTable(table);
		}

		public static void createTable(Connection connect) {
		    try {
				final Admin admin = connect.getAdmin();
				HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
				HColumnDescriptor famLoc = new HColumnDescriptor(FAMILY);
				//famLoc.set...
				tableDescriptor.addFamily(famLoc);
				createOrOverwrite(admin, tableDescriptor);
				admin.close();
		    } catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
		    }
		}

		public int run(String[] args) throws IOException {
		    Connection connection = ConnectionFactory.createConnection(getConf());
		    createTable(connection);
		    Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		    Put put = new Put(Bytes.toBytes("keyToPut"));
		    table.put(put);
		    return 0;
		}

	}

    public static void main(String[] args) throws Exception {
    	int exitCode = ToolRunner.run(HBaseConfiguration.create(), new Main.HBaseProg(), args);
		System.exit(exitCode);
    }*/
	
	public static int run(String pathIn, String pathOut) throws IOException {
	    
		SparkConf conf = new SparkConf().setAppName("MapProject").setMaster("local[4]");
	    JavaSparkContext sc = new JavaSparkContext(conf);
		
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
	    
	    JavaPairRDD<String, PortableDataStream> files = sc.binaryFiles("hdfs://young:9000/user/raw_data/dem3/N44W002.hgt");
	    
		files.foreach(file -> {
			//System.out.println(file._1+"  ==============================  "+file._2);
		    byte[] data = file._2.toArray();
		    String filename = file._1.split("/")[file._1.split(("/")).length-1];
		    //filename = filename.split(".")[0];
		    //System.out.println("=======================\n"+filename+"\n=======================");
		    HgtInfos ex = Computing.hgt2mat(data, 1201, filename);
		    //System.out.println("=======================\n"+file.toString()+"\n=======================");
		    System.out.println("=======================\n"+ex.lat+"\n=======================");
		    System.out.println("=======================\n"+ex.lng+"\n=======================");
		    Computing.createPng(Computing.hgt2mat(data, 1201, filename).matrice, pathOut+filename.substring(0, filename.indexOf(".")+1)+"png");
        });
		
	    return 0;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		//int result = run("hdfs://young:9000/user/raw_data/dem3","/net/cremi/achemoune/Bureau/");
		int result = run("/net/cremi/achemoune/Bureau/projet_PLE/intro","/net/cremi/achemoune/Bureau/");
		System.exit(result);
		//System.out.println("Hellooo !!");
	    
	}
}
