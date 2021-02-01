package armazem;

import Exceptions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import data.*;

public class GestorArmazem implements SSArmazem {

	private ZonaDeEntrega zonaEntrega;
	private ZonaDeRececao zonaRececao;

	private Zona zona; // Passar para MapaCorredores

	/* Para implementação de utilizadores */
	// private Map<String, Encarregado> encarregados;
	// private Map<String, Gestor> gestores;
	// private Map<String, Utilizador> utilizadores;

	private Map<String, Robot> robots;
	private Map<String, Palete> paletes;
	private Map<String, Prateleira> prateleiras;

	private Map<String, Produto> produtos;
	private MapaCorredores mapaCorredores;

	/* Para implementação de Requisições */
	// private List<String> queueEntregas;
	// private List<String> queueEspera;


	public GestorArmazem() {
		this.zonaEntrega = new ZonaDeEntrega();
		this.zonaRececao = new ZonaDeRececao();
		this.robots = RobotDAO.getInstance();
		this.paletes = PaleteDAO.getInstance();
		this.produtos = ProdutoDAO.getInstance();
		this.mapaCorredores = new MapaCorredores();

		initMapa();

		if (this.robots.size() == 0) {
			Robot r1 = new Robot("R01", "ZR01");
			Robot r2 = new Robot("R02", "P04");
			this.robots.put(r1.getCodRobot(), r1);
			this.robots.put(r2.getCodRobot(), r2);
		}

		setEstadoPrateleiras();
	}


	/* 
	Carrega o estado das prateleiras ao iniciar o programa a partir da base de dados de prateleiras 
	*/

	private void setEstadoPrateleiras() {
		this.paletes.values().stream().map(p -> p.getLocalizacao()).filter(l -> l.startsWith("P")).forEach(l -> {
			Prateleira p = this.prateleiras.get(l);
			p.setOcupada(true);
			this.prateleiras.put(p.getCodPrateleira(), p);
		});
	}


	public Map<String, String> getLocalizacoes() {
		Collection<Palete> paletes = this.paletes.values();
		Map<String, String> mapPaleteLocalizacao = new HashMap<>();

		for (Palete pal : paletes) {
			mapPaleteLocalizacao.put(pal.getCodQR(), pal.getLocalizacao());
		}

		return mapPaleteLocalizacao;	
	}

	/*
	 * Para implementação de utilizadores
	 *
	*/
	/*
	public Boolean validarLogin(String username, String password){
		Utilizador u = this.utilizadores.get(username);
		if (u instanceof Gestor){
			u.validarLogin(password);
			return true;
		}
		return false;
	}
	  
	public Boolean validarLogout(String username){
		Utilizador u = this.utilizadores.get(username);
		if (u instanceof Gestor){
			u.validarLogout();
			return true;
		}
		return false;
	}
	*/

	public Percurso calcularPercurso(String codRobot, String locPalete, String locFinal) {
		Robot r = this.robots.get(codRobot);
		Percurso percurso = calcularCaminhoMaisCurto(r.getLocalizacao(), locPalete, locFinal);
		return percurso;
	}

	 
	private int distanciaEntrePrateleiras(String codPrateleiraInicial, String codPrateleiraFinal) {
		Prateleira p1 = this.prateleiras.get(codPrateleiraInicial);
		Prateleira p2 = this.prateleiras.get(codPrateleiraFinal);
		String cInicial = p1.getCodCorredor();
		String cFinal = p2.getCodCorredor();

		Corredor corredorInicial = this.zona.getCorredor(cInicial);
		Corredor corredorFinal = this.zona.getCorredor(cFinal);
		List<String> percurso = new ArrayList<String>();
		int distanciaEntreCorredores = this.mapaCorredores.distanciaEntreCorredores(percurso, cInicial, cFinal);
		int distanciaPrateleiraInicial = corredorInicial.distanciaAPrateleira(codPrateleiraInicial);
		int distanciaPrateleiraFinal = corredorFinal.distanciaAPrateleira(codPrateleiraFinal);

		return distanciaPrateleiraInicial + distanciaPrateleiraFinal + distanciaEntreCorredores;
	}



	/* 
		Calcula e devolve o percurso/caminho mais curto de um robot até uma palete até a localização final 
	*/
	private Percurso calcularCaminhoMaisCurto(String locRobot, String locPalete, String locFinal) {
		String corredorInicial = null;
		String corredorFinal = null;

		if (locRobot.charAt(0) == 'P') {
			locRobot = this.prateleiras.get(locRobot).getCodCorredor();
		}

		if (locPalete.startsWith("ZR")) {
			corredorInicial = locPalete;
		} else if (locPalete.charAt(0) == 'P') { 
			corredorInicial = this.prateleiras.get(locPalete).getCodCorredor(); 
		}

		if (locFinal.startsWith("ZE")) {
			corredorFinal = locFinal;
		} else if (locFinal.charAt(0) == 'P') { 
			corredorFinal = this.prateleiras.get(locFinal).getCodCorredor(); 
		}

		List<String> caminhoFinal = new ArrayList<>();

		if (locRobot.equals(corredorInicial)) {
			caminhoFinal.add(locRobot);
		} else {
			caminhoFinal = mapaCorredores.getCaminhoEntre(new ArrayList<String>(), locRobot, corredorInicial);
		}

		List<String> caminhoPaleteFinal = mapaCorredores.getCaminhoEntre(new ArrayList<String>(), corredorInicial,
				corredorFinal);

		caminhoFinal.remove(caminhoFinal.size() - 1);
		caminhoFinal.addAll(caminhoPaleteFinal);
		int distancia = mapaCorredores.distanciaPercurso(caminhoFinal);

		if (locPalete.charAt(0) == 'P') {
			caminhoFinal.add(caminhoFinal.size() - caminhoPaleteFinal.size(), locPalete);
		}

		if (locFinal.charAt(0) == 'P') {
			caminhoFinal.add(locFinal);
		}

		return new Percurso(caminhoFinal, distancia);
	}

	public String determinarLocalizacao(String codQR) {
		return this.paletes.get(codQR).getLocalizacao();
	}

	public Boolean existeRobot(String codRobot) {
		return this.robots.containsKey(codRobot);
	}

	public Boolean existePalete(String codQR) {
		return this.paletes.containsKey(codQR);
	}

	public boolean existeProd(String codProd) {
		return this.produtos.containsKey(codProd);
	}

	public List<String> getListaRobotsDisponiveis() {
		Collection<Robot> robot = this.robots.values();
		return robot.stream()
				    .filter(x -> !x.getOcupado())
				    .map(Robot::getCodRobot)
				    .collect(Collectors.toList());
	}

	public Robot getRobotMaisProximo(String locPalete, List<String> listRobot) throws NoRobotDisponivelException {
		if (listRobot.size() == 0)
			throw new NoRobotDisponivelException();
		float min = Float.MAX_VALUE;
		Robot robotMaisProximo = null;
		for (String r : listRobot) {
			Robot robot = this.robots.get(r);
			float distancia = getDistanciaRobot(robot.getLocalizacao(), locPalete);
			if (distancia < min) {
				min = distancia;
				robotMaisProximo = robot;
			}
		}
		return robotMaisProximo;
	}

	public void atualizarEstadoRobot(String codRobot, Percurso percurso, String codQR) {
		Robot robot = this.robots.get(codRobot);
		robot.setPercurso(percurso);
		robot.setOcupado(true);
		robot.setCodPalete(codQR);
		this.robots.put(codRobot, robot);
	}

	public void atualizarEstadoPrateleira(String codPrateleira, boolean ocupada) {
		Prateleira prateleira = prateleiras.get(codPrateleira);
		prateleira.setOcupada(ocupada);
		prateleiras.put(codPrateleira, prateleira);
	};

	public void registarRecolha(String codRobot) {

		Robot robot = this.robots.get(codRobot);
		Palete palete = this.paletes.get(robot.getCodPalete());
		String locPalete = palete.getLocalizacao();	

		robot.setRecolhaFeita();

		if (locPalete.startsWith("ZR")) {
			this.zonaRececao.removePalete(palete.getCodQR());
		} else if (locPalete.startsWith("P")) {	
			atualizarEstadoPrateleira(locPalete, false);
		}

		palete.setLocalizacao(codRobot);
		this.robots.put(codRobot, robot);
		this.paletes.put(palete.getCodQR(), palete);
	}

	public void registarEntrega(String codRobot) {

		Robot robot = this.robots.get(codRobot);
		Palete palete = this.paletes.get(robot.getCodPalete());
		String locFinal = robot.getLocalizacaoFinal();

		palete.setLocalizacao(locFinal);
		robot.setLocalizacao(locFinal);
		robot.finalizarEntrega();

		this.paletes.put(palete.getCodQR(), palete);
		this.robots.put(codRobot, robot);

		//localização final é zona de entrega
		if (locFinal.startsWith("ZE")) {
			this.zonaEntrega.addPalete(palete.getCodQR());
		} else if (locFinal.startsWith("P")) { //localização final é prateleira
			atualizarEstadoPrateleira(locFinal, true);
		}
	}

	/*
	 * Para implementação de requisições
	 * 
	 * public void registarPaleteQueueEntregas(String codQR){
	 * this.queueEntregas.add(codQR); }
	 * 
	 * public void registarPaleteQueueEspera(String codQR){
	 * this.queueEspera.add(codQR); }
	 */

	public void registarPaleteSistema(String codQR, String codProd, String nomeProd) {
		Produto produto = null;

		if (!this.produtos.containsKey(codProd)) {
			produto = new Produto(codProd, nomeProd);
		} else {
			produto = this.produtos.get(codProd);
		}

		Palete palete = new Palete(codQR, codProd, 1, this.zonaRececao.getCodZonaInOut());
		this.paletes.put(codQR, palete);
		produto.addPalete(palete);
		this.produtos.put(codProd, produto);
		this.zonaRececao.addPalete(codQR);
	}

	public String getPrateleiraVazia() throws NoPrateleirasVaziasException {
		List<Prateleira> prateleirasVazias = this.prateleiras.values().stream()
																	  .filter(p -> !p.getOcupada())
																	  .collect(Collectors.toList());
		if (prateleirasVazias.size() == 0) {
			throw new NoPrateleirasVaziasException();
		} else
			return prateleirasVazias.get(0).getCodPrateleira();
	}

	public Palete getPaleteProdutos(String codQR) {
		return this.paletes.get(codQR);
	}

	/*
	 * Para implementação de requisição
	 * 
	 */
	/* public String verificarDisponibilidadeProduto(String codProd){ 
	 	String codQR = null; 
	 	if(produtos.containsKey(codProd)){ 
			Produto prod = produtos.get(codProd); 
			if(prod.contemPaletes()) 
				codQR = prod.getCodPalete();
		} 
		return codQR; 
	} */
	 

	/**
	 * Método que calcula a distancia entre um Robot e uma Palete
	 * 
	 * @param locRobot Localização do Robot  @param locPalete Localização da Palete
	 * @return Distancia entre a Palete e o Robot
	 */
	private int getDistanciaRobot(String locRobot, String locPalete) {
		int distancia = 0;
		String codCorredorRobot = "";
		String codCorredorPalete = "";
		if (locRobot == locPalete) {
			return 0;
		}

		if (locRobot.charAt(0) == 'P' && locPalete.charAt(0) == 'P') {
			return distanciaEntrePrateleiras(locRobot, locPalete);
		}

		if (locRobot.charAt(0) == 'Z') {
			codCorredorRobot = locRobot;
		} else if (locRobot.charAt(0) == 'P') {
			codCorredorRobot = this.prateleiras.get(locRobot).getCodCorredor();
			Corredor corredorRobot = this.zona.getCorredor(codCorredorRobot);
			distancia += corredorRobot.distanciaAPrateleira(locRobot);
		}

		if (locPalete.charAt(0) == 'Z') {
			codCorredorPalete = locPalete;
		} else if (locPalete.charAt(0) == 'P') {
			codCorredorPalete = this.prateleiras.get(locPalete).getCodCorredor();
			Corredor corredorPalete = this.zona.getCorredor(codCorredorPalete);
			distancia += corredorPalete.distanciaAPrateleira(locPalete);
		}

		if (codCorredorRobot != codCorredorPalete) {
			distancia += mapaCorredores.distanciaEntreCorredores(new ArrayList<String>(), codCorredorRobot,
					codCorredorPalete);
		}
		return distancia;
	}

	public List<Palete> getListPaletes() {
		List<Palete> paletes = new ArrayList<>(this.paletes.values());
		return paletes;
	}


	public Produto getProduto(String codProd) {
		return this.produtos.get(codProd);
	}

	public boolean robotOcupadoSemPalete(String codRobot) {
		Robot r = this.robots.get(codRobot);
		return (r.getOcupado() && !r.getRecolhaFeita());
	}

	public boolean robotOcupadoComPalete(String codRobot) {
		Robot r = this.robots.get(codRobot);
		return (r.getOcupado() && r.getRecolhaFeita());
	}
	

	/*  */
	
	private void initMapa() {
		this.prateleiras = new HashMap<String, Prateleira>();

		Corredor c01 = new Corredor("C01", 20);
		for (int j = 1; j <= 5; j++) {
			Prateleira toAdd = new Prateleira("P0" + j, "C01");
			prateleiras.put("P0" + j, toAdd);
			c01.addPrateleira(toAdd, Math.abs(10 - 5 * (j - 1)));
		}

		Corredor c02 = new Corredor("C02", 20);
		for (int j = 1; j <= 5; j++) {
			Prateleira toAdd = new Prateleira("P0" + (j + 5), "C02");
			prateleiras.put("P0" + (j + 5), toAdd);
			c02.addPrateleira(toAdd, Math.abs(10 - 5 * (j - 1)));
		}

		this.zona = new Zona();
		this.zona.addCorredor(c01);
		this.zona.addCorredor(c02);
	}

}