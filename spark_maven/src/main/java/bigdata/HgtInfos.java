package bigdata;

import java.io.Serializable;

public class HgtInfos implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] heights;
	private int lat;
	private int lng;
	
	public HgtInfos(String fileName, byte[] heights, int lat, int lng) {
		this.fileName = fileName;
		this.heights = heights;
		this.lat = lat;
		this.lng = lng;
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
	public int getLat() {
		return lat;
	}
	public int getLng() {
		return lng;
	}
	public void setLat(int lat) {
		this.lat = lat;
	}
	public void setLng(int lng) {
		this.lng = lng;
	}
}
