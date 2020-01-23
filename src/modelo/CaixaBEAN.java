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
    private float trocoIn;
    private float trocoFin;
    private String status;
    private int funcionario;

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

    public float getTrocoIn() {
        return trocoIn;
    }

    public void setTrocoIn(float trocoIn) {
        this.trocoIn = trocoIn;
    }

    public float getTrocoFin() {
        return trocoFin;
    }

    public void setTrocoFin(float trocoFin) {
        this.trocoFin = trocoFin;
    }  

    public int getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(int funcionario) {
        this.funcionario = funcionario;
    }
    
}
