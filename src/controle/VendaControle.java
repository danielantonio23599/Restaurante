/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import modelo.ExcluzaoBEAN;
import modelo.ExcluzaoDAO;
import modelo.Mesa;
import modelo.PedidoDAO;
import modelo.PedidoVendaBEAN;
import modelo.PedidoVendaDAO;
import modelo.Produtos;
import modelo.ProdutosGravados;
import modelo.VendaAtualBEAN;
import modelo.VendaAtualDAO;
import modelo.VendaBEAN;
import modelo.VendaDAO;
import util.Time;

/**
 *
 * @author Daniel
 */
public class VendaControle {

    //private VendaAtualDAO v = new VendaAtualDAO();
    private VendaDAO ven = new VendaDAO();
    private PedidoVendaDAO p = new PedidoVendaDAO();
    private ExcluzaoControle e = new ExcluzaoControle();
    private PedidoDAO ped = new PedidoDAO();
    private SharedP_Control sc = new SharedP_Control();
    private CaixaControle cc = new CaixaControle();

    public VendaBEAN listarVenda(int mesa) {
        return ven.listarVenda(getVenda(mesa));
    }

    public void atualizaVenda(VendaBEAN v) {
        ven.atualizaVenda(v);
    }

    public int abrirMesa(VendaBEAN v) {
        return ven.abrirMesa(v);
    }

    public void adicionar(PedidoVendaBEAN venda) {
        p.adicionar(venda);
    }

    public ArrayList<ProdutosGravados> getProdutosNImpressos(int parseInt) {
        return p.OFFImpressoAll(parseInt);
    }

    public ArrayList<Mesa> getMesasAbertas() {
        return ven.listarMesasAbertas();
    }

    public int getValorMesa(int mesa) {
        int venda = getVenda(mesa);
        return ven.getValorMesa(venda);
    }

    public ArrayList<ProdutosGravados> listarProdutosMesa(String text) {
        //verificar se mesa esta aberta
        return p.produtosMesa(Integer.parseInt(text));

    }

    public void transferirMesa(String origem, String destino) {
        int des = getVenda(Integer.parseInt(destino));
        if (des == 0) {
            des = abrirMesa(destino);
        }
        p.transferirMesa(getVenda(Integer.parseInt(origem)), des);
        ven.excluir(getVenda(Integer.parseInt(origem)));
    }

    public int getVenda(int mesa) {
        return ven.getVenda(mesa);
    }

    private int abrirMesa(String mesa) {
        CaixaControle ca = new CaixaControle();
        VendaBEAN v = new VendaBEAN();
        v.setCaixa(ca.getCaixa());
        v.setCheckIn(getHoraAtual());
        v.setMesa(Integer.parseInt(mesa));
        return ven.abrirMesa(v);
    }

    private String getHoraAtual() {
        return Time.getTime();
    }

    public void transferirProduto(int mesaOrigem, int mesaDestino, int pedido, String time) {
        int des = getVenda(mesaDestino);
        if (des == 0) {
            des = abrirMesa(mesaDestino + "");
        }
        p.transferir(getVenda(mesaOrigem), pedido, getVenda(mesaDestino), time);
    }

    public void excluirProduto(int mesaorigem, String motivo, int produto, String time) {
        int venda = getVenda(mesaorigem);
        PedidoVendaBEAN pedido = p.localizar(produto, venda, time);

        ExcluzaoBEAN pro = new ExcluzaoBEAN();
        pro.setNome(ped.localizar(produto).getNome());
        pro.setMotivo(motivo);
        pro.setVenda(venda);
        pro.setObs(pedido.getDescricao());
        pro.setQuantidade(pedido.getQuantidade());
        pro.setTime(Time.getTime());
        p.excluirProduto(venda, motivo, produto, time);

        e.inserirExclusao(pro);
    }

    public int gerarMesaBalcao() {

        int mesa = p.getMesaBalcaoAberta(cc.getCaixa());
        if (mesa > 0) {
            return mesa;
        } else {
            mesa = p.getMaxMesa(cc.getCaixa());
            if (mesa > 100) {
                return ++mesa;
            } else {
                return 101;
            }
        }
    }

}
