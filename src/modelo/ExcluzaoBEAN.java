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
    private FuncionarioBEAN funcionario;
    private VendaBEAN venda;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
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

    public FuncionarioBEAN getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBEAN funcionario) {
        this.funcionario = funcionario;
    }

    public VendaBEAN getVenda() {
        return venda;
    }

    public void setVenda(VendaBEAN venda) {
        this.venda = venda;
    }
    
    
}
