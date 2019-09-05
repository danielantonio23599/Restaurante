/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import modelo.ProdutoBEAN;
import modelo.ProdutoDAO;
import modelo.Produtos;

/**
 *
 * @author Daniel
 */
public class ProdutoControle {

    private ProdutoDAO p = new ProdutoDAO();
    private PedidoControle pc = new PedidoControle();

    public DefaultComboBoxModel buscar(String produto) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();

        ArrayList<Produtos> pp = p.buscar(produto);
        for (Produtos p : pp) {
            modelo.addElement(p.getCodigo() + " : " + p.getNome() + " : R$ " + p.getPreco());

        }
        return modelo;
    }

    public ProdutoBEAN localizar(int produto) {

        return p.localizar(produto);
    }

    public DefaultComboBoxModel buscar(int produto) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();

        ArrayList<Produtos> pp = p.buscar(produto);
        for (Produtos p : pp) {
            modelo.addElement(p.getCodigo() + " : " + p.getNome() + " : R$ " + p.getPreco());

        }

        return modelo;
    }

    public ArrayList<ProdutoBEAN> listarAll() {
        return p.listarALl();
    }

    public String cadastrar(ProdutoBEAN f) {
        p.adicionar(f);

        return "Cadastro Realizado com sucesso!!";
    }

    public String editar(ProdutoBEAN f) {
        p.editar(f);
        return "Produto editado com sucesso!!";
    }

    public String excluir(int cod) {
        p.excluir(cod);

        return "Produto Excluido com sucesso!!";
    }

    public Produtos buscarUm(String combo) {
        ArrayList<Produtos> produtos = p.listarProdutos();
        ArrayList<Produtos> todos = pc.listarPedidos(produtos);

        for (Produtos p : todos) {
            String pro = p.getCodigo() + " : " + p.getNome() + " : R$ " + p.getPreco();
            if (combo.equals(pro)) {
                return p;
            }
        }
        return null;

    }

}
