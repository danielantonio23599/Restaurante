/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Daniel
 */
public class ProdutoDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public ProdutoDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public ArrayList<Produtos> buscar(String produto) {

        ArrayList<Produtos> p = new ArrayList<>();
        String sql = "SELECT proCodigo,proNome, proPreco FROM produto WHERE proNome LIKE '" + produto + "%';";

        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produtos pp = new Produtos();
                pp.setCodigo(rs.getInt(1));
                pp.setNome(rs.getString(2));
                pp.setPreco(rs.getFloat(3));
                p.add(pp);
                // modelo.addElement(rs.getInt(1) + " : " + rs.getString(2) + String.format("%80s"," R$ " + rs.getFloat(3)+""));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return p;

    }

    public ArrayList<Produtos> buscar(int produto) {

        ArrayList<Produtos> p = new ArrayList<>();
        String sql = "SELECT proCodigo,proNome, proPreco FROM produto WHERE proCodigo LIKE '" + produto + "%';";

        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produtos pp = new Produtos();
                pp.setCodigo(rs.getInt(1));
                pp.setNome(rs.getString(2));
                pp.setPreco(rs.getFloat(3));
                p.add(pp);
                // modelo.addElement(rs.getInt(1) + " : " + rs.getString(2) + String.format("%80s"," R$ " + rs.getFloat(3)+""));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return p;

    }

    public boolean adicionar(ProdutoBEAN c) {
        String sql = "INSERT INTO produto (proNome, proPreco, proCusto, proDescricao, proQuantidade)"
                + " VALUES (?, ?, ?, ?, ?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setFloat(2, c.getPreco());
            stmt.setFloat(3, c.getCusto());
            stmt.setString(4, c.getDescricao());
            stmt.setInt(5, c.getQuantidade());
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ProdutoBEAN> listarALl() {
        ArrayList<ProdutoBEAN> c = new ArrayList<ProdutoBEAN>();

        String sql = "select * from produto;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProdutoBEAN ca = new ProdutoBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setPreco(rs.getFloat(3));
                ca.setCusto(rs.getFloat(4));
                ca.setQuantidade(rs.getInt(5));
                ca.setDescricao(rs.getString(6));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public ArrayList<Produtos> listarProdutos() {
        ArrayList<Produtos> c = new ArrayList<Produtos>();

        String sql = "select * from produto;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produtos ca = new Produtos();
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setPreco(rs.getFloat(3));
                ca.setDescricao(rs.getString(6));
                ca.setTipo("produto");
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public ProdutoBEAN localizar(int produto) {
        ProdutoBEAN c = new ProdutoBEAN();

        String sql = "select * from produto where proCodigo = " + produto + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c.setCodigo(rs.getInt(1));
                c.setNome(rs.getString(2));
                c.setPreco(rs.getFloat(3));
                c.setCusto(rs.getFloat(4));
                c.setQuantidade(rs.getInt(5));
                c.setDescricao(rs.getString(6));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public void editar(ProdutoBEAN c) {
        String sql = "update produto set proNome = ? , proPreco = ? , proCusto = ? , proQuantidade = ?, proDescricao = ? where proCodigo = " + c.getCodigo() + ";";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setFloat(2, c.getPreco());
            stmt.setFloat(3, c.getCusto());
            stmt.setInt(4, c.getQuantidade());
            stmt.setString(5, c.getDescricao());

            int l = stmt.executeUpdate();
            stmt.close();
            if (l > 0) {
                System.out.println("Foram alterados " + l + " linhas");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void excluir(int cod) {
        String sql = "delete from produto where proCodigo = ? ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cod);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
