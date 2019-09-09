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
public class SharedPreferencesDAO {

    private  Connection connection;

    private  PreparedStatement stmt;

    public SharedPreferencesDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public  SharedPreferencesBEAN listarALl() {
        SharedPreferencesBEAN ca = new SharedPreferencesBEAN();
        ca.setFunCodigo(-1);

        String sql = "select * from shared_preferences;";
        try {
            stmt =  connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setFunCodigo(rs.getInt(1));
                ca.setFunNome(rs.getString(2));
                ca.setFunEmail(rs.getString(3));
                ca.setFunCargo(rs.getInt(5));

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public  void inserir(SharedPreferencesBEAN c) {

        String sql = "INSERT INTO shared_preferences ( funCodigo , funNome , funEmail , funCargo )VALUES (?, ?, ?, ?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setInt(1, c.getFunCodigo());
            stmt.setString(2, c.getFunNome());
            stmt.setString(3, c.getFunEmail());
            stmt.setInt(4, c.getFunCargo());
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public  void excluir() {
        String sql = "delete from shared_preferences ; ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
