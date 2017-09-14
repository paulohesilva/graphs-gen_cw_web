package br.com.grafos.algoritmo.cw.helper;

import java.util.ArrayList;
import java.util.List;

import br.com.grafos.algoritmo.cw.model.Arc;
import br.com.grafos.algoritmo.cw.model.Matrix;
import br.com.grafos.algoritmo.cw.model.Point;
import br.com.grafos.model.Ponto;

public class CalculateHelper {

	private static final double NOTHING = 0.0;

	public double distance(Ponto p, Ponto q) {
		double dx = p.getX() - q.getX();
		double dy = p.getY() - q.getY();
		double dist = Math.sqrt(dx * dx + dy * dy);
		return dist;
	}

	//	public double distance(Point p, Point q) {
//		double dx = p.getX() - q.getX();
//		double dy = p.getY() - q.getY();
//		double dist = Math.sqrt(dx * dx + dy * dy);
//		return dist;
//	}

	
	/**
	 * TODO REMOVE THIS  
	 * 
	 * @param pointList
	 * @return Distance Matrix
	 */
//	public Matrix getMatrixDistance(List<Point> pointList) {
//		Matrix matrixDistance = new Matrix();
//		matrixDistance.setStructure(new Double[pointList.size()][pointList
//				.size()]);
//		for (int i = 0; i < pointList.size(); i++) {
//			if (pointList.get(i).getStoreHouse())
//				matrixDistance.setPositionStoreHouse(i);
//			for (int j = 0; j < pointList.size(); j++) {
//				matrixDistance.getStructure()[i][j] = this.distance(
//						pointList.get(i), pointList.get(j));
//			}
//		}
//		return matrixDistance;
//	}

	/**
	 *  
	 * 
	 * @param pointList
	 * @return Distance Matrix
	 */
	public Matrix getMatrixDistance(List<Ponto> pointList) {
		Matrix matrixDistance = new Matrix();
		matrixDistance.setStructure(new Double[pointList.size()][pointList
		                                                         .size()]);
		for (int i = 0; i < pointList.size(); i++) {
			if (pointList.get(i).isStoreHouse())
				matrixDistance.setPositionStoreHouse(i);
			for (int j = 0; j < pointList.size(); j++) {
				matrixDistance.getStructure()[i][j] = this.distance(pointList.get(i), pointList.get(j));
			}
		}
		return matrixDistance;
	}

	/**
	 * 
	 * @param Matrix matrixDistance
	 * @return savings Matrix
	 */
	public Matrix getMatrixEconomy(Matrix matrixDistance) {
		List<Double> savingList = new ArrayList<Double>();
		Matrix savings = null;
		for (int i = 0; i < matrixDistance.size ; i++) {
			for (int j = 0; j < matrixDistance.size ; j++) {				
				if (i != matrixDistance.shp && j != matrixDistance.shp) {					
					if (i != j) {
						Double value = (((matrixDistance.getStructure()[matrixDistance.shp][i]) + (matrixDistance.getStructure()[matrixDistance.shp][j])) 
								- (matrixDistance.getStructure()[i][j]));						
						if(value != null) savingList.add(value);
					} else {
						savingList.add(NOTHING);
					}
				}				
			}
		}
		savings = new Matrix(savingList, matrixDistance.size - 1);
		return savings;
	}
//
//	
//	//TODO REMOVE THIS
//	public static void main(String[] args) {
//
//		new CalculateHelper().matrixDistance();
//
//	}
//	
//	
//	//TODO REMOVE THIS
//		public void matrixDistance() {
//			Point p0 = new Point(80, 80);
//			p0.setStoreHouse(true);
//			Point p1 = new Point(60, 130);
//			Point p2 = new Point(30, 60);
//			Point p3 = new Point(60, 20);
//			Point p4 = new Point(90, 50);
//			Point p5 = new Point(100, 120);
//			
//			List<Point> list = new ArrayList<Point>();
//			list.add(p0);
//			list.add(p1);
//			list.add(p2);
//			list.add(p3);
//			list.add(p4);
//			list.add(p5);
//
//			Matrix matrixDistanceResult = new CalculateHelper().getMatrixDistance(list);
//
//
//			Matrix matrixDistance = new Matrix();
//			matrixDistance = matrixDistanceResult;
//
//			Matrix matrixSaving = new Matrix();
//			matrixSaving = this.getMatrixEconomy(matrixDistance);
//
//
//			List<Arc> arcList =	matrixSaving.getArcListOrderBySaving();
//			arcList.size();
//
//			
//			
//		}

}
