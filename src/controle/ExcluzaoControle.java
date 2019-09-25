/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modelo.ExcluzaoBEAN;
import modelo.ExcluzaoDAO;

/**
 *
 * @author Daniel
 */
public class ExcluzaoControle {

    private ExcluzaoDAO e = new ExcluzaoDAO();
    SharedP_Control c = new SharedP_Control();

    public void inserirExclusao(ExcluzaoBEAN pro) {
        int fun = c.listar().getFunCodigo();
        pro.setFuncionario(fun);

        e.inserir(pro);
    }

}
