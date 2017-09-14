package br.com.grafos.algoritmo.genetico.control;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import sun.org.mozilla.javascript.internal.json.JsonParser;
import br.com.grafos.algoritmo.genetico.helper.GenUtil;
import br.com.grafos.algoritmo.genetico.model.Alelo;
import br.com.grafos.algoritmo.genetico.model.Cromossomo;
import br.com.grafos.model.ConfiguracaoTela;


public class Genetico {

	{
		// TODO VALORES DEFAULT PARA O EXERCÍCIO
		this.taxaDeCruzamento = 85f;
		this.taxaDeMutacao = 10f;
		this.geracoes = 3000;
		this.quantidadeDeCromossomos = 40;
	}

	private Integer quantidadeDeCromossomos;
	private Float taxaDeCruzamento;
	private Float taxaDeMutacao;
	private Integer geracoes;
	private List<Cromossomo> cromossomos = new ArrayList<Cromossomo>();

	private List<Cromossomo> pais = new ArrayList<Cromossomo>();
	private List<Cromossomo> maes = new ArrayList<Cromossomo>();
	private List<Cromossomo> novaGeracao = new ArrayList<Cromossomo>();
	private Cromossomo melhorCromossomo;

	
	//========================================================>
	// configurações
	
	
	
	public Genetico() {
	}

	public Genetico(ConfiguracaoTela config) {
			this.geracoes = config.getIteracoes();
			this.taxaDeCruzamento = new Float(config.getTaxaDereproducao());
			this.taxaDeMutacao =  new Float(config.getTaxaMutacao());
			this.quantidadeDeCromossomos =  config.getPopulacaoInicial();
			//GERA A POPULAÇÃO INICIAL
			this.cromossomos = GenUtil.geraCromossomos(this.quantidadeDeCromossomos,config.getPontosIniciais());
	}
	
	public String gerar2() {		
			this.geraPaisPorEletismo();
			if((this.cromossomos.size() % 2 == 0)){
				for (int i = 0; i < (this.cromossomos.size()/2) ; i++) {
					this.getNovaGeracao2(pais.get(i), maes.get(i));
				}
			}else{
				for (int i = 0; i < ((this.cromossomos.size() - 1)/2) ; i++) {
					this.getNovaGeracao2(pais.get(i), maes.get(i));
				}
				
				Cromossomo crom = this.novaGeracao.get(0);
				this.novaGeracao.add(crom);
			}
			this.cromossomos = this.novaGeracao; 
			this.novaGeracao = new ArrayList<Cromossomo>();
			return melhorCromossomo.toJson();
	}
	
	private int getMiddle(){
		if(this.cromossomos.size() % 2 == 0){
			return cromossomos.size() / 2;
		}
		return 0;
	}
	
	
	private void getNovaGeracao2(Cromossomo cromossomoPai,
			Cromossomo cromossomoMae) {

		int pontoCorte1 = GenUtil.rgen.nextInt(cromossomoPai.size() - 1) + 1;
		int pontoCorte2;

		do {
			pontoCorte2 = GenUtil.rgen.nextInt(cromossomoMae.size() - 1) + 1;
		} while (pontoCorte1 == pontoCorte2);
		if (pontoCorte1 > pontoCorte2) {
			int temp = pontoCorte1;
			pontoCorte1 = pontoCorte2;
			pontoCorte2 = temp;
		}

		Cromossomo filho1 = new Cromossomo(cromossomoPai);
		Cromossomo filho2 = new Cromossomo(cromossomoMae);

		if (podeCruzar()) {
			for (int i = 0; i < filho1.size(); i++) {
				if (i < pontoCorte1 || i >= pontoCorte2) {
					filho1.set(i, cromossomoMae.get(i));
					filho2.set(i, cromossomoPai.get(i));
				}
			}
		}

		aplicaPMX(cromossomoMae, pontoCorte1, pontoCorte2, filho1);
		aplicaPMX(cromossomoPai, pontoCorte1, pontoCorte2, filho2);

		aplicaMutacao(filho1);
		aplicaMutacao(filho2);

		cromossomoPai = new Cromossomo(filho1);
		cromossomoMae = new Cromossomo(filho2);

		if(this.melhorCromossomo == null || cromossomoPai.getAptidao() < this.melhorCromossomo.getAptidao()){
			this.melhorCromossomo = cromossomoPai;
			System.out.println("melhor da geração -> "+this.melhorCromossomo);
		}

		if(this.melhorCromossomo == null || cromossomoMae.getAptidao() < this.melhorCromossomo.getAptidao()){
			this.melhorCromossomo = cromossomoMae;
			System.out.println("melhor da geração -> "+this.melhorCromossomo);
		}
		
		novaGeracao.add(cromossomoPai);
		novaGeracao.add(cromossomoMae);

		
		
	}
	
//	public void gerar() {
//
//		this.cromossomos = GenUtil.geraCromossomos(this.quantidadeDeCromossomos);
//
////		System.out.println("Cromossomos iniciais:");
////		System.out.println(cromossomos.toString());
////		System.out.println("------------------------------------------------------------------------");
//		for (int geracao = 0; geracao < this.geracoes; geracao++) {
//			this.geraPaisPorEletismo();
//			for (int i = 0; i < 5; i++) {
//				this.getNovaGeracao(pais.get(i), maes.get(i));
//			}
//			this.cromossomos = this.novaGeracao;
////			System.out.println("Geração "+ (geracao + 1));
////			System.out.println(cromossomos.toString());
//			this.novaGeracao = new ArrayList<Cromossomo>();
//		}
//		System.out.println("###################################################################");		
//		System.out.println("###################################################################");		
//		System.out.println("###################################################################");		
//		System.out.print("MELHOR CROMOSSOMO -> "+this.melhorCromossomo);		
//		
//		
//
//	}

	private void geraPaisPorEletismo() {
		Collections.sort(this.cromossomos);
		for (int i = 0; i < this.cromossomos.size(); i++) {
			this.pais.add(this.cromossomos.get(i));
		}
		this.maes = new ArrayList<Cromossomo>(this.pais);
		Collections.shuffle(this.maes);
	}

//	private void getNovaGeracao(Cromossomo cromossomoPai,
//			Cromossomo cromossomoMae) {
//
//		int pontoCorte1 = GenUtil.rgen.nextInt(cromossomoPai.size() - 1) + 1;
//		int pontoCorte2;
//
//		do {
//			pontoCorte2 = GenUtil.rgen.nextInt(cromossomoMae.size() - 1) + 1;
//		} while (pontoCorte1 == pontoCorte2);
//		if (pontoCorte1 > pontoCorte2) {
//			int temp = pontoCorte1;
//			pontoCorte1 = pontoCorte2;
//			pontoCorte2 = temp;
//		}
//
//		Cromossomo filho1 = new Cromossomo(cromossomoPai);
//		Cromossomo filho2 = new Cromossomo(cromossomoMae);
//
//		if (podeCruzar()) {
//			for (int i = 0; i < filho1.size(); i++) {
//				if (i < pontoCorte1 || i >= pontoCorte2) {
//					filho1.set(i, cromossomoMae.get(i));
//					filho2.set(i, cromossomoPai.get(i));
//				}
//			}
//		}
//
//		aplicaPMX(cromossomoMae, pontoCorte1, pontoCorte2, filho1);
//		aplicaPMX(cromossomoPai, pontoCorte1, pontoCorte2, filho2);
//
//		aplicaMutacao(filho1);
//		aplicaMutacao(filho2);
//
//		cromossomoPai = new Cromossomo(filho1);
//		cromossomoMae = new Cromossomo(filho2);
//
//		if(this.melhorCromossomo == null || cromossomoPai.getAptidao() < this.melhorCromossomo.getAptidao()){
//			this.melhorCromossomo = cromossomoPai;
//			System.out.println("melhor da geração -> "+this.melhorCromossomo);
//		}
//
//		if(this.melhorCromossomo == null || cromossomoMae.getAptidao() < this.melhorCromossomo.getAptidao()){
//			this.melhorCromossomo = cromossomoMae;
//			System.out.println("melhor da geração -> "+this.melhorCromossomo);
//		}
//		
//		novaGeracao.add(cromossomoPai);
//		novaGeracao.add(cromossomoMae);
//
//		
//		
//	}

	int taxa = GenUtil.rgen.nextInt(100);

	private boolean podeCruzar() {
		return (taxa <= this.taxaDeCruzamento);
	}

	private void aplicaMutacao(Cromossomo cromossomo) {
		int taxa = GenUtil.rgen.nextInt(100);
		if (taxa <= this.taxaDeMutacao) {
			int i;
			int j;
			do {
				i = GenUtil.rgen.nextInt(cromossomo.size());
				j = GenUtil.rgen.nextInt(cromossomo.size());
			} while (i != j);
			Alelo alelo1 = cromossomo.get(i);
			cromossomo.set(i, cromossomo.get(j));
			cromossomo.set(j, alelo1);
		}
	}

	private void aplicaPMX(Cromossomo cromossomoPai, int pontoCorte1,
			int pontoCorte2, Cromossomo cromossomofilho) {
		boolean inconsistencia = false;
		do {
			inconsistencia = false;
			for (int i = 0; i < cromossomofilho.size(); i++) {// varre os
																// trocados
				if (i < pontoCorte1 || i >= pontoCorte2) { // varre s— os
															// trocados
					for (int j = pontoCorte1; j < pontoCorte2; j++) { // varre o
																		// miolo
						if (i != j
								&& cromossomofilho.get(i).getId()
										.equals(cromossomofilho.get(j).getId())) {
							inconsistencia = true;
							cromossomofilho.set(i, cromossomoPai.get(j));
						}
					}
				}
			}
		} while (inconsistencia);
	}

	
	
	public Cromossomo getMelhorCromossomo() {
		return melhorCromossomo;
	}

	public void setMelhorCromossomo(Cromossomo melhorCromossomo) {
		this.melhorCromossomo = melhorCromossomo;
	}

}
