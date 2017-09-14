package br.com.grafos.algoritmo.cw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix {

	private static final int NOTHING = 0;

	private Double[][] structure;

	/**
	 * storeHouse position i
	 */
	public int shp = -1;


	/**
	 * size of matrix
	 */
	public int size = 0;

	public Matrix() {
	}
	
	public Matrix(List<Double> savingList, int dimension){
		this.size = dimension;
		int c=0;
		this.structure = new Double[dimension][dimension];
		for(int i = 0 ; i < dimension ; i++){
			for(int j = 0 ; j < dimension ; j++){
				this.structure[i][j] = savingList.get(c);
				c++;
			}
		}
	}
	
	public void setPositionStoreHouse(int i) {
		this.shp = i;
	}

	public Double[][] getStructure() {
		return structure;
	}

	public void setStructure(Double[][] structure) {
		this.structure = structure;
		this.size = this.structure[0].length;
	}

	public List<Arc> getArcListOrderBySaving(){
		List<Arc> arcList = new ArrayList<Arc>();
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				Arc arc = new Arc(i+1, j+1, this.structure[i][j]);
				if(!arcList.contains(arc) && arc.getSaving() > NOTHING){
					arcList.add(arc);
				}
			}
		}
		Collections.sort(arcList);
		return arcList;
	}
	
	//TODO REMOVE THIS
	public static void main(String[] args) {
		List<Double> savings = new ArrayList<Double>();
		savings.add(1d);
		savings.add(2d);
		savings.add(3d);
		savings.add(4d);
		savings.add(5d);
		savings.add(6d);
		savings.add(7d);
		savings.add(8d);
		savings.add(9d);
		
		br.com.grafos.algoritmo.cw.model.Matrix m = new br.com.grafos.algoritmo.cw.model.Matrix(savings,3);
		System.out.println(m.size);
		
		
		
	}
	
}
