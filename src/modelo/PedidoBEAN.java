/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Time;
import java.util.Date;


/**
 *
 * @author Daniel
 */
public class PedidoBEAN {
    private int codigo;
    private String time;
    private Float quantidade;
    private String observacao;
    private  String impresso;
    private int excluzao;
    private int produto;
    private int venda;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getImpresso() {
        return impresso;
    }

    public void setImpresso(String impresso) {
        this.impresso = impresso;
    }

    public int getExcluzao() {
        return excluzao;
    }

    public void setExcluzao(int excluzao) {
        this.excluzao = excluzao;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

  
    
    
}
