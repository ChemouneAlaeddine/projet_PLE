package bigdata;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configured;
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


public class ourHBase {

    public static class HBaseProg extends Configured implements Tool {
    private static final byte[] TABLE_NAME = Bytes.toBytes("achemoune_bfaik");
	private static final byte[] FAMILY = Bytes.toBytes("familyName");
	
	private static final byte[] FILE_NAME    = Bytes.toBytes("file_name");
	private static final byte[] LAT    = Bytes.toBytes("lat");
	private static final byte[] LNG    = Bytes.toBytes("lng");
	private static final byte[] DATA    = Bytes.toBytes("data");
	
	private static Table table;
	

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
	
	public static void addRow(String fileName, double lat, double lng, BufferedImage img) {
		
		Put put = new Put(Bytes.toBytes(fileName));
		    
	    put.addColumn(FAMILY, FILE_NAME, Bytes.toBytes(fileName));
	    put.addColumn(FAMILY, LAT, Bytes.toBytes(lat));
	    put.addColumn(FAMILY, LNG, Bytes.toBytes(lng));
	    
	    try {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write( img, "png", baos);
		    baos.flush();
		    byte[] imageInByte = baos.toByteArray();
		    
		    put.addColumn(FAMILY, DATA, imageInByte);
		    
		    table.put(put);
		    
	    }catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(-1);
	    }
	}

	@Override
	public int run(String[] args) throws IOException {
	    Connection connection = ConnectionFactory.createConnection(getConf());
	    createTable(connection);
	    table = connection.getTable(TableName.valueOf(TABLE_NAME));
	    
//	    Put put = new Put(Bytes.toBytes(args[3]));
//	    
//	    put.addColumn(FAMILY, FILE_NAME, Bytes.toBytes(args[0]));
//	    put.addColumn(FAMILY, LAT, Bytes.toBytes(args[1]));
//	    put.addColumn(FAMILY, LNG, Bytes.toBytes(args[2]));
//	    byte[] bt = Computing.createPng(Computing.hgt2mat(args[3], 1201, args[0]).getMatrice(), args[0].substring(0, args[0].indexOf(".")+1)+"png");
//	    //byte[] data = args[3].getBytes();
//	    put.addColumn(FAMILY, DATA, bt);
	    
	    /*table.put(put);
	    table.close();
	    connection.close();*/
	    
	    return 0;
	}

    }

    /*public static void main(String[] args) throws Exception {
	int exitCode = ToolRunner.run(HBaseConfiguration.create(), new ourHBase.HBaseProg(), args);
	System.exit(exitCode);
    }*/
}
