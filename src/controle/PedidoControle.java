/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import modelo.PedidoBEAN;
import modelo.PedidoDAO;
import modelo.ProdutoBEAN;
import modelo.Produtos;

/**
 *
 * @author Daniel
 */
public class PedidoControle {

    private PedidoDAO p = new PedidoDAO();

    public DefaultComboBoxModel buscar(String produto) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        ArrayList<Produtos> pe = p.buscar(produto);
        for (Produtos p : pe) {
            modelo.addElement(p.getCodigo() + " : " + p.getNome() + " : R$ " + p.getPreco());

        }

        return modelo;
    }

    public DefaultComboBoxModel buscar(int produto) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        ArrayList<Produtos> pe = p.buscar(produto);
        for (Produtos p : pe) {
            modelo.addElement(p.getCodigo() + " : " + p.getNome() + " : R$ " + p.getPreco());

        }

        return modelo;

    }

    public ArrayList<PedidoBEAN> listarAll() {
        return p.listarAll();
    }

    public String cadastrar(PedidoBEAN f) {
        p.adicionar(f);
        return "Cadastro realizado com sucesso!!";
    }

    public String editar(PedidoBEAN f) {
        p.editar(f);
        return "Pedido editado com sucesso!!";
    }

    public String excluir(int cod) {
        p.excluir(cod);
        return "Exclusão realizado com sucesso!!";
    }

    public PedidoBEAN localizar(int i) {
        return p.localizar(i);
    }

    public ArrayList<Produtos> listarPedidos(ArrayList<Produtos> pro) {
        ArrayList<Produtos> produtos = p.listarPedidos();
        for (Produtos produto : produtos) {
            pro.add(produto);
        }
        return pro;
    }

    public Produtos buscarUm(String combo) {
        ArrayList<Produtos> todos = p.listarPedidos();
        for (Produtos p : todos) {
            String pro = p.getCodigo() + " : " + p.getNome() + " : R$ " + p.getPreco();
            if (combo.equals(pro)) {
                return p;
            }
        }
        return null;

    }

}
