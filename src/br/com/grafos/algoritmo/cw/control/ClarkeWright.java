package br.com.grafos.algoritmo.cw.control;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import br.com.grafos.algoritmo.cw.helper.CalculateHelper;
import br.com.grafos.algoritmo.cw.model.Arc;
import br.com.grafos.algoritmo.cw.model.Matrix;
import br.com.grafos.model.ConfiguracaoTela;
import br.com.grafos.model.Ponto;

public class ClarkeWright {

	private static final int NAO_CLASSIFICADO = -1;
	private ConfiguracaoTela config;
	private List<List<Integer>> classificados = new ArrayList<List<Integer>>();
	private List<Integer> desclassificados = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		Ponto p0 = new Ponto(1, 80, 80, true);
		Ponto p1 = new Ponto(2, 60, 130);
		Ponto p2 = new Ponto(3, 30, 60);
		Ponto p3 = new Ponto(4, 60, 20);
		Ponto p4 = new Ponto(5, 90, 50);
		Ponto p5 = new Ponto(6, 100, 120);
		List<Ponto> list = new ArrayList<Ponto>();
		list.add(p0);
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		ConfiguracaoTela config = new ConfiguracaoTela();
		config.setPontosIniciais(list);
		config.setRestricaoPontos(3);
		config.setDeposito(p0);
		ClarkeWright a = new ClarkeWright(config);
		System.out.println(a.getToJson());
	}

	/**
	 * Se exatamente um dos pontos I ou J já pertencem a algum roteiro aberto,
	 * verificar se ele é o primeiro ou o último ponto deste roteiro. Em caso
	 * afirmativo, acrescenta-se o arco i, j ao roteiro. Caso contrário
	 * descarta-se o par, passando para a próxima etapa;
	 * 
	 * @param arco
	 */

	/**
	 * 0 não estão em nehum roteiro (nesse caso deve-se criar um novo roteiro) 1
	 * mesmo roteiro (descarta o arco) 2 roteiro diferente (se for extremo faz
	 * fusão senao descarta)
	 * 
	 * 3 roteito dif mas não
	 * 
	 * @param arco
	 * @param config
	 */
	private void aplicaPasso4(Arc arco) {

		Classificacao classificacao = classificaArco(arco);

		if (classificacao.roteiroDeI == NAO_CLASSIFICADO
				&& classificacao.roteiroDeJ == NAO_CLASSIFICADO) {
			/**
			 * caso os nós i e j não estejam incluídos em nenhum roteiro já
			 * aberto, então deve-se criar um novo roteiro com esses nós;
			 */
			criaNovoRoteiro(arco);
		} else {

			/**
			 * No caso de ambos os nós i e j pertencerem a um mesmo roteiro,
			 * descarta-se o par, passando para a próxima etapa;
			 */
			if (classificacao.roteiroDeI != classificacao.roteiroDeJ) {
				/**
				 * Se exatamente um dos pontos I ou J já pertencem a algum
				 * roteiro aberto, verificar se ele é o primeiro ou o último
				 * ponto deste roteiro. Em caso afirmativo, acrescenta-se o arco
				 * i, j ao roteiro. Caso contrário descarta-se o par, passando
				 * para a próxima etapa;
				 * 
				 * @param arco
				 */
				if (classificacao.roteiroDeJ == NAO_CLASSIFICADO
						&& classificacao.iExtremo) {
					if (podeAumentarORoteiro(classificacao.roteiroDeI, arco)) {
						this.classificados.get(classificacao.roteiroDeI).add(
								(Integer) arco.getJ());
					}else if(!this.desclassificados.contains(arco.getJ())){
						this.desclassificados.add(arco.getJ());
					}
				} else if (classificacao.roteiroDeI == NAO_CLASSIFICADO
						&& classificacao.jExtremo) {
					if (podeAumentarORoteiro(classificacao.roteiroDeJ, arco)) {
						this.classificados.get(classificacao.roteiroDeJ).add(
								(Integer) arco.getI());
					}else if(!this.desclassificados.contains(arco.getI())){
						this.desclassificados.add(arco.getI());
					}
					/**
					 * No caso de ambos os nós i e j pertencerem a roteiros
					 * diferentes, verificar se ambos são extremos desses
					 * roteiros. Em caso afirmativo devemos fundir os roteiros.
					 * Roteiro. Caso contrário descarta-se o par, passando para
					 * a próxima etapa;
					 */
				} else if (classificacao.iExtremo && classificacao.jExtremo) {
					if (podeJuntarRoteiros(classificacao.roteiroDeI,
							classificacao.roteiroDeJ)) {
						juntasOsRoteiros(classificacao.roteiroDeI,
								classificacao.roteiroDeJ);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Método classifica em quais roteiros os pontos do arco estão e se são
	 * pontos extremos do roteiro
	 * 
	 * @param arco
	 * @return
	 */
	private Classificacao classificaArco(Arc arco) {
		Classificacao classificacaoDoArco = new Classificacao();
		for (int i = 0; i < this.classificados.size(); i++) {
			List<Integer> roteiro = this.classificados.get(i);
			if (roteiro != null) {
				for (int j = 0; j < roteiro.size(); j++) {
					if (arco.getI() == roteiro.get(j)
							|| arco.getI() == roteiro.get(j)) {
						classificacaoDoArco.roteiroDeI = i;
						if (j == 0 || j == (roteiro.size() - 1))
							classificacaoDoArco.iExtremo = true;
					}
					if (arco.getJ() == roteiro.get(j)
							|| arco.getJ() == roteiro.get(j)) {
						classificacaoDoArco.roteiroDeJ = i;
						if (j == 0 || j == (roteiro.size() - 1))
							classificacaoDoArco.jExtremo = true;
					}
				}
			}
		}
		return classificacaoDoArco;
	}

	private boolean podeJuntarRoteiros(int rota1, int rota2) {
		Integer tamanhoDaRota1 = this.classificados.get(rota1).size();
		Integer tamanhoDaRota2 = this.classificados.get(rota2).size();
		if (this.config.getRestricaoPontos() == null ||(tamanhoDaRota1 + tamanhoDaRota2) <= this.config
				.getRestricaoPontos()) {
			return true;
		}
		return false;
	}

	private boolean podeAumentarORoteiro(Integer indiceRoteiro, Arc arco) {
		if (this.config.getRestricaoPontos() == null || this.classificados.get(indiceRoteiro).size() < this.config
				.getRestricaoPontos()) {
			return true;
		}
		return false;
	}

	private void criaNovoRoteiro(Arc arco) {
		List<Integer> novolt = new ArrayList<Integer>();
		novolt.add((Integer) arco.getI());
		novolt.add((Integer) arco.getJ());
		this.classificados.add(novolt);
		this.desclassificados.remove(novolt);		
	}

	private void juntasOsRoteiros(int roteiroDeI, int roteiroDeJ) {
		List<Integer> roteiroI = this.classificados.get(roteiroDeI);
		List<Integer> roteiroJ = this.classificados.get(roteiroDeJ);

		this.classificados.remove(roteiroI);
		this.classificados.remove(roteiroJ);
		
		roteiroI.addAll(roteiroJ);
		List<Integer> juncao = new ArrayList<Integer>(roteiroI);
		this.classificados.add(juncao);
	}

	public String getRoteirosJson() {
		return getToJson();
	}

	class Classificacao {
		int roteiroDeI = NAO_CLASSIFICADO;
		int roteiroDeJ = NAO_CLASSIFICADO;
		boolean iExtremo = false;
		boolean jExtremo = false;
	}

	private String getToJson() {
		 JSONArray mJSONArray = new JSONArray(this.classificados);
		 return mJSONArray.toString();
	}
	

	public ClarkeWright(ConfiguracaoTela config) {

		this.config = config;

		CalculateHelper helper = new CalculateHelper();

		Matrix matrixDistance = helper.getMatrixDistance(config
				.getPontosIniciais());

		Matrix matrixSaving = helper.getMatrixEconomy(matrixDistance);

		List<Arc> arcList = matrixSaving.getArcListOrderBySaving();

		for (Arc arco : arcList) {
			aplicaPasso4(arco);
		}

		for(List<Integer> roteiro : this.classificados){
			roteiro.add(0, config.getDeposito().getId());
			roteiro.add(roteiro.size(),config.getDeposito().getId());
		}
		
		criaRotaParaDesclassificados();

	}

	private void criaRotaParaDesclassificados() {
		for(Integer desclassificado : this.desclassificados){
			List<Integer> nova = new ArrayList<Integer>();
			nova.add(this.config.getDeposito().getId());
			nova.add(desclassificado);
			nova.add(this.config.getDeposito().getId());
			this.classificados.add(nova);
		}
	}

	//
	//
	// // ============================================================> PASS44 4
	// <==========================================================
	// // ============================================================> PA 4 44
	// <==========================================================
	// // ============================================================> PASS44
	// 4444 <==========================================================
	// // ============================================================> PA
	// 4444444 <==========================================================
	// // ============================================================> PA 44
	// <==========================================================
	//
	// private List<List<Arc>> roteiros = new ArrayList<List<Arc>>();
	// private List<List<Integer>> roteirosSTR = new ArrayList<List<Integer>>();
	//
	//
	// private List<Integer> lt = new ArrayList<Integer>();
	//
	// /**
	// * Se exatamente um dos pontos I ou J já pertencem a algum roteiro aberto,
	// * verificar se ele é o primeiro ou o último ponto deste roteiro. Em caso
	// * afirmativo, acrescenta-se o arco i, j ao roteiro. Caso contrário
	// * descarta-se o par, passando para a próxima etapa;
	// *
	// * @param arco
	// */
	//
	// /**
	// * 0 não estão em nehum roteiro (nesse caso deve-se criar um novo roteiro)
	// 1
	// * mesmo roteiro (descarta o arco) 2 roteiro diferente (se for extremo faz
	// * fusão senao descarta)
	// *
	// * 3 roteito dif mas não
	// *
	// * @param arco
	// * @param config
	// */
	// private void aplicaPasso4(Arc arco) {
	// int roteiroDeI = -1;
	// int roteiroDeJ = -1;
	//
	// for (int i = 0; i < this.roteiros.size(); i++) {
	// List<Arc> roteiro = this.roteiros.get(i);
	// if (roteiro != null) {
	// for (int j = 0; j < roteiro.size(); j++) {
	// if ( arco.getI() == roteiro.get(j).getI() || arco.getI() ==
	// roteiro.get(j).getJ()) {
	// roteiroDeI = i;
	// }
	// if ( arco.getJ() == roteiro.get(j).getJ() || arco.getJ() ==
	// roteiro.get(j).getI()) {
	// roteiroDeJ = i;
	// }
	// }
	// }
	// }
	//
	// if (roteiroDeI == -1 && roteiroDeJ == -1) {
	// /**
	// * caso os nós i e j não estejam incluídos em nenhum roteiro já
	// * aberto, então deve-se criar um novo roteiro com esses nós;
	// */
	// criaNovoRoteiro(arco);
	// }
	//
	// /**
	// * No caso de ambos os nós i e j pertencerem a um mesmo roteiro,
	// * descarta-se o par, passando para a próxima etapa;
	// */
	// if (roteiroDeI != roteiroDeJ) {
	// if (roteiroDeI != roteiroDeJ) {
	// boolean i_EhExtremo = false;
	// boolean j_EhExtremo = false;
	// i_EhExtremo = ehPontoExtremo(arco, roteiroDeI);
	// j_EhExtremo = ehPontoExtremo(arco, roteiroDeJ);
	// /**
	// * Se exatamente um dos pontos I ou J já pertencem a algum
	// * roteiro aberto, verificar se ele é o primeiro ou o último
	// * ponto deste roteiro. Em caso afirmativo, acrescenta-se o arco
	// * i, j ao roteiro. Caso contrário descarta-se o par, passando
	// * para a próxima etapa;
	// *
	// * @param arco
	// */
	// if (roteiroDeJ == -1 && i_EhExtremo) {
	// if(podeAumentarORoteiro(roteiroDeI)){
	// this.roteiros.get(roteiroDeI).add(arco);
	// this.lt.add(arco.getJ());
	// }
	// } else if (roteiroDeI == -1 && j_EhExtremo) {
	// if(podeAumentarORoteiro(roteiroDeJ)){
	// this.roteiros.get(roteiroDeJ).add(arco);
	// this.lt.add(arco.getI());
	// }
	// /**
	// * No caso de ambos os nós i e j pertencerem a roteiros
	// * diferentes, verificar se ambos são extremos desses
	// * roteiros. Em caso afirmativo devemos fundir os roteiros.
	// * Roteiro. Caso contrário descarta-se o par, passando para
	// * a próxima etapa;
	// */
	// } else if (i_EhExtremo && j_EhExtremo) {
	// if(podeJuntarRoteiros(roteiroDeI, roteiroDeJ)){
	// juntasOsRoteiros(roteiroDeI, roteiroDeJ);
	// }
	// }
	// }
	// }
	//
	// }
	//
	//
	// private boolean podeJuntarRoteiros(int rota1, int rota2) {
	// Integer tamanhoDaRota1 = this.roteiros.get(rota1).size();
	// Integer tamanhoDaRota2 = this.roteiros.get(rota2).size();
	// if((tamanhoDaRota1 + tamanhoDaRota2) < this.config.getRestricaoPontos()){
	// return true;
	// }
	// return false;
	// }
	//
	// private boolean podeAumentarORoteiro(Integer indiceRoteiro) {
	// if(this.roteiros.get(indiceRoteiro).size() <
	// this.config.getRestricaoPontos()){
	// return true;
	// }
	// return false;
	// }
	//
	//
	// private boolean ehPontoExtremo(Arc arco, int roteiro) {
	// if(
	// roteiro != -1 && (
	// arco.getI() == this.roteiros.get(roteiro).get(0).getI() ||
	// arco.getI() ==
	// this.roteiros.get(roteiro).get(this.roteiros.get(roteiro).size()
	// -1).getI() ||
	// arco.getI() == this.roteiros.get(roteiro).get(0).getJ() ||
	// arco.getI() ==
	// this.roteiros.get(roteiro).get(this.roteiros.get(roteiro).size()
	// -1).getJ()||
	//
	// arco.getJ() == this.roteiros.get(roteiro).get(0).getI() ||
	// arco.getJ() ==
	// this.roteiros.get(roteiro).get(this.roteiros.get(roteiro).size()
	// -1).getI() ||
	// arco.getJ() == this.roteiros.get(roteiro).get(0).getJ() ||
	// arco.getJ() ==
	// this.roteiros.get(roteiro).get(this.roteiros.get(roteiro).size()
	// -1).getJ())
	// ){
	// return true;
	// } else{
	// return false;
	// }
	// }
	//
	// private void criaNovoRoteiro(Arc arco) {
	// List<Arc> novoRoteiro = new ArrayList<Arc>();
	// novoRoteiro.add(arco);
	// this.roteiros.add(novoRoteiro);
	//
	// //TODO teste
	// this.lt.add(arco.getI());
	// this.lt.add(arco.getJ());
	// }
	//
	// private void juntasOsRoteiros(int roteiroDeI, int roteiroDeJ) {
	// List<Arc> roteiroI = this.roteiros.remove(roteiroDeI);
	// List<Arc> roteiroJ = this.roteiros.remove(roteiroDeJ);
	// roteiroI.addAll(roteiroJ);
	// List<Arc> juncao = new ArrayList<>(roteiroI);
	// this.roteiros.add(juncao);
	//
	// //TODO TESTE
	//
	//
	// }
	//
	// public List<List<Integer>> retiraRepeticoesEadicionaDepositoNasRotas(){
	// Integer deposito = this.config.getDeposito().getId();
	// List<List<Integer>> roteirosStr = new ArrayList<List<Integer>>();
	// List<Integer> rotaStr = null;
	// for (int i = 0; i < this.roteiros.size(); i++) {
	// List<Arc> roteiro = this.roteiros.get(i);
	// rotaStr = new ArrayList<Integer>();
	// rotaStr.add(deposito);
	// for (int j = 0; j < roteiro.size(); j++) {
	// Integer it = roteiro.get(j).getI();
	// Integer jt = roteiro.get(j).getJ();
	// if(!rotaStr.contains(it)) rotaStr.add(it);
	// if(!rotaStr.contains(jt)) rotaStr.add(jt);
	// }
	// if(rotaStr.size() > 0){
	// rotaStr.add(deposito);
	// roteirosStr.add(rotaStr);
	// }
	// }
	// return roteirosStr;
	// }
	//
	// public String convertRoteiroToJson(){
	//
	// List<List<String>> resultadosStr = new ArrayList<List<String>>();
	//
	// for(int i = 0 ; i < this.roteirosSTR.size() ; i++){
	// List<Integer> listaDeRoteiros = this.roteirosSTR.get(i);
	// List<String> rotas = new ArrayList<String>();
	// for(Integer rota : listaDeRoteiros){
	// String rotaJson = new String(rota.toString());
	// rotas.add(rotaJson);
	// }
	// resultadosStr.add(rotas);
	// }
	//
	// JSONArray mJSONArray = new JSONArray(resultadosStr);
	// return mJSONArray.toString();
	// }
	//
	//
	// public String getRoteirosJson() {
	// return roteirosJson;
	// }
	//
	//
	// public void setRoteirosJson(String roteirosJson) {
	// this.roteirosJson = roteirosJson;
	// }
	//
	//

}
