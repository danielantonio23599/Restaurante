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
public class ExcluzaoBEAN {

    private int codigo;
    private String nome;
    private String motivo;
    private String time;
    private int funcionario;
    private int venda;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(int funcionario) {
        this.funcionario = funcionario;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

}
