package armazem;

import java.util.HashMap;
import java.util.Map;

public class Corredor {

	private Map<String, Integer> mapaPrateleiras;
	private String codCorredor;
	private int comprimento;


	/**
	 * Construtor do corredor
	 * @param codCorredor código do corredor
	 * @param comprimento comprimento do corredor
	 * @return Corredor
	 */

	public Corredor(String codCorredor, int comprimento){
		this.mapaPrateleiras = new HashMap<>();
		this.codCorredor = codCorredor;
		this.comprimento = comprimento;
	}

	/**
	 * Devolve o código do corredor
	 * @return código do corredor
	 */

	public String getCodCorredor(){
		return this.codCorredor;
	}

	/**
	 * Atribui um código ao corredor
	 * @param c código do corredor
	 */

	public void setCodCorredor(String c){
		this.codCorredor = c;
	}


	/**
	 * Devolve o comprimento do corredor
	 * @return comprimento do corredot
	 */
	public int getComprimento(){
		return this.comprimento;
	}

	/**
	 * Atribui um comprimento ao corredor 
	 */
	public void setComprimento(int c){
		this.comprimento = c;
	}

	/**
	 * Adiciona uma prateleira ao mapa de prateleiras
	 * @param prateleira prateleira a ser adicionada
	 * @param distancia distancia da prateleira ao meio do corredor
	 */
	public void addPrateleira(Prateleira prateleira, int distancia){
		String codPrateleira = prateleira.getCodPrateleira();
		this.mapaPrateleiras.put(codPrateleira, distancia);
	}

	/**
	 * Devolve a distancia da prateleira ao meio do corredor
	 * @param codPrateleira código da prateleira
	 * @return distancia da prateleira ao meio do corredor
	 */
	public int distanciaAPrateleira(String codPrateleira){
		return this.mapaPrateleiras.get(codPrateleira);
	}

}