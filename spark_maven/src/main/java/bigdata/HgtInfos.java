package bigdata;

import java.io.Serializable;

public class HgtInfos implements Serializable {
	
	private String fileName;
	private int[][] matrice;
	private double lat;
	private double lng;
	
	public HgtInfos(String fileName, int[][] matrice, double lat, double lng) {
		this.fileName = fileName;
		this.matrice = matrice;
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
	public int[][] getMatrice() {
		return matrice;
	}
	public void setMatrice(int[][] matrice) {
		this.matrice = matrice;
	}
	public double getLat() {
		return lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLat(int lat) {
		this.lat = lat;
	}
	public void setLng(int lng) {
		this.lng = lng;
	}
}
