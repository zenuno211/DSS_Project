package armazem;

import java.util.HashMap;
import java.util.Map;

public class Zona {

	private Map<String, Corredor> corredores;

	/**
	 * Construtor de Zona 
	 */
	public Zona(){
		this.corredores = new HashMap<>();
	}

	/**
	 * Dado o codigo de um corredor retorna o Objeto corredor correspondente
	 */
	public Corredor getCorredor(String codCorredor){
		return this.corredores.get(codCorredor);
	}

	/**
	 * Adiciona um daddo corredor à Zona
	 */
	public void addCorredor(Corredor corredor){
		this.corredores.put(corredor.getCodCorredor(), corredor);
	}

}