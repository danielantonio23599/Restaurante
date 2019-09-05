/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class CaixaDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public CaixaDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public boolean abrirCaixa(CaixaBEAN c) {
        String sql = "insert into caixa(caiData,caiIn,caiTroco,caiStatus) values (?,?,?,?)";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getData());
            stmt.setString(2, c.getIn());
            stmt.setFloat(3, c.getTroco());
            stmt.setString(4, "aberto");

            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CaixaBEAN listar() {

        CaixaBEAN ca = new CaixaBEAN();
        String sql = "select * from caixa where caiStatus = 'aberto';";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                ca.setCodigo(rs.getInt(1));
                ca.setData(rs.getDate(2) + "");
                ca.setIn(rs.getString(3));
                ca.setOut(rs.getString(4));
                ca.setValorBruto(rs.getFloat(5));
                ca.setValorLiquido(rs.getFloat(6));
                ca.setTroco(rs.getFloat(7));
                ca.setStatus(rs.getString(8));

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }
    

    public void fecharCaixa(CaixaBEAN c) {
        String sql = "update caixa set carOut = ? , caiValorBruto = ? , caiValorLiquido = ? , caiStatus = ? where caiStatus = 'aberto';";
        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getOut());
            stmt.setFloat(2, c.getValorBruto());
            stmt.setFloat(3, c.getValorLiquido());
            stmt.setString(4, "fechado");
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
