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
public class VendaDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public VendaDAO() {
        this.connection = ConnectionFactory.getConnection();;
    }

    public int abrirMesa(VendaBEAN c) {
        String sql = "insert into venda(venCheckIn,venMesa,ven_caiCodigo, venStatus) values (?,?,?,?)";
        System.out.println(" check in "+c.getCheckIn()+" mesa "+c.getMesa()+" caixa "+c.getCaixa());
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, c.getCheckIn());
            stmt.setInt(2, c.getMesa());
            stmt.setInt(3, c.getCaixa());
            stmt.setString(4, "aberta");
            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getVenda(c.getCheckIn());
    }

    private int getVenda(String hora) {
        int cod = 0;
        String sql = "select venCodigo from venda where venCheckIn = '" + hora + "';";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cod = rs.getInt(1);

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return cod;
    }

    public ArrayList<Mesa> listarMesasAbertas() {
        ArrayList<Mesa> c = new ArrayList<>();

        String sql = "select venMesa, sum(pevQTD*pedPreco) \n"
                + "from\n"
                + " caixa join venda join pedido_venda join pedido \n"
                + "	where\n"
                + "    caiCodigo = ven_caiCodigo and pev_venCodigo = venCodigo \n"
                + "    and pev_pedCodigo = pedCodigo and caiStatus = 'aberto'and venStatus = 'aberta' group by venMesa;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mesa ca = new Mesa();
                ca.setMesa(rs.getInt(1));
                ca.setValor(rs.getFloat(2));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public void atualiza(VendaBEAN c) {
        String sql = "update venda_atual set caixa = ? , venda = ? , mesa = ?  where caixa = " + c.getCaixa() + ";";
        try {
            stmt = connection.prepareStatement(sql);

            stmt.setInt(1, c.getCaixa());
            stmt.setInt(2, c.getCodigo());
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

    public boolean isPagamentoUtlizado(int pagamento) {
        int cod = 0;
        String sql = "select venCodigo from venda where ven_pagCodigo = '" + pagamento + "';";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cod = rs.getInt(1);

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        if (cod == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int getVenda(int mesa) {
        int cod = 0;
        String sql = "select venCodigo from venda where venMesa = " + mesa + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cod = rs.getInt(1);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        //retorna 0 se não tiver nenhuma venda naquela mesa
        return cod;
    }

    public void excluir(int venda) {
        String sql = "delete from venda where venCodigo = " + venda + ";";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
