package bigdata;

import java.io.IOException;


import org.apache.hadoop.conf.Configured;
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


public class ourHBase {

    public static class HBaseProg extends Configured implements Tool {
    private static final byte[] TABLE_NAME = Bytes.toBytes("ourdb2");
	private static final byte[] FAMILY = Bytes.toBytes("familyName");
	
	private static final byte[] LATLONG    = Bytes.toBytes("latlong");
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
	
	public static void addRow(String fileName, int lat, int lng, byte[] img) {
		
		Put put = new Put(Bytes.toBytes(fileName));
		
		byte[] ltlg = {(byte)lat,(byte)lng};
		
	    put.addColumn(FAMILY, LATLONG, ltlg);
	    put.addColumn(FAMILY, DATA, img);
	    
	    try {
	    	
	    	table.put(put);
	    
	    }catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(-1);
	    }
	    
	    /*try {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write( img, "png", baos);
		    baos.flush();
		    byte[] imageInByte = baos.toByteArray();
		    
		put.addColumn(FAMILY, DATA, img);
		    
		    table.put(put);
		    
	    }catch(IOException e) {
	    	e.printStackTrace();
	    	System.exit(-1);
	    }*/
	}

	@Override
	public int run(String[] args) throws IOException {
	    Connection connection = ConnectionFactory.createConnection(getConf());
	    createTable(connection);
	    table = connection.getTable(TableName.valueOf(TABLE_NAME));   
	    return 0;
	}
    }
}
