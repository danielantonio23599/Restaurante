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

    /*  public VendaAtualBEAN listar() {
        return v.listar();
    }

    public void adicionar(VendaAtualBEAN b) {
        v.inserir(b);
    }*/
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

    public ArrayList<ProdutosGravados> listarProdutosMesa(String text) {
        //verificar se mesa esta aberta
        // if (ven.getVenda(Integer.parseInt(text)) > 0) {

        return p.produtosMesa(Integer.parseInt(text));
        /* } else {
            abrirMesa(text);
        }*/
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        return sdf.format(hora);
    }

    public void transferirProduto(int mesaOrigem, int mesaDestino, int pedido, String time) {
        int des = getVenda(mesaDestino);
        if (des == 0) {
            des = abrirMesa(mesaDestino + "");
        }
        p.transferir(getVenda(mesaOrigem), pedido, getVenda(mesaDestino), time);
    }

    public void excluirProduto(int mesaorigem, String motivo, int produto, String time) {
        p.excluirProduto(getVenda(mesaorigem), motivo, produto, time);

        ExcluzaoBEAN pro = new ExcluzaoBEAN();
        pro.setNome(ped.localizar(produto).getNome());
        pro.setMotivo(motivo);
        pro.setVenda(getVenda(mesaorigem));
        pro.setTime(Time.getTime());
        e.inserirExclusao(pro);
    }

}
