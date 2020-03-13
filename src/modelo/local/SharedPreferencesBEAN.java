/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.local;

/**
 *
 * @author Daniel
 */
public class SharedPreferencesBEAN {

    private int funCodigo;
    private String funNome;
    private String funEmail;
    private String funSenha;
    private int funCargo;
    private String cargo;

    public int getFunCodigo() {
        return funCodigo;
    }

    public void setFunCodigo(int funCodigo) {
        this.funCodigo = funCodigo;
    }

    public String getFunNome() {
        return funNome;
    }

    public void setFunNome(String funNome) {
        this.funNome = funNome;
    }

    public String getFunEmail() {
        return funEmail;
    }

    public void setFunEmail(String funEmail) {
        this.funEmail = funEmail;
    }

    public String getFunSenha() {
        return funSenha;
    }

    public void setFunSenha(String funSenha) {
        this.funSenha = funSenha;
    }

    public int getFunCargo() {
        return funCargo;
    }

    public void setFunCargo(int funCargo) {
        this.funCargo = funCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}
