package armazem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Produto {
    private String codProd;
    private String nomeProd;
    private Set<String> paletes;

    public Produto(String codProd, String nomeProd) {
        this.codProd = codProd;
        this.nomeProd = nomeProd;
        this.paletes = new HashSet<String>();
    }

    public Produto(String codProd, String nomeProd, Collection<String> paletes) {
        this.codProd = codProd;
        this.nomeProd = nomeProd;
        this.paletes = new HashSet<String>(paletes);
    }

    public String getCodProd() {
        return this.codProd;
    }

    public String getNomeProd() {
        return this.nomeProd;
    }

    /**
     * Função que devolve a lista de paletes que contêm um produto.
     * 
     * @return Lista resultante.
     */
    public Set<String> getPaletes() {
        return this.paletes; // Confirmar encapsulamento : value.clone();
    }


    /**
     * Função que adiciona uma palete a um produto.
     * 
     * @param palete Palete a adicionar
     */
    public void addPalete(Palete palete) { // Confirmar exceção
        String codQR = palete.getCodQR();
        if (this.paletes.contains(codQR)) {
            // throw new ExistePaleteException(palete.getCodQR());
        } else {
            this.paletes.add(codQR); // confirmar encapsulamento
        }
    }


    /**
     * Função que avalia se um produto existe nalguma palete.
     * 
     * @return True -> Existe | False -> Não existe.
     */
    public boolean contemPaletes() {
        return !this.paletes.isEmpty();
    }

    /**
     * Função que devolve a primeira palete onde um produto existe
     * 
     * @return Palete que contem esse produto. Null se não existe
     */
    public String getCodPalete() {
        if (this.contemPaletes())
            return this.paletes.iterator().next();
        else
            return null;
    }

    public boolean equals(Produto produto) {
        return (this.codProd.equals(produto.getCodProd())) && 
                this.nomeProd.equals(produto.getNomeProd()) && 
                paletes.equals(produto.getPaletes());
    }
}
