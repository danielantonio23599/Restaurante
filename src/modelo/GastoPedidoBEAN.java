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
public class GastoPedidoBEAN {
    private GastoBEAN gasto;
    private PedidoBEAN pedido;
    private float quantidade;

    public GastoBEAN getGasto() {
        return gasto;
    }

    public void setGasto(GastoBEAN gasto) {
        this.gasto = gasto;
    }

    public PedidoBEAN getPedido() {
        return pedido;
    }

    public void setPedido(PedidoBEAN pedido) {
        this.pedido = pedido;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }
    
}
