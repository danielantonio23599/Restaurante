/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.local;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Daniel
 */
public class ServidorDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public ServidorDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public ServidorBEAN listar() {
        ServidorBEAN ca = new ServidorBEAN();
        ca.setIp("");
        String sql = "select * from servidor;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setIp(rs.getString(1));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void inserir(ServidorBEAN c) {

        String sql = "INSERT INTO servidor ( ser_ip)VALUES (?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getIp());
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void excluir() {
        String sql = "delete from servidor ; ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
