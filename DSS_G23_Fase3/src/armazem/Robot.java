package armazem;

import java.util.Objects;
import java.util.List;

public class Robot {

	private String codRobot; 
	private String codPalete;
	private String localizacao;
	private Percurso percurso;
	private Boolean ocupado;
	private Boolean recolhaFeita;

	/**
	 * Construtor de Robot dado um codigo e uma localizacao
	 */
	public Robot(String codigo, String localizacao){
		this.percurso = null;
		this.codPalete = null;
		this.localizacao = localizacao;
		this.codRobot = codigo;
		this.recolhaFeita = false;
		this.ocupado = false;
	}

	/**
	 * Construtor de Robot dado o um codigo, um codigo da palete, um percurso, uma localização,
	 * um boolean relativo ao robot estar ocupado ou nao e um boolean relativo ao robot já ter 
	 * feito a recolha 
	 */
	public Robot(String codigo, String codPalete, List<String> percurso, String localizacao, Boolean ocupado, Boolean recolhaFeita){
		this.percurso = new Percurso(percurso, 0);
		this.codPalete = codPalete;
		this.localizacao = localizacao;
		this.codRobot = codigo;
		this.ocupado = ocupado;
		this.recolhaFeita = recolhaFeita;
	}

	/**
	 * Obtem o codigo do robot
	 */
	public String getCodRobot() {
		return this.codRobot;
	}

	/**
	 * obtem o codigo da palete transportada pelo robot
	 */
	public String getCodPalete(){
		return this.codPalete;
	}

	/**
	 * Atribui um código de palete ao robot
	 * @param codPalete código da palete
	 */
	public void setCodPalete(String codPalete) {
		this.codPalete = codPalete;
	}

	/**
	 * obtem a localizacao do robot 
	 */
	public String getLocalizacao(){
		return this.localizacao;
	}

	/**
	 * Atribui um código de localização ao robot
	 * @param loc código da localização
	 */
	public void setLocalizacao(String loc){
		this.localizacao = loc;
	}


	/**
	 * Obtem o percurso do robot
	 */
	public Percurso getPercurso(){
		return this.percurso;
	}

	/**
	 * Atribui um percurso ao roboot
	 * @param percurso percurso atribuído ao robot
	 */
	public void setPercurso(Percurso percurso) {
		this.percurso = percurso;
	}

	/**
	 * Verifica se o robot está ocupado
	 */
	public Boolean getOcupado() {
		return this.ocupado;
	}

	/**
	 * Altera o estado de ocupação do robot
	 * @param oocupado estado atual do robot
	 */
	public void setOcupado(Boolean ocupado) {
		this.ocupado = ocupado;
	}

	/**
	 * Verifica se o robot já fez a recolha  
	 */
	public Boolean getRecolhaFeita(){
		return this.recolhaFeita;
	}

	/**
	 * Atualiza o estado do robot após recolher uma palete
	 */
	public void setRecolhaFeita(){
		this.recolhaFeita = true;
	}
	

	/**
	 * Obtem o codigo da localização do destino do robot
	 */
	public String getLocalizacaoFinal(){
		return this.percurso.getLocalizacaoFinal();
	}

	/**
	 * Atualiza o estado do robot após a entrega de uma palete
	 */
	public void finalizarEntrega() {
		this.codPalete = null;
		this.percurso = null;
		this.ocupado = false;
		this.recolhaFeita = false;
	}

	
	/* 
	Simulação do robot
	public void percorrePercurso(Percurso p){
		for(String s : p.getPercurso()){
			this.localizacao = s;
		}
	} */
	

	public boolean equals(Robot r){
		return (this.codRobot.equals(r.getCodRobot())) &&
				(this.codPalete.equals(r.getCodPalete())) &&
				(this.localizacao.equals(r.getLocalizacao())) &&
				(this.percurso.equals(r.getPercurso())) &&
				(this.ocupado == r.getOcupado()) &&
				(this.recolhaFeita == r.getRecolhaFeita());
	}

	@Override
	public int hashCode() {
		return Objects.hash(codRobot, codPalete, localizacao, percurso, ocupado);
	}

	
}