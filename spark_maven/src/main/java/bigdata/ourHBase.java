package bigdata;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
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


public class ourHBase {

    public static class HBaseProg extends Configured implements Tool {
    private static final byte[] TABLE_NAME = Bytes.toBytes("ourdb");
    private static final byte[] FAMILY = Bytes.toBytes("familyName");
	
	private static final byte[] Y_X    = Bytes.toBytes("y_x");
	private static final byte[] DATA    = Bytes.toBytes("data");
	
	private static Connection connection = null;
	private static Table table = null;
	

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
	
	public static Table getTable() throws IOException {
		if(table == null) {
			table = getConnection().getTable(TableName.valueOf(TABLE_NAME));
		}
		return table;
	}
	
	public static Connection getConnection () throws IOException{
		if(connection == null) {
			Configuration hbconf = HBaseConfiguration.create();
			connection = ConnectionFactory.createConnection(hbconf);
		}
		return connection;
	}
	
	public static void addRow(int y, int x, byte[] img) throws IOException {
		String st = new String("x"+x+"y"+y);
		Put put = new Put(Bytes.toBytes(st));
		
		byte[] y_x = {(byte)y,(byte)x};
	    put.addColumn(FAMILY, Y_X, y_x);
	    put.addColumn(FAMILY, DATA, img);
	    HBaseProg.getTable().put(put);
	}

	@Override
	public int run(String[] args) throws IOException {
	    createTable(getConnection());
	    table = getTable();
	    return 0;
	}
    }
}
