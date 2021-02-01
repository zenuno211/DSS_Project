package armazem;

import java.util.HashSet;
import java.util.Set;

public abstract class ZonaInOut {

	private String codZonaInOut;
	private Set<String> paletes; 

	/**
	 * Construtor de ZonaInOut
	 */
	public ZonaInOut(String codZonaInOut){
		this.codZonaInOut = codZonaInOut;
		this.paletes = new HashSet<String>();
	}

	/**
	 * Retorna o codigo da zona respetiva
	 */
	public String getCodZonaInOut() {
		return this.codZonaInOut;
	}

	/**
	 * Adiciona uma palete à zona respetiva
	 * @param palete a adicionar
	 */
	public void addPalete(String palete) {
		this.paletes.add(palete);
	}

	/**
	 * Remove uma palete da zona respetiva
	 * @param palete a remover
	 */
	public void removePalete(String palete) {
		this.paletes.remove(palete);
	}

} 