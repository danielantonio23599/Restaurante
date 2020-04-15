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
public class Caixa {

    private int caixa;
    private float faturamento;
    private float saldo;
    private float despesas;
    private float sangria;

    public int getCaixa() {
        return caixa;
    }

    public void setCaixa(int caixa) {
        this.caixa = caixa;
    }
    
    public float getFaturamento() {
        return faturamento;
    }

    public void setFaturamento(float faturamento) {
        this.faturamento = faturamento;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public float getDespesas() {
        return despesas;
    }

    public void setDespesas(float despesas) {
        this.despesas = despesas;
    }

    public float getSangria() {
        return sangria;
    }

    public void setSangria(float sangria) {
        this.sangria = sangria;
    }

}
