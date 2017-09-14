package br.com.grafos.algoritmo.genetico.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.grafos.algoritmo.genetico.model.Alelo;
import br.com.grafos.algoritmo.genetico.model.Cromossomo;
import br.com.grafos.model.Ponto;


public class GenUtil {

	public static Random rgen = new Random();
	
	public static List<Cromossomo> geraCromossomos(int quantidadeDeCromossomos,List<Ponto> ponstosIniciais){
		List<Cromossomo> cromossomos = new ArrayList<>();
		
		for (int i = 0; i < quantidadeDeCromossomos; i++) {			
			List<Ponto> tempArray = randomizeList(ponstosIniciais);			
			cromossomos.add(new Cromossomo(tempArray));			
		}
		
		return cromossomos;
	}
	
	private static List<Ponto> randomizeList(List<Ponto> array){
		for (int i = 0; i < array.size() - 1; i++) {
		    int randomPosition = rgen.nextInt((array.size() - 1));
		    Ponto temp = array.get(i);
		    array.set(i, array.get(randomPosition));
		    array.set(randomPosition, temp);
		}
		return array;
	}
	
	public static List<Cromossomo> geraCromossomos(int quantidadeDeCromossomos){
		List<Cromossomo> cromossomos = new ArrayList<>();
		for (int i = 0; i < quantidadeDeCromossomos; i++) {			
			int[] tempArray= new int[]{1,2,3,4,5};
			randomizeArray(tempArray);
			cromossomos.add(new Cromossomo(tempArray));
		}
		return cromossomos;
	}
	
	private static int[] randomizeArray(int[] array){
		for (int i=0; i<array.length; i++) {
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPosition];
		    array[randomPosition] = temp;
		}
		return array;
	}
	
	
	/**
	 * TODO métodos específicos para o exercício. 
	 */
	public static Integer getDistanciaPreDefinida(Ponto a, Ponto b) {
//		switch (a.getId()) {
//		case 1:
//			return getDistanciaDe1(b);
//		case 2:
//			return getDistanciaDe2(b);
//		case 3:
//			return getDistanciaDe3(b);
//		case 4:
//			return getDistanciaDe4(b);
//		case 5:
//			return getDistanciaDe5(b);
//		default :
//			throw new IllegalStateException();
//		}
		
		return  distance(a, b);
		
	}

	public static Integer distance(Ponto p, Ponto q) {
		double dx = p.getX() - q.getX();
		double dy = p.getY() - q.getY();
		Double dist = Math.sqrt(dx * dx + dy * dy);													
		return dist.intValue();
	}
	
	private static Integer getDistanciaDe1(Alelo b) {
		switch (b.getId()) {
			case 2:{
				return 5;
			}
			case 3:{
				return 3;
			}
			case 4:{
				return 6;
			}
			case 5:{
				return 5;
			}
			default :{
				throw new IllegalStateException();
			}
		}
	}
	
	private static Integer getDistanciaDe2(Alelo b) {
		switch (b.getId()) {
			case 1:{
				return 5;
			}
			case 5:{
				return 4;
			}
			case 4:{
				return 10;
			}
			case 3:{
				return 7;
			}
			default :{
				throw new IllegalStateException();
			}
		}
	}
	
	private static Integer getDistanciaDe3(Alelo b) {
		switch (b.getId()) {
			case 1:{
				return 3;
			}
			case 2:{
				return 7;
			}
			case 4:{
				return 5;
			}
			case 5:{
				return 8;
			}
			default :{
				throw new IllegalStateException();
			}
		}
	}
	
	private static Integer getDistanciaDe4(Alelo b) {
		switch (b.getId()) {
			case 1:{
				return 6;
			}
			case 2:{
				return 10;
			}
			case 3:{
				return 5;
			}
			case 5:{
				return 4;
			}
			default :{
				throw new IllegalStateException();
			}
		}
	}
	
	private static Integer getDistanciaDe5(Alelo b) {
		switch (b.getId()) {
			case 1:{
				return 5;
			}
			case 2:{
				return 4;
			}
			case 4:{
				return 4;
			}
			case 3:{
				return 8;
			}
			default :{
				throw new IllegalStateException();
			}
		}
	}

}
