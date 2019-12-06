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

/**
 *
 * @author Daniel
 */
public class VendaAtualDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public VendaAtualDAO() {
        this.connection = ConnectionFactory.getConnection();;
    }

    public boolean inserir(VendaAtualBEAN c) {
        String sql = "insert into venda_atual(caixa,venda,mesa) values (?,?,?)";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setInt(1, c.getCaixa());
            stmt.setInt(2, c.getVenda());
            stmt.setInt(3, c.getMesa());

            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public VendaAtualBEAN listar() {

        VendaAtualBEAN ca = new VendaAtualBEAN();
        String sql = "select * from venda_atual;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                ca.setCaixa(rs.getInt(1));
                ca.setVenda(rs.getInt(2));
                ca.setMesa(rs.getInt(3));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void atualiza(VendaAtualBEAN c) {
        String sql = "update venda_atual set caixa = ? , venda = ? , mesa = ?  where caixa = " + c.getCaixa() + ";";
        try {
            stmt = connection.prepareStatement(sql);

            stmt.setInt(1, c.getCaixa());
            stmt.setInt(2, c.getVenda());
            stmt.setInt(3, c.getMesa());
            int l = stmt.executeUpdate();
            stmt.close();
            if (l > 0) {
                System.out.println("Foram alterados " + l + " linhas");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
