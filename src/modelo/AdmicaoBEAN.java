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
public class AdmicaoBEAN {

    private int funcionario;
    private int empresa;
    private int cargo;
    private String admicao;
    private String saida;
    private int uniforme;
    private int cartaoPonto;
    private Float salario;

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public int getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(int funcionario) {
        this.funcionario = funcionario;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public String getAdmicao() {
        return admicao;
    }

    public void setAdmicao(String admicao) {
        this.admicao = admicao;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }

   

    public int getUniforme() {
        return uniforme;
    }

    public void setUniforme(int uniforme) {
        this.uniforme = uniforme;
    }

    public int getCartaoPonto() {
        return cartaoPonto;
    }

    public void setCartaoPonto(int cartaoPonto) {
        this.cartaoPonto = cartaoPonto;
    }

    public Float getSalario() {
        return salario;
    }

    public void setSalario(Float salario) {
        this.salario = salario;
    }
    

}
