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
public class PedidoVendaDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public PedidoVendaDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public boolean adicionar(PedidoVendaBEAN c) {
        String sql = "INSERT INTO pedido_venda (pev_pedCodigo, pev_venCodigo, pevTime, pevQTD,"
                + " pevObs,pevImpresso )"
                + " VALUES (?, ?, ?, ?, ?, ?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setInt(1, c.getPedido());
            stmt.setInt(2, c.getVenda());
            stmt.setString(3, c.getHora());
            stmt.setFloat(4, c.getQuantidade());
            stmt.setString(5, c.getDescricao());
            stmt.setString(6, "of");
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PedidoVendaBEAN> listarAll() {
        ArrayList<PedidoVendaBEAN> c = new ArrayList<PedidoVendaBEAN>();

        String sql = "select * from pedido_venda;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PedidoVendaBEAN ca = new PedidoVendaBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setPedido(rs.getInt(2));
                ca.setVenda(rs.getInt(3));
                ca.setHora(rs.getString(4));
                ca.setQuantidade(rs.getFloat(5));
                ca.setDescricao(rs.getString(6));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public ArrayList<ProdutosGravados> OFFImpressoAll(int mesa) {
        ArrayList<ProdutosGravados> c = new ArrayList<ProdutosGravados>();

        String sql = "SELECT pevCodigo,pev_pedCodigo, pedNome,pevQTD, pevTime,venMesa, (pedPreco * pevQTD) "
                + "FROM pedido join pedido_venda join venda"
                + " where"
                + " venCodigo = pev_venCodigo and pev_pedCodigo = pedCodigo and pevImpresso = 'of'and venMesa=" + mesa + " ;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProdutosGravados ca = new ProdutosGravados();
                ca.setCodPedidVenda(rs.getInt(1));
                ca.setCodProduto(rs.getInt(2));
                ca.setNome(rs.getString(3));
                ca.setQuantidade(rs.getFloat(4));
                ca.setTime(rs.getString(5));
                ca.setMesa(rs.getInt(6));
                ca.setValor(rs.getFloat(7));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public ArrayList<ProdutosGravados> produtosMesa(int mesa) {
        ArrayList<ProdutosGravados> c = new ArrayList<ProdutosGravados>();

        String sql = "SELECT pev_venCodigo,pev_pedCodigo, pedNome,pevQTD, pevTime,venMesa, (pedPreco * pevQTD) "
                + "FROM pedido join pedido_venda join venda"
                + " where"
                + " venCodigo = pev_venCodigo and pev_pedCodigo = pedCodigo and venMesa=" + mesa + " ;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProdutosGravados ca = new ProdutosGravados();
                ca.setCodPedidVenda(rs.getInt(1));
                ca.setCodProduto(rs.getInt(2));
                ca.setNome(rs.getString(3));
                ca.setQuantidade(rs.getFloat(4));
                ca.setTime(rs.getString(5));
                ca.setMesa(rs.getInt(6));
                ca.setValor(rs.getFloat(7));
                c.add(ca);
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return c;
    }

    public PedidoVendaBEAN localizar(int pedido, int venda) {
        PedidoVendaBEAN ca = new PedidoVendaBEAN();

        String sql = "select * from pedido_venda where pev_pedCodigo = " + pedido + " and pev_venCondigo = " + venda + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setPedido(rs.getInt(2));
                ca.setVenda(rs.getInt(3));
                ca.setHora(rs.getString(4));
                ca.setQuantidade(rs.getFloat(5));
                ca.setDescricao(rs.getString(6));

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void transferir(int origem, int pedido, int destino, String time) {
        String sql = "update pedido_venda set pev_venCodigo = " + destino + "  "
                + "where pev_pedCodigo = " + pedido + " and pev_venCodigo = " + origem + " and pevTime = '"+time+"' ;";

        try {
            stmt = connection.prepareStatement(sql);
            int l = stmt.executeUpdate();
            stmt.close();
            if (l > 0) {
                System.out.println("Foram alterados " + l + " linhas");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void excluir(int pedido, int venda) {
        String sql = "delete from pedido_venda where pev_pedCodigo = ? and pev_venCodigo";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pedido);
            stmt.setInt(2, venda);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void transferirMesa(int origem, int destino) {
        String sql = "update pedido_venda set pev_venCodigo = " + destino + "  where pev_venCodigo = " + origem + " ;";

        try {
            stmt = connection.prepareStatement(sql);
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
