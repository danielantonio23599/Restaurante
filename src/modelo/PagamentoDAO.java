/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.local.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class PagamentoDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public PagamentoDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public boolean adicionar(PagamentoBEAN c) {
        String sql = "INSERT INTO pagamento (pagNome, pagAcrescimo)"
                + " VALUES (?,?);";

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, c.getNome());
            stmt.setFloat(2, c.getAcrescimo());

            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PagamentoBEAN> listarAll() {
        ArrayList<PagamentoBEAN> c = new ArrayList<PagamentoBEAN>();

        String sql = "select * from pagamento;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PagamentoBEAN ca = new PagamentoBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setAcrescimo(rs.getFloat(3));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public PagamentoBEAN localizar(int pedido) {
        PagamentoBEAN ca = new PagamentoBEAN();

        String sql = "select * from pagamento where pagCodigo = " + pedido + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setAcrescimo(rs.getFloat(3));
                
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }
    public PagamentoBEAN localizar(String pedido) {
        PagamentoBEAN ca = new PagamentoBEAN();

        String sql = "select * from pagamento where pagNome = '" + pedido + "';";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setAcrescimo(rs.getFloat(3));
                
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void editar(PagamentoBEAN c) {
        String sql = "update pagamento set pagNome = ? , pagAcrescimo = ? "
                + " where pagCodigo = " + c.getCodigo() + ";";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setFloat(2, c.getAcrescimo());

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
        String sql = "delete from pagamento where pagCodigo = ? ";
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
