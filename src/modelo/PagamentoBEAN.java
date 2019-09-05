/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Daniel
 */
public class PagamentoBEAN {
    private int codigo;
    private String nome;
    private Float acrescimo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(Float acrescimo) {
        this.acrescimo = acrescimo;
    }
    
}
