package bigdata;

import java.io.Serializable;

public class HgtInfos implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] heights;
	private int y;
	private int x;
	
	public HgtInfos(String fileName, byte[] heights, int y, int x) {
		this.fileName = fileName;
		this.heights = heights;
		this.y = y;
		this.x = x;
	}
	
	public HgtInfos() {}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getHeights() {
		return heights;
	}
	public void setHeights(byte[] heights) {
		this.heights = heights;
	}
	public int getY() {
		return y;
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setX(int x) {
		this.x = x;
	}
}
