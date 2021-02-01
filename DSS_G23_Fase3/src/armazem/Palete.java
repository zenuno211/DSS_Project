package armazem;

public class Palete {

    private String localizacao;
    private float tamanho;
    private String codProduto;
    private String codQR;

    /**
     * Construtor de Palete 
     */
    public Palete(String codQR, String codProduto, float tamanho, String localizacao) {
        this.codQR = codQR;
        this.codProduto = codProduto;
        this.tamanho = tamanho;
        this.localizacao = localizacao;
    }

    /**
     * Obtem o codigo da palete 
     */
    public String getCodQR() {
        return codQR;
    }

    /**
     * Atribui um codigo à palete 
     */
    public void setCodQR(String codQR) {
        this.codQR = codQR;
    }

    /**
     * Obtem o codigo do Produto da Palete
     */
    public String getCodProduto() {
        return this.codProduto;
    }

    /**
     * Atribui um codigo ao produto da Palete
     */
    public void setCodProduto(String produto) {
        this.codProduto = produto;
    }   

    /**
     * Obtem o tamanho da Palete
     */
    public float getTamanho() {
        return tamanho;
    }

    /**
     * Atribui um tamanho à Palete 
     */
    public void setTamanho(float tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * Obtem o codigo da localização da Palete
     */
    public String getLocalizacao() {
        return localizacao;
    }

    /**
     * Atribui uma localização à palete 
     */
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public boolean equals(Palete palete) {
        return (this.codQR.equals(palete.getCodQR())) &&
                (this.codProduto.equals(palete.getCodProduto())) &&
                (this.tamanho == palete.getTamanho()) && 
                (this.localizacao.equals(palete.getLocalizacao()));
    }
}
