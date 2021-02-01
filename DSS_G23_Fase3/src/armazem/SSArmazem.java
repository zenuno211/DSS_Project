package armazem;

import java.util.List;
import java.util.Map;

import Exceptions.*;

public interface SSArmazem {

	/**
	 * 
	 * @return Map<String,String> Localizações codQR -> codLocalizacao
	 */
	public Map<String, String> getLocalizacoes();

	/*
	 * Para implementação de utilizadores
	 * 
	 * Boolean validarLogin(String username, String password);
	 * 
	 * Boolean validarLogout(String username);
	 */

	/**
	 * 
	 * @param codRobot
	 * @param locPalete
	 * @param locFinal
	 */
	public Percurso calcularPercurso(String codRobot, String locPalete, String locFinal);

	/**
	 * Método que retorna a Localização de uma palete
	 * 
	 * @param codQR Código da palete
	 * @return Localização da palete
	 */
	public String determinarLocalizacao(String codQR);

	/**
	 * Método que retorna se um Robot existe ou não no sistema
	 * 
	 * @param codRobot Código do Robot a considerar
	 * @return Retorna True se o Robot existe no sistema e False caso contrário
	 */
	public Boolean existeRobot(String codRobot);

	/**
	 * Método que retorna se uma palete existe ou não sistema
	 * 
	 * @param codQR Código da palete a considerar
	 * @return Retorna True se a palete existe no sistema e False caso contrário
	 */
	public Boolean existePalete(String codQR);

	/**
	 * Método que retorna se um Produto existe ou não no sistema
	 * 
	 * @return True se existe, False se não
	 */
	public boolean existeProd(String codProd);

	/**
	 * Método que retorna uma lista de robots não ocupados
	 * 
	 * @return Lista de Robots
	 */
	public List<String> getListaRobotsDisponiveis();

	/**
	 * Método que retorna o Robot mais próximo de uma localização de uma palete
	 * 
	 * @param locPalete Localização da palete
	 * @param listRobot Lista de robots não ocupados
	 * @return Robot desejado
	 */
	public Robot getRobotMaisProximo(String locPalete, List<String> listRobot) throws NoRobotDisponivelException;

	/**
	 * Método que atualiza o estado de um Robot
	 * 
	 * @param codRobot Código do Robot a atualizar
	 * @param percurso Percurso do Robot
	 * @param codQR    Código de uma palete
	 */
	public void atualizarEstadoRobot(String codRbot, Percurso percurso, String codQR);

	/**
	 * Método que atualiza o estado de uma Prateleira
	 * 
	 * @param codPrateleira Código da prateleira a atualizar
	 * @param ocupada       Boolean para atualizar estado da prateleira
	 */
	public void atualizarEstadoPrateleira(String codPrateleira, boolean ocupada);

	/**
	 * Método que regista uma recolha de um Robot
	 * 
	 * @param codRobot Código do Robot
	 */
	public void registarRecolha(String codRobot);

	/**
	 * Método que regista uma entrega de um Robot
	 * 
	 * @param codRobot
	 */
	public void registarEntrega(String codRobot);

	/*
	 * Para implementação de requisições
	 * 
	 * void registarPaleteQueueEntregas(String codQR);
	 * 
	 * void registarPaleteQueueEspera(String codQR);
	 */

	/**
	 * Método que regista uma Palete num sistema
	 * 
	 * @param codQR    Código da Palete a registar
	 * @param codProd  Código do Produto dessa Palete
	 * @param nomeProd Nome do Produto dessa palete
	 */
	public void registarPaleteSistema(String codQR, String codProd, String nomeProd);

	/**
	 * Método que devolve uma prateleira vazia do sistema
	 * 
	 * @return Código da prateleira
	 */
	public String getPrateleiraVazia() throws NoPrateleirasVaziasException;

	/**
	 * Método que retorna uma Palete do sistema dado o seu código
	 * 
	 * @param codQR Código da Palete
	 * @return Palete do sistema
	 */
	public Palete getPaleteProdutos(String codQR);

	/*
	 * Para implementação de requisição
	 * 
	 * String verificarDisponibilidadeProduto(String codProd);
	 */
	

	/**
	 * Método que retorna uma lista com os códigos das paletes do sistema
	 * 
	 * @return Lista de paletes
	 */
	public List<Palete> getListPaletes();

	/**
	 * Método que retorna um Produto dado o seu código
	 * 
	 * @param codProd Código do produto a obter
	 * @return Produto desejado
	 */
	public Produto getProduto(String codProd);

	/**
	 * Método que retorna se um Robot está ocupado sem palete
	 * 
	 * @param codRobot Código do Robot a avaliar
	 * @return True se o Robot está ocupado sem palete, False caso contrário
	 */
	public boolean robotOcupadoSemPalete(String codRobot);

	/**
	 * Método que retorna se um Robot está ocupado com palete
	 * 
	 * @param codRobot Código do Robot a avaliar
	 * @return True se o Robot está ocupado com palete, False caso contrário
	 */
	public boolean robotOcupadoComPalete(String codRobot);

}