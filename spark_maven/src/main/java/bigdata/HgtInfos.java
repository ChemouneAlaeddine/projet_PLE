package bigdata;

import java.io.Serializable;
import java.util.ArrayList;

public class HgtInfos implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private byte[] heights;
	private double lat;
	private double lng;
	
	public HgtInfos(String fileName, byte[] heights, double lat, double lng) {
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
	public void setHeights(byte[] matrice) {
		this.heights = heights;
	}
	public double getLat() {
		return lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
}
