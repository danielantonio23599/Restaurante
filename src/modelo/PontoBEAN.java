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
public class PontoBEAN {
    private int codigo;
    private Date data;
    private String IN;
    private String OUT;
    private FuncionarioBEAN funcionario;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getIN() {
        return IN;
    }

    public void setIN(String IN) {
        this.IN = IN;
    }

    public String getOUT() {
        return OUT;
    }

    public void setOUT(String OUT) {
        this.OUT = OUT;
    }

    public FuncionarioBEAN getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBEAN funcionario) {
        this.funcionario = funcionario;
    }
    
}
