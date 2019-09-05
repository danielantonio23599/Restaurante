/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import modelo.PagamentoBEAN;
import modelo.PagamentoDAO;
import modelo.VendaBEAN;
import modelo.VendaDAO;

/**
 *
 * @author Daniel
 */
public class PagamentoControle {

    PagamentoDAO p = new PagamentoDAO();
    VendaDAO v = new VendaDAO();

    public ArrayList<PagamentoBEAN> listarAll() {
        return p.listarAll();

    }

    public PagamentoBEAN localizar(int pagamento) {
        return p.localizar(pagamento);

    }

    public String adicionar(PagamentoBEAN pagamento) {
        p.adicionar(pagamento);
        return "Pagamento adicionada com SUCESSO!!";

    }
    public String editar(PagamentoBEAN pagamento) {
        p.editar(pagamento);
        return "Pagamento atualizado com SUCESSO!!";

    }

    public String excluir(int pagamento) {
        boolean a = v.isPagamentoUtlizado(pagamento);
        if (a == false) {
            p.excluir(pagamento);
            return "Pagamento excluido com sucesso!!.";
        } else {
            return "Pagamento está sendo utilizado em alguma venda";
        }
    }

}
