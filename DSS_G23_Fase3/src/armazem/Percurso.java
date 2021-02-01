package armazem;

import java.util.ArrayList;
import java.util.List;

public class Percurso {

	private List<String> percurso;
	private int comprimento;

	/**
	 * Construtor de Percurso dado um comprimento
	 */
	public Percurso(int comp) {
		this.percurso = new ArrayList<String>();
		this.comprimento = comp;
	}

	/**
	 * Construtor de Percurso dado um caminho e um comprimeto
	 */
	public Percurso(List<String> caminho, int comp) {
		this.percurso = caminho;
		this.comprimento = comp;
	}

	/**
	 * Obtem a lista de codigos de localizações de um Percurso 
	 */
	public List<String> getPercurso() {
		List<String> res = new ArrayList<String>(this.percurso);
		return res;
	}

	/**
	 * Método que retorna o comprimento de um percurso
	 * @return Comprimento do percurso
	 */
	public int getComprimento() {
		return this.comprimento;
	}
	
	/**
	 * Método que retorna a localização final de um percurso
	 * @return Código da localização final
	 */
	public String getLocalizacaoFinal() {
		return this.percurso.get(this.percurso.size() - 1);
	}
	
	/**
	 * Método que adiciona uma localização a um percurso
	 * @param localizacao Código da localizacao a adicionar
	 */
	public void addLoc(String localizacao) {
		this.percurso.add(localizacao);
	}

	/**
	 * Método que soma uma distancia ao percurso
	 * @param distancia Distancia a adicionar
	 */
	public void addDistancia(int distancia) {
		this.comprimento = this.comprimento + distancia;
	}

}