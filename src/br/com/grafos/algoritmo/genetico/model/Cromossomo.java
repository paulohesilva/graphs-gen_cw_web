package br.com.grafos.algoritmo.genetico.model;


import java.util.ArrayList;
import java.util.List;

import br.com.grafos.algoritmo.genetico.helper.GenUtil;
import br.com.grafos.model.Ponto;

public class Cromossomo extends ArrayList<Alelo> implements
		Comparable<Cromossomo> {

	private static final long serialVersionUID = 1L;
	private Integer aptidao;

	public Cromossomo(List<Ponto> pontos) {
		for(Ponto ponto : pontos){
			Alelo alelo = new Alelo(ponto.getId(), ponto.getX(), ponto.getY());
			this.add(alelo);
		}
		this.calculaAptidao();
	}
	
	public Cromossomo(Cromossomo cromossomo) {
		this.addAll(cromossomo);
		this.calculaAptidao();
	}

	public Cromossomo(int... alelosValuesArgs) {
		for (int aleloValue : alelosValuesArgs) {
			this.add(new Alelo(aleloValue));
		}
		this.calculaAptidao();
	}

	public Integer getAptidao() {
		return aptidao;
	}

	public void setAptidao(Integer aptidao) {
		this.aptidao = aptidao;
	}

	private void calculaAptidao() {
		Integer distanciaTotal = 0;
		for (int i = 0; i < this.size() - 1; i++) {
			Integer distancia = GenUtil.getDistanciaPreDefinida(this.get(i), this.get(i + 1));
			distanciaTotal = distanciaTotal + distancia;
		}
		distanciaTotal = distanciaTotal + GenUtil.getDistanciaPreDefinida(this.get(this.size() - 1), this.get(0));
		this.aptidao = distanciaTotal;
	}

	
	
	@Override
	public int compareTo(Cromossomo o) {
		if (aptidao > o.aptidao)
			return 1;
		else if (aptidao.equals(o.aptidao))
			return 0;
		else
			return -1;
	}
	
	public String toJson(){
		StringBuilder alelos = new StringBuilder("[");
		for(int i = 0 ; i < this.size() ; i++){
			alelos.append(this.get(i));
			if(i != size() - 1)
				alelos.append(",");
		}
		alelos.append("]");
		return alelos.toString();
	}
	
	public static void main(String[] args) {
		Cromossomo c = new Cromossomo(1,2,3,4,5);
		System.out.println(c.toJson());
	}
	
	@Override
	public String toString() {
		
		StringBuilder alelos = new StringBuilder("[");
		for(Alelo alelo : this){
			alelos.append(alelo.getId()+"  ");
		}
		alelos.append("] -> "+ this.aptidao);
		
		return alelos.toString();
	}
	
}
