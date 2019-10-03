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
        ArrayList<DespesaBEAN> despesas = new ArrayList<>();
        ArrayList<DespesaBEAN> t = d.listarAll();
        ArrayList<DespesaBEAN> u = listarDespesaDia();
        for (DespesaBEAN td : t) {
            DespesaBEAN ca = new DespesaBEAN();
            ca.setDespesaCaixa(false);
            for (DespesaBEAN cai : u) {
                if (td.getCodigo() == cai.getCodigo()) {
                    ca.setDespesaCaixa(true);
                }

            }
            ca.setCodigo(td.getCodigo());
            ca.setNome(td.getNome());
            ca.setDescricao(td.getDescricao());
            ca.setPreco(td.getPreco());
            despesas.add(ca);
        }
        return despesas;
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

    public ArrayList<DespesaBEAN> listarDespesaDia() {
        return d.listarDespesaCaixa(c.getCaixa());
    }

    public Float getTotalDespesasCaixa() {
        return d.getTotalDespesasCaixa(c.getCaixa());
    }

}
