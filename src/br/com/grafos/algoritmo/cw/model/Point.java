package br.com.grafos.algoritmo.cw.model;

public class Point {

	private Double x;
	private Double y;
	private boolean storeHouse;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Boolean getStoreHouse() {
		return storeHouse;
	}

	public void setStoreHouse(Boolean storeHouse) {
		this.storeHouse = storeHouse;
	}

}
