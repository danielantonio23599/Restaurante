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
public class CargoDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public CargoDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public boolean adicionar(CargoBEAN c) {
        String sql = "insert into cargo(carNome,carAtribuicao,carRequisitos) values (?,?,?)";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getAtribuicao());
            stmt.setString(3, c.getRequisito());
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<CargoBEAN> listarALl() {
        ArrayList<CargoBEAN> c = new ArrayList<CargoBEAN>();

        String sql = "select * from cargo;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CargoBEAN ca = new CargoBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setAtribuicao(rs.getString(3));
                ca.setRequisito(rs.getString(4));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public void editar(CargoBEAN c) {
        String sql = "update cargo set carNome = ? , carAtribuicao = ? , carRequisitos = ? where carNome = '" + c.getNome() + "';";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getAtribuicao());
            stmt.setString(3, c.getRequisito());
            int l = stmt.executeUpdate();
            stmt.close();
            if (l > 0) {
                System.out.println("Foram alterados " + l + " linhas");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<CargoBEAN> localizar(int i) {
        String sql = "select * from cargo where carCodigo = ?;";
        CargoBEAN ca = new CargoBEAN();
        ArrayList<CargoBEAN> k = new ArrayList<CargoBEAN>();
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setNome(rs.getString(2));
                ca.setAtribuicao(rs.getString(3));
                ca.setRequisito(rs.getString(4));
                k.add(ca);
            }
            stmt.close();
            return k;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public void excluir(String cod) {
        String sql = "delete from cargo where carNome = ? ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cod);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CargoBEAN pegaCodigo(String cargo) {
        CargoBEAN c = new CargoBEAN();
        String sql = "select carCodigo from cargo where carNome = ?;";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cargo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c.setCodigo(rs.getInt(1));
            }
            stmt.close();
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CargoBEAN localizarPorNome(String cargo) {
        CargoBEAN c = new CargoBEAN();
        String sql = "select * from cargo where carNome = ?;";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cargo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c.setCodigo(rs.getInt(1));
                c.setNome(rs.getString(2));
                c.setAtribuicao(rs.getString(3));
                c.setRequisito(rs.getString(4));
            }
            stmt.close();
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String listarPorFuncionario(int funcionario) {
        String nome = "";
        String sql = "select carNome from funcionario join cargo where carCodigo = fun_carCodigo and funCodigo = " + funcionario + " ;";
        try {
            stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nome = rs.getString(1);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nome;
    }
}
