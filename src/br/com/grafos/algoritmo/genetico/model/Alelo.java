package br.com.grafos.algoritmo.genetico.model;

import br.com.grafos.model.Ponto;

public class Alelo extends Ponto{

	public Alelo(){}

	public Alelo(Integer id){
		super(id);
	}

	public Alelo(Integer id, Integer x, Integer y){
		super(id,x,y);
	}
	
	@Override
	public String toString() {
		String jsonFormat = "{\"p\":"+getId()+"}";
		return jsonFormat;
	}
	
}
