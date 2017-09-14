package br.com.grafos.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ConfiguracaoTela {
	
	private Integer iteracoes;
	private Integer taxaDereproducao;
	private Integer taxaMutacao;
	private Integer populacaoInicial;
	private Integer restricaoPontos;
	private Ponto deposito;
	private List<Ponto> pontosIniciais;
	private Integer algoritmo;
	
	public ConfiguracaoTela() {}
	
	public ConfiguracaoTela(String configuracaoTela) {
		JSONObject json;
		try {
			json = new JSONObject(new JSONTokener(configuracaoTela));			
			this.iteracoes = !json.getString("iteracoes").isEmpty() ? Integer.parseInt(json.getString("iteracoes")) : null;			
			this.taxaDereproducao = !json.getString("reproducao").isEmpty() ? Integer.parseInt(json.getString("reproducao")) : null;
			this.taxaMutacao = !json.getString("mutacao").isEmpty() ? Integer.parseInt(json.getString("mutacao")) : null;
			this.populacaoInicial = !json.getString("populacaoInicial").isEmpty() ? Integer.parseInt(json.getString("populacaoInicial")) : null;
			this.restricaoPontos = !json.getString("restricaoPontos").isEmpty() ? Integer.parseInt(json.getString("restricaoPontos")) : null;
			this.algoritmo = json.getInt("algoritmo");
			JSONArray jsonArray = (JSONArray) json.get("pontos");
			if (jsonArray != null) { 
					this.pontosIniciais = new ArrayList<Ponto>();
				   int len = jsonArray.length();
				   for (int i = 0 ; i < len ; i++){ 
					Ponto np = new Ponto(jsonArray.get(i).toString());
					if(np.isStoreHouse()) this.deposito = np;
				    pontosIniciais.add(np);
				   } 
			} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public Integer getIteracoes() {
		return iteracoes;
	}


	public void setIteracoes(Integer iteracoes) {
		this.iteracoes = iteracoes;
	}


	public Integer getTaxaDereproducao() {
		return taxaDereproducao;
	}


	public void setTaxaDereproducao(Integer taxaDereproducao) {
		this.taxaDereproducao = taxaDereproducao;
	}


	public Integer getTaxaMutacao() {
		return taxaMutacao;
	}


	public void setTaxaMutacao(Integer taxaMutacao) {
		this.taxaMutacao = taxaMutacao;
	}


	public List<Ponto> getPontosIniciais() {
		return pontosIniciais;
	}


	public void setPontosIniciais(List<Ponto> pontosIniciais) {
		this.pontosIniciais = pontosIniciais;
	}


	public Integer getPopulacaoInicial() {
		return populacaoInicial;
	}


	public void setPopulacaoInicial(Integer populacaoInicial) {
		this.populacaoInicial = populacaoInicial;
	}

	public Integer getRestricaoPontos() {
		return restricaoPontos;
	}

	public void setRestricaoPontos(Integer restricaoPontos) {
		this.restricaoPontos = restricaoPontos;
	}

	public Ponto getDeposito() {
		return deposito;
	}

	public void setDeposito(Ponto deposito) {
		this.deposito = deposito;
	}

	public Integer getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(Integer algoritmo) {
		this.algoritmo = algoritmo;
	}
	
	
	
}