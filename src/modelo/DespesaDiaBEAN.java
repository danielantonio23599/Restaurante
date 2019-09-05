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
public class DespesaDiaBEAN {
    private DespesaBEAN despesa;
    private CaixaBEAN caixa;
    private float preco;

    public DespesaBEAN getDespesa() {
        return despesa;
    }

    public void setDespesa(DespesaBEAN despesa) {
        this.despesa = despesa;
    }

    public CaixaBEAN getCaixa() {
        return caixa;
    }

    public void setCaixa(CaixaBEAN caixa) {
        this.caixa = caixa;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }
    
    
}
