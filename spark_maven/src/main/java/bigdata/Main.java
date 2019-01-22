package bigdata;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configured;
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
import org.apache.hadoop.util.ToolRunner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import bigdata.Computing;

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
	    
		SparkConf conf = new SparkConf().setAppName("MapProject").setMaster("local[2]");
	    JavaSparkContext sc = new JavaSparkContext(conf);
		
		//FileSystem hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
		//Path file = new Path("hdfs://localhost:9000/achemoune/file.txt");
		
		RemoteIterator<LocatedFileStatus> fileStatusListIterator = FileSystem.get(sc.hadoopConfiguration()).listFiles(new Path(pathIn), true);
		
		while(fileStatusListIterator.hasNext()) {
			LocatedFileStatus fileEntry = fileStatusListIterator.next();
			if(!fileEntry.isDirectory() && fileEntry.getPath().getName().endsWith(".hgt")) {
				Computing.createPng(Computing.hgt2mat(fileEntry, 1201).matrice, pathOut+fileEntry.getPath().getName().substring(0, fileEntry.getPath().getName().indexOf(".")+1)+"png");
			}
		}
		
	    return 0;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		int result = run("/net/cremi/achemoune/Bureau/projet_PLE/intro","/net/cremi/achemoune/Bureau/");
		System.exit(result);
		//System.out.println("Hellooo !!");
	    
	}
}
