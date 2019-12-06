/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.local.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class FuncionarioDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public FuncionarioDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public boolean adicionar(FuncionarioBEAN c) {
        String sql = "INSERT INTO funcionario (funSalario, funDataAdmicao, funDataNascimento, funTelefone,"
                + " funEndereco, funNome, funUniforme, funEmail, funCPF, funRG, funSenha,funNunCartao, fun_carCodigo)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setFloat(1, c.getSalario());
            stmt.setString(2, c.getDataAdmicao() + "");
            stmt.setString(3, c.getDataNacimento() + "");
            stmt.setString(4, c.getTelefone());
            stmt.setString(5, c.getEndereco());
            stmt.setString(6, c.getNome());
            stmt.setInt(7, c.getUniforme());
            stmt.setString(8, c.getEmail());
            stmt.setString(9, c.getCPF());
            stmt.setString(10, c.getRG());
            stmt.setString(11, c.getSenha());
            stmt.setInt(12, c.getCartao());
            stmt.setInt(13, c.getCargo());
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<FuncionarioBEAN> listarALl() {
        ArrayList<FuncionarioBEAN> c = new ArrayList<>();

        String sql = "select * from funcionario;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                FuncionarioBEAN ca = new FuncionarioBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setSalario(rs.getInt(2));
                ca.setDataAdmicao(rs.getDate(3)+"");
                ca.setDataNacimento(rs.getDate(4) + "");
                ca.setTelefone(rs.getString(5));
                ca.setEndereco(rs.getString(6));
                ca.setNome(rs.getString(7));
                ca.setUniforme(rs.getInt(8));
                // ca.setFoto(rs.getBytes(9));                
                ca.setCargo(rs.getInt(15));
                ca.setEmail(rs.getString(10));
                ca.setCPF(rs.getString(11));
                ca.setRG(rs.getString(12));
                ca.setSenha(rs.getString(13));
                ca.setCartao(rs.getInt(14));
                //ca.setCago(rs.getInt(16));
                c.add(ca);
            }
            stmt.close();
            return c;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public int funCargo(int cargo) {
        int cont = 0;
        String sql = "select count(funCodigo) from funcionario where fun_carCodigo = ?;";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cargo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cont = rs.getInt(1);
            }
            stmt.close();
            return cont;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean numeroCartaoExistente(int codigo) {
        int cont = 0;
        String sql = "select count(funCodigo) from funcionario where funNunCartao = ?;";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cont = rs.getInt(1);
            }
            stmt.close();
            if (cont == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editar(FuncionarioBEAN c) {
        String sql = "update funcionario set funSalario = ? , funDataAdimicao"
                + " = ? , funDataNascimento = ? , funTelefone = ?,"
                + "funEndereco= ?, funUniforme = ?, funSenha = ?"
                + " where funCodigo = " + c.getCodigo() + ";";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setFloat(1, c.getSalario());
            stmt.setString(2, c.getDataAdmicao());
            stmt.setString(3, c.getDataNacimento());
            stmt.setString(4, c.getTelefone());
            stmt.setString(5, c.getEndereco());
            stmt.setInt(7, c.getUniforme());
            stmt.setString(12, c.getSenha());
            int l = stmt.executeUpdate();
            stmt.close();
            if (l > 0) {
                System.out.println("Foram alterados " + l + " linhas");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public FuncionarioBEAN localizar(String email) {
        String sql = "select * from funcionario where funEmail = ?;";
        FuncionarioBEAN ca = new FuncionarioBEAN();

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setSalario(rs.getInt(2));
                ca.setDataAdmicao(rs.getDate(3) + "");
                ca.setDataNacimento(rs.getDate(4) + "");
                ca.setTelefone(rs.getString(5));
                ca.setEndereco(rs.getString(6));
                ca.setNome(rs.getString(7));
                ca.setUniforme(rs.getInt(8));
                ca.setCargo(rs.getInt(9));
                ca.setEmail(rs.getString(10));
                ca.setCPF(rs.getString(11));
                ca.setRG(rs.getString(12));
                ca.setSenha(rs.getString(13));
            }
            stmt.close();
            return ca;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public FuncionarioBEAN localizar(int codigo) {
        String sql = "select * from funcionario where funCodigo = ?;";
        FuncionarioBEAN ca = new FuncionarioBEAN();

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setSalario(rs.getInt(2));
                ca.setDataAdmicao(rs.getDate(3) + "");
                ca.setDataNacimento(rs.getDate(4) + "");
                ca.setTelefone(rs.getString(5));
                ca.setEndereco(rs.getString(6));
                ca.setNome(rs.getString(7));
                ca.setUniforme(rs.getInt(8));
                ca.setCargo(rs.getInt(9));
                ca.setEmail(rs.getString(10));
                ca.setCPF(rs.getString(11));
                ca.setRG(rs.getString(12));
                ca.setSenha(rs.getString(13));
            }
            stmt.close();
            return ca;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public Boolean isExiste(String email) {
        String sql = "select funCodigo from funcionario where funEmail = ?;";
        boolean retorno = false;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                retorno = true;

            }
            stmt.close();
            return retorno;

        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public int Login(String email, String senha) {
        String sql = "select funCodigo from funcionario where funEmail = '" + email + "' and funSenha = '" + senha + "';";
        int codigo = 0;
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                codigo = rs.getInt(1);
            }
            stmt.close();
            return codigo;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public void excluir(int cod) {
        String sql = "delete from funcionario where funCodigo = ? ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cod);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> buscar(String funcionario) {

        ArrayList<String> p = new ArrayList<>();
        String sql = "SELECT funEmail FROM funcionario ;";//WHERE funEmail LIKE '" + funcionario + "%';";

        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                p.add(rs.getString(1));
                // modelo.addElement(rs.getInt(1) + " : " + rs.getString(2) + String.format("%80s"," R$ " + rs.getFloat(3)+""));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return p;

    }

}
