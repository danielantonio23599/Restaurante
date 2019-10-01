/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import modelo.DespesaBEAN;
import modelo.DespesaDAO;
import modelo.DespesaDiaDAO;

/**
 *
 * @author Daniel
 */
public class DespesaControle {

    private DespesaDiaDAO desdia = new DespesaDiaDAO();
    private DespesaDAO d = new DespesaDAO();
    private CaixaControle c = new CaixaControle();

    public ArrayList<DespesaBEAN> listarALL() {
        return d.listarAll();
    }

    public String adicionar(DespesaBEAN despesa) {
        d.adicionar(despesa);
        return "Cadastro Realizado com SUCESSO!!";
    }

    public String excluir(ArrayList<DespesaBEAN> des) {
        for (DespesaBEAN d : des) {
            this.d.excluir(d.getCodigo());
        }
        return "Excluzão realizada com SUCESSO!!";
    }

    public String adicionarDespesaDia(ArrayList<DespesaBEAN> dadosIncluir) {
        for (DespesaBEAN d : dadosIncluir) {
            this.d.adicionarDespesaDia(d, c.getCaixa());
        }
        return "Cadastro Realizado com SUCESSO!!";

    }

}
