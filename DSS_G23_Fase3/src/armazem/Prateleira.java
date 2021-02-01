package armazem;

public class Prateleira {
	private String codPrateleira;
	private String codCorredor;
	private Boolean ocupada;

	public Prateleira(String codPrateleira, String codCorredor) {
		this.codPrateleira = codPrateleira;
		this.codCorredor = codCorredor;
		this.ocupada = false;
	}

	/**
	 * Método que devolve o código de uma prateleira
	 * @return Código da prateleira
	 */
	public String getCodPrateleira() {
		return this.codPrateleira;
	}

	/**
	 * Método que devolve o código do corredor de uma pratelira
	 * @return Código do corredor
	 */
	public String getCodCorredor() {
		return this.codCorredor;
	}

	/**
	 * Método que retorna se uma prateleira está ocupada ou não
	 * @return True se está ocupada, false caso contrário
	 */
	public Boolean getOcupada() {
		return this.ocupada;
	}

	/**
	 * Método que atualiza o estado de ocupação de uma prateleira
	 * @param ocupada Estado a atualizar
	 */
	public void setOcupada(Boolean ocupada) {
		this.ocupada = ocupada;
	}

}