/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.local.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class PedidoDAO {

    private Connection connection;

    private PreparedStatement stmt;

    public PedidoDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public boolean adicionar(PedidoBEAN c) {
        String sql = "INSERT INTO pedido (pedTime, pedQTD,"
                + " pedObs,pedImpresso,ped_proCodigo,ped_venCodigo )"
                + " VALUES (?, ?, ?, ?, ?, ?);";

        try {
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, c.getTime());
            stmt.setFloat(2, c.getQuantidade());
            stmt.setString(3, c.getObservacao());
            stmt.setString(4, "of");
            stmt.setInt(5, c.getProduto());
            stmt.setInt(6, c.getVenda());
            stmt.execute();
            stmt.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PedidoBEAN> listarAll() {
        ArrayList<PedidoBEAN> c = new ArrayList<PedidoBEAN>();

        String sql = "select * from pedido where ped_excCodigo !> 0;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PedidoBEAN ca = new PedidoBEAN();
                ca.setCodigo(rs.getInt(1));
                ca.setTime(rs.getString(2));
                ca.setQuantidade(rs.getFloat(3));
                ca.setObservacao(rs.getString(4));
                ca.setImpresso(rs.getString(5));
                ca.setExcluzao(rs.getInt(6));
                ca.setProduto(rs.getInt(7));
                ca.setVenda(rs.getInt(8));
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

        String sql = "SELECT ped_venCodigo,ped_proCodigo, proNome,pedQTD, pedTime,venMesa, (proPreco * pedQTD) "
                + "FROM produto join pedido join venda"
                + " where"
                + " venCodigo = ped_venCodigo and ped_proCodigo = proCodigo and pedImpresso = 'of'and venMesa=" + mesa + " and venStatus = 'aberta' and ped_excCodigo !> 0;";
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

        String sql = "SELECT ped_venCodigo,ped_proCodigo, proNome,pedQTD, pedTime,venMesa, (proPreco * pedQTD) "
                + "FROM produto join pedido join venda"
                + " where"
                + " venCodigo = ped_venCodigo and ped_proCodigo = proCodigo and venMesa=" + mesa + " and venStatus = 'aberta' and ped_excCodigo !> 0;";
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

    public PedidoBEAN localizar(int produto, int venda, String time) {
        PedidoBEAN ca = new PedidoBEAN();

        String sql = "select * from pedido where ped_proCodigo = " + produto + " and ped_venCondigo = " + venda + " and pedTime = '" + time + "' and ped_excCodigo !> 0;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ca.setCodigo(rs.getInt(1));
                ca.setTime(rs.getString(2));
                ca.setQuantidade(rs.getFloat(3));
                ca.setObservacao(rs.getString(4));
                ca.setImpresso(rs.getString(5));
                ca.setExcluzao(rs.getInt(6));
                ca.setProduto(rs.getInt(7));
                ca.setVenda(rs.getInt(8));

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return ca;
    }

    public void transferir(int origem, int produto, int destino, String time) {
        String sql = "update pedido set ped_venCodigo = " + destino + "  "
                + "where ped_proCodigo = " + produto + " and ped_venCodigo = " + origem + " and pedTime = '" + time + "' ;";

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

    public void excluir(int codigo, int excluzao) {
        String sql = "update pedido set ped_excCodigo = " + excluzao + " where pedCodigo = ? ;";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigo);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void transferirMesa(int origem, int destino) {
        String sql = "update pedido set ped_venCodigo = " + destino + "  where ped_venCodigo = " + origem + " ;";

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

    /* public void excluirProduto(int venda, String motivo, int produto, String time) {
        String sql = "delete from pedido where ped_proCodigo = " + produto + " and ped_venCodigo = " + venda + " and pedTime = '" + time + "';";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
    public int getMesaBalcaoAberta(int caixa) {
        int mesa = 0;
        String sql = "select venMesa from venda where ven_caiCodigo = " + caixa + " and venStatus = 'aberta' and venMesa > 100 ;";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mesa = rs.getInt(1);

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return mesa;
    }

    public int getMaxMesa(int caixa) {
        int mesa = 0;
        String sql = "select max(venMesa) from venda where ven_caiCodigo = " + caixa + ";";
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mesa = rs.getInt(1);

            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return mesa;
    }

}
