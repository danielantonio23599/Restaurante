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
public class SharedPreferencesEmpresaDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public SharedPreferencesEmpresaDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public SharedPreferencesEmpresaBEAN listar() {
        SharedPreferencesEmpresaBEAN ca = new SharedPreferencesEmpresaBEAN();
        ca.setEmpCodigo(0);

        String sql = "select * from shared_preferences_empresa;";
        try {
            PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setEmpCodigo(rs.getInt(1));
                ca.setEmpEmail(rs.getString(2));
                ca.setEmpSenha(rs.getString(3));
                ca.setEmpLogo(rs.getBytes(4));
                ca.setEmpFantazia(rs.getString(5));

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void inserir(SharedPreferencesEmpresaBEAN c) {

        String sql = "INSERT INTO shared_preferences_empresa ( speCodigo , speEmail ,speSenha , speLogo,speFantazia )VALUES (?, ?, ?, ?, ?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setInt(1, c.getEmpCodigo());
            stmt.setString(2, c.getEmpEmail());
            stmt.setString(3, c.getEmpSenha());
            stmt.setBytes(4, c.getEmpLogo());
            stmt.setString(5, c.getEmpFantazia());
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void excluir() {
        String sql = "delete from shared_preferences_empresa ; ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
