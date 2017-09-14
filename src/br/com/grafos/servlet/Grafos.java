package br.com.grafos.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.grafos.algoritmo.cw.control.ClarkeWright;
import br.com.grafos.algoritmo.genetico.control.Genetico;
import br.com.grafos.algoritmo.genetico.model.Cromossomo;
import br.com.grafos.model.ConfiguracaoTela;

@WebServlet("/grafos")
public class Grafos extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Genetico genetico;
	private static ClarkeWright clarkeWright;
	private static ConfiguracaoTela config;
	
	{
		this.config = new ConfiguracaoTela();
		this.config.setAlgoritmo(1);
	}
	
	public Grafos() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		switch (config.getAlgoritmo()) {
		case 1:{
			if (genetico != null) {
				Cromossomo cromo = genetico.getMelhorCromossomo();
				if (cromo != null) {
					String cromoJson = cromo.toJson();
					if (!cromoJson.isEmpty()) {
						response.setContentType("application/json");
						response.getWriter().write(cromoJson);
					}
				}
			}			
			break;
		}
		case 2:{
			if(clarkeWright != null){
				String resp = clarkeWright.getRoteirosJson();
				if(resp != null && !resp.isEmpty()){
					response.setContentType("application/json");
					response.getWriter().write(resp);
				}
			}
			break;
		}
		
		default:
			break;
		}
		

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String rs = request.getParameter("restart");

		if (rs != null && "1".equals(rs)) {
			genetico = null;
			clarkeWright = null;
		} else {

			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String configuracoes = "";
			if (br != null) {
				configuracoes = br.readLine();
			}

			config = new ConfiguracaoTela(configuracoes);

			switch (config.getAlgoritmo()) {
				case 1:{
					genetico = new Genetico(config);				
					for (int i = 0; i < config.getIteracoes(); i++) {
						genetico.gerar2();
					}
					break;
				}
				case 2:{
					clarkeWright = new ClarkeWright(config);
					break;
				}
				default:
					break;
			}

			System.out.println("=========================> FINALIZOU <=============================");

		}
	}

}
