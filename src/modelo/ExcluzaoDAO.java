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
public class ExcluzaoDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public ExcluzaoDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public void inserir(ExcluzaoBEAN c) {
        String sql = "INSERT INTO excluzao (advNome, advMotivo , advTime, adv_funCodigo, adv_venCodigo)"
                + " VALUES (?, ?, ?, ?,?);";
        System.out.println("funcionario " + c.getFuncionario() + " venda " +c.getVenda());
        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getMotivo());
            stmt.setString(3, c.getTime());
            stmt.setInt(4, c.getFuncionario());
            stmt.setInt(5, c.getVenda());

            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
