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
        String sql = "INSERT INTO excluzao (advNome, advMotivo , advTime, advQTD, advOBS, adv_funCodigo, adv_venCodigo)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?);";
        System.out.println("funcionario " + c.getFuncionario() + " venda " + c.getVenda());
        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getMotivo());
            stmt.setString(3, c.getTime());
            stmt.setFloat(4, c.getQuantidade());
            stmt.setString(5, c.getObs());
            stmt.setInt(6, c.getFuncionarioC());
            stmt.setInt(7, c.getVenda());
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ExcluzaoBEAN> listarExclusaoVenda(int venda) {
        ArrayList<ExcluzaoBEAN> c = new ArrayList<>();

        String sql = "select advCodigo, advNome, advMotivo, advQTD, advObs, advTime, funNome, venMesa\n"
                + "	from excluzao join funcionario join venda\n"
                + "		where  adv_funCodigo = funCodigo and venCodigo = adv_venCodigo and venCodigo ='" + venda + "' \n"
                + "			group by venCodigo \n"
                + "				order by venMesa;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExcluzaoBEAN e = new ExcluzaoBEAN();
                e.setCodigo(rs.getInt(1));
                e.setNome(rs.getString(2));
                e.setMotivo(rs.getString(3));
                e.setQuantidade(rs.getFloat(4));
                e.setObs(rs.getString(5));
                e.setTime(rs.getString(6));
                e.setFuncionario(rs.getString(7));
                e.setVenda(rs.getInt(8));
                c.add(e);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public ExcluzaoBEAN listarUm(String cod) {
        ExcluzaoBEAN e = new ExcluzaoBEAN();
        System.out.println("Codigo " + cod);
        String sql = "select advCodigo, advNome, advMotivo, advTime, advQTD, advObs,"
                + " adv_funCodigo,adv_venCodigo, funNome from excluzao join funcionario where "
                + "adv_funCodigo = funCodigo and advCodigo = " + cod + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                e.setCodigo(rs.getInt(1));
                e.setNome(rs.getString(2));
                e.setMotivo(rs.getString(3));
                e.setTime(rs.getString(4));
                e.setQuantidade(rs.getFloat(5));
                e.setObs(rs.getString(6));
                e.setFuncionarioC(rs.getInt(7));
                e.setVenda(rs.getInt(8));
                e.setFuncionario(rs.getString(9));
            }
            stmt.close();

        } catch (SQLException er) {
            throw new RuntimeException();
        }
        return e;
    }

    public ArrayList<ExcluzaoBEAN> listarExclusaoCaixa(int caixa) {
        ArrayList<ExcluzaoBEAN> c = new ArrayList<>();

        String sql = "select advCodigo, advNome, advMotivo, advQTD, advObs, advTime, funNome, venMesa\n"
                + "	from excluzao join funcionario join venda\n"
                + "		where  adv_funCodigo = funCodigo and venCodigo = adv_venCodigo and ven_caiCodigo =" + caixa + " \n"
                + "			group by venCodigo \n"
                + "				order by venMesa;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExcluzaoBEAN e = new ExcluzaoBEAN();
                e.setCodigo(rs.getInt(1));
                e.setNome(rs.getString(2));
                e.setMotivo(rs.getString(3));
                e.setQuantidade(rs.getFloat(4));
                e.setObs(rs.getString(5));
                e.setTime(rs.getString(6));
                e.setFuncionario(rs.getString(7));
                e.setVenda(rs.getInt(8));
                c.add(e);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

}
