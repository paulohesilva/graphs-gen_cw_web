package br.com.grafos.algoritmo.cw.control;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import br.com.grafos.algoritmo.cw.model.Arc;
import br.com.grafos.model.ConfiguracaoTela;

public class CW {

    private static final int NAO_CLASSIFICADO = -1;
    private ConfiguracaoTela config;
    private String roteirosJson;
    private List<List<Integer>> roteirosSTR = new ArrayList<List<Integer>>();
    private List<List<Integer>> classificados = new ArrayList<List<Integer>>();
    

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

        if (classificacao.roteiroDeI == NAO_CLASSIFICADO && classificacao.roteiroDeJ == NAO_CLASSIFICADO) {
            /**
             * caso os nós i e j não estejam incluídos em nenhum roteiro já
             * aberto, então deve-se criar um novo roteiro com esses nós;
             */
            criaNovoRoteiro(arco);
        }else{

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
                if (classificacao.roteiroDeJ == NAO_CLASSIFICADO && classificacao.iExtremo) {
                    if (podeAumentarORoteiro(classificacao.roteiroDeI,arco)) {
                        this.classificados.get(classificacao.roteiroDeI).add((Integer) arco.getJ());
                    }
                } else if (classificacao.roteiroDeI == NAO_CLASSIFICADO && classificacao.jExtremo) {
                    if (podeAumentarORoteiro(classificacao.roteiroDeJ,arco)) {
                        this.classificados.get(classificacao.roteiroDeJ).add((Integer) arco.getI());
                    }
                    /**
                     * No caso de ambos os nós i e j pertencerem a roteiros
                     * diferentes, verificar se ambos são extremos desses
                     * roteiros. Em caso afirmativo devemos fundir os roteiros.
                     * Roteiro. Caso contrário descarta-se o par, passando para
                     * a próxima etapa;
                     */
                } else if (classificacao.iExtremo && classificacao.jExtremo) {
                    if (podeJuntarRoteiros(classificacao.roteiroDeI, classificacao.roteiroDeJ)) {
                        juntasOsRoteiros(classificacao.roteiroDeI, classificacao.roteiroDeJ);
                    }
                }
            }
        }
    }

    /**
     * 
     * Método classifica em quais roteiros os pontos do arco estão e se são pontos extremos do roteiro
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
                    if (arco.getI() == roteiro.get(j) || arco.getI() == roteiro.get(j)) {
                        classificacaoDoArco.roteiroDeI = i;
                        if(j == 0 || j == (roteiro.size() - 1)) classificacaoDoArco.iExtremo = true;
                    }
                    if (arco.getJ() == roteiro.get(j) || arco.getJ() == roteiro.get(j)) {
                        classificacaoDoArco.roteiroDeJ = i;
                        if(j == 0 || j == (roteiro.size() - 1)) classificacaoDoArco.jExtremo = true;
                    }
                }
            }
        }
        return classificacaoDoArco;
    }

    private boolean podeJuntarRoteiros(int rota1, int rota2) {
        Integer tamanhoDaRota1 = this.classificados.get(rota1).size();
        Integer tamanhoDaRota2 = this.classificados.get(rota2).size();
        if ((tamanhoDaRota1 + tamanhoDaRota2) <= this.config.getRestricaoPontos()) {
            return true;
        }
        return false;
    }

    private boolean podeAumentarORoteiro(Integer indiceRoteiro, Arc arco) {
        if (this.classificados.get(indiceRoteiro).size() < this.config.getRestricaoPontos()) {
            return true;
        }
        return false;
    }

    private void criaNovoRoteiro(Arc arco) {
        List<Integer> novolt = new ArrayList<Integer>();
        novolt.add((Integer) arco.getI());
        novolt.add((Integer) arco.getJ());
        this.classificados.add(novolt);
    }

    private void juntasOsRoteiros(int roteiroDeI, int roteiroDeJ) {
        List<Integer> roteiroI = this.classificados.remove(roteiroDeI);
        List<Integer> roteiroJ = this.classificados.remove(roteiroDeJ);
        roteiroI.addAll(roteiroJ);
        List<Integer> juncao = new ArrayList<Integer>(roteiroI);
        this.classificados.add(juncao);
    }

//    public List<List<Integer>> retiraRepeticoesEadicionaDepositoNasRotas() {
//        Integer deposito = this.config.getDeposito().getId();
//        List<List<Integer>> roteirosStr = new ArrayList<List<Integer>>();
//        List<Integer> rotaStr = null;
//        for (int i = 0; i < this.classificados.size(); i++) {
//            List<Arc> roteiro = this.classificados.get(i);
//            rotaStr = new ArrayList<Integer>();
//            rotaStr.add(deposito);
//            for (int j = 0; j < roteiro.size(); j++) {
//                Integer it = (Integer) roteiro.get(j).getI();
//                Integer jt = (Integer) roteiro.get(j).getJ();
//                if (!rotaStr.contains(it))
//                    rotaStr.add(it);
//                if (!rotaStr.contains(jt))
//                    rotaStr.add(jt);
//            }
//            if (rotaStr.size() > 0) {
//                rotaStr.add(deposito);
//                roteirosStr.add(rotaStr);
//            }
//        }
//        return roteirosStr;
//    }

    public String convertRoteiroToJson() {

        List<List<String>> resultadosStr = new ArrayList<List<String>>();

        for (int i = 0; i < this.roteirosSTR.size(); i++) {
            List<Integer> listaDeRoteiros = this.roteirosSTR.get(i);
            List<String> rotas = new ArrayList<String>();
            for (Integer rota : listaDeRoteiros) {
                String rotaJson = new String(rota.toString());
                rotas.add(rotaJson);
            }
            resultadosStr.add(rotas);
        }

        JSONArray mJSONArray = new JSONArray(resultadosStr);
        return mJSONArray.toString();
    }

    public String getRoteirosJson() {
        return roteirosJson;
    }

    public void setRoteirosJson(String roteirosJson) {
        this.roteirosJson = roteirosJson;
    }

    class Classificacao{
        int roteiroDeI = NAO_CLASSIFICADO;
        int roteiroDeJ = NAO_CLASSIFICADO;
        boolean iExtremo = false;
        boolean jExtremo = false;
    }
    
    
}