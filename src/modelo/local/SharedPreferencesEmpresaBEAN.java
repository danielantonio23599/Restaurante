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
public class SharedPreferencesEmpresaBEAN {

    private int empCodigo;
    private String empEmail;
    private String empSenha;
    private String empFantazia;
    private byte[] empLogo;

    public String getEmpFantazia() {
        return empFantazia;
    }

    public void setEmpFantazia(String empFantazia) {
        this.empFantazia = empFantazia;
    }

    public byte[] getEmpLogo() {
        return empLogo;
    }

    public void setEmpLogo(byte[] empLogo) {
        this.empLogo = empLogo;
    }

    public int getEmpCodigo() {
        return empCodigo;
    }

    public void setEmpCodigo(int empCodigo) {
        this.empCodigo = empCodigo;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpSenha() {
        return empSenha;
    }

    public void setEmpSenha(String empSenha) {
        this.empSenha = empSenha;
    }

}
