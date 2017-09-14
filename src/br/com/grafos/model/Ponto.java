package br.com.grafos.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Ponto {

	private Integer id;
	private Integer x;
	private Integer y;
	private boolean storeHouse;
	
	public Ponto(){}
	
	public Ponto(Integer id){
		this.id = id;
	}

	public Ponto(Integer id, Integer x, Integer y){
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public Ponto(Integer id, Integer x, Integer y, Boolean storeHouse){
		this.id = id;
		this.x = x;
		this.y = y;
		this.storeHouse = storeHouse;
	}

	public Ponto(String jsonStr){
		try {
			JSONObject jsonObject = new JSONObject(new JSONTokener(jsonStr));
			this.id = jsonObject.getInt("id");
			this.x = jsonObject.getInt("x");
			this.y = jsonObject.getInt("y");
			this.storeHouse = jsonObject.getBoolean("storeHouse");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getX() {
		return x;
	}
	
	public void setX(Integer x) {
		this.x = x;
	}
	
	public Integer getY() {
		return y;
	}
	
	public void setY(Integer y) {
		this.y = y;
	}

	public boolean isStoreHouse() {
		return storeHouse;
	}

	public void setStoreHouse(boolean storeHouse) {
		this.storeHouse = storeHouse;
	}
	
	
	
}