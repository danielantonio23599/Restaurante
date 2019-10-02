/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import modelo.ExcluzaoBEAN;
import modelo.ExcluzaoDAO;

/**
 *
 * @author Daniel
 */
public class ExcluzaoControle {

    private ExcluzaoDAO e = new ExcluzaoDAO();
    SharedP_Control c = new SharedP_Control();
    CaixaControle caixa = new CaixaControle();

    public void inserirExclusao(ExcluzaoBEAN pro) {
        int fun = c.listar().getFunCodigo();
        pro.setFuncionarioC(fun);
        e.inserir(pro);
    }

    public ArrayList<ExcluzaoBEAN> listarExclusaoVenda(int venda) {
        return e.listarExclusaoVenda(venda);
    }

    public ExcluzaoBEAN listarUm(String cod) {
        return e.listarUm(cod);
    }

    public ArrayList<ExcluzaoBEAN> listarExclusaoCaixa() {
        return e.listarExclusaoCaixa(caixa.getCaixa());
    }

}
