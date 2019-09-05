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

/**
 *
 * @author Daniel
 */
public class PedidoDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public PedidoDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public ArrayList<Produtos> buscar(String produto) {

        ArrayList<Produtos> p = new ArrayList<>();
        String sql = "SELECT pedCodigo,pedNome, pedPreco FROM pedido WHERE pedNome LIKE '" + produto + "%';";

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
        String sql = "SELECT pedCodigo,pedNome, pedPreco FROM pedido WHERE pedCodigo LIKE '" + produto + "%';";

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

    public boolean adicionar(PedidoBEAN c) {
        String sql = "INSERT INTO pedido (pedNome, pedPreco, pedCusto, pedDescricao, pedArmonizacao,pedPreparo,pedTipo,pedFoto)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?,?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setFloat(2, c.getPreco());
            stmt.setFloat(3, c.getCusto());
            stmt.setString(4, c.getDescricao());
            stmt.setString(5, c.getArmonizacao());
            stmt.setString(6, c.getPreparo());
            stmt.setString(7, c.getTipo());
            stmt.setBytes(8, c.getFoto());
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PedidoBEAN> listarAll() {
        ArrayList<PedidoBEAN> c = new ArrayList<PedidoBEAN>();

        String sql = "select * from pedido;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PedidoBEAN ca = new PedidoBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setPreco(rs.getFloat(3));
                ca.setCusto(rs.getFloat(4));
                ca.setArmonizacao(rs.getString(5));
                ca.setFoto(rs.getBytes(6));
                ca.setPreparo(rs.getString(7));
                ca.setDescricao(rs.getString(8));
                ca.setTipo(rs.getString(9));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public PedidoBEAN localizar(int pedido) {
        PedidoBEAN ca = new PedidoBEAN();

        String sql = "select * from pedido where pedCodigo = " + pedido + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setPreco(rs.getFloat(3));
                ca.setCusto(rs.getFloat(4));
                ca.setArmonizacao(rs.getString(5));
                ca.setFoto(rs.getBytes(6));
                ca.setPreparo(rs.getString(7));
                ca.setDescricao(rs.getString(8));
                ca.setTipo(rs.getString(9));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void editar(PedidoBEAN c) {
        String sql = "update pedido set pedNome = ? , pedPreco = ? , pedCusto = ? , pedDescricao = ?, pedArmonizacao = ?"
                + ", pedPreparo = ? , pedTipo = ?, pedFoto = ? where pedCodigo = " + c.getCodigo() + ";";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setFloat(2, c.getPreco());
            stmt.setFloat(3, c.getCusto());
            stmt.setString(4, c.getDescricao());
            stmt.setString(5, c.getArmonizacao());
            stmt.setString(6, c.getPreparo());
            stmt.setString(7, c.getTipo());
            stmt.setBytes(8, c.getFoto());

            int l = stmt.executeUpdate();
            stmt.close();
            if (l > 0) {
                System.out.println("Foram alterados " + l + " linhas");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
public ArrayList<Produtos> listarPedidos() {
        ArrayList<Produtos> c = new ArrayList<Produtos>();

        String sql = "select * from pedido;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produtos ca = new Produtos();
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setPreco(rs.getFloat(3));
                ca.setDescricao(rs.getString(8));
                ca.setTipo("pedidos");
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }
    public void excluir(int cod) {
        String sql = "delete from pedido where pedCodigo = ? ";
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
