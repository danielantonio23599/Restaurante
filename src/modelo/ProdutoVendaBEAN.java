/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class ProdutoVendaBEAN {

    private ProdutoBEAN produto;
    private VendaBEAN venda;
    private String hora;
    private Date data;
    private String descricao;
    private float quantidade;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutoBEAN getProduto() {
        return produto;
    }

    public void setProduto(ProdutoBEAN produto) {
        this.produto = produto;
    }

    public VendaBEAN getVenda() {
        return venda;
    }

    public void setVenda(VendaBEAN venda) {
        this.venda = venda;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

}
