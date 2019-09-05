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
public class CaixaBEAN {
    private int codigo;
    private String data;
    private String in;
    private String out;
    private float valorBruto;
    private float valorLiquido;
    private float despesas;
    private float troco;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public float getValorBruto() {
        return valorBruto;
    }

    public void setValorBruto(float valorBruto) {
        this.valorBruto = valorBruto;
    }

    public float getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(float valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public float getDespesas() {
        return despesas;
    }

    public void setDespesas(float despesas) {
        this.despesas = despesas;
    }

    public float getTroco() {
        return troco;
    }

    public void setTroco(float troco) {
        this.troco = troco;
    }
    
    
}
