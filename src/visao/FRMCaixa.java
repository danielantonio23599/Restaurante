/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import com.google.gson.Gson;
import controle.SharedPEmpresa_Control;
import visao.util.Mesa;

import controle.SharedP_Control;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.Caixa;
import modelo.CaixaBEAN;
import modelo.DespesaBEAN;
import modelo.ExcluzaoBEAN;
import modelo.Produtos;
import modelo.ProdutosGravados;
import modelo.SangriaBEAN;
import modelo.VendaBEAN;
import modelo.local.SharedPreferencesBEAN;
import modelo.local.SharedPreferencesEmpresaBEAN;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sync.RestauranteAPI;
import sync.SyncDefault;

import util.Time;
import visao.util.AlertAbrirCaixa;
import visao.util.AlertSangria;
import visao.util.Carregamento;

/**
 *
 * @author Daniel
 */
public class FRMCaixa extends javax.swing.JFrame {

    private DefaultTableModel dTable;
    private TableRowSorter<TableModel> tr;
    private float saldoMesa;
    private float saldo;
    /**
     * Creates new form FRMCaixa
     */
    public FRMCaixa() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        mudarTela("index");
        btnAtualizar.setMnemonic(KeyEvent.VK_F5);
        btnFecharVenda.setMnemonic(KeyEvent.VK_F9);
        btnImprimir.setMnemonic(KeyEvent.VK_F7);
        btnCancelar.setMnemonic(KeyEvent.VK_ESCAPE);

        comboProduto.grabFocus();
        comboProduto.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {

                String cadenaEscrita = comboProduto.getEditor().getItem().toString();
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        buscar(cadenaEscrita);
                        System.out.println("entrou");
                        if (comboProduto.getItemCount() > 0) {
                            comboProduto.getEditor().setItem(cadenaEscrita);
                            comboProduto.showPopup();

                        } else {
                            comboProduto.addItem(cadenaEscrita);
                        }
                        botaoPesquisar.hasFocus();
                    } catch (NumberFormatException ey) {
                    }
                }
            }

        });

    }

    private void buscar(String cadenaEscrita) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<DefaultComboBoxModel> call = api.pesquisaProdutos(sh.getEmpEmail(), sh.getEmpSenha(), cadenaEscrita);
        call.enqueue(new Callback<DefaultComboBoxModel>() {
            @Override
            public void onResponse(Call<DefaultComboBoxModel> call, Response<DefaultComboBoxModel> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        DefaultComboBoxModel u = response.body();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                comboProduto.setModel((ComboBoxModel<String>) u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<DefaultComboBoxModel> call, Throwable thrwbl) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void mudarTela(String nome) {
        CardLayout card = (CardLayout) Principal.getLayout();
        card.show(Principal, nome);
    }

    private DefaultTableModel criaTabela() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = (DefaultTableModel) tabelaDespesa.getModel();
        //retorna o DefaultTableModel
        return dTable;
    }

    private void atualizaTabela() {
        listarDespesas();
        listarDespesaDia();
    }

    public void limpaTabela() {
        dTable = criaTabela();
        while (dTable.getRowCount() > 0) {
            dTable.removeRow(0);
        }
    }

    private void preencheTabela(ArrayList<DespesaBEAN> dados) {
        limpaTabela();
        dTable = criaTabela();
        for (DespesaBEAN dado : dados) {

            dTable.addRow(new Object[]{dado.isDespesaCaixa(), dado.getCodigo(),
                dado.getNome(), dado.getDescricao(), dado.getPreco()});

        }

    }

    private DefaultTableModel criaTabelaDesDia() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = (DefaultTableModel) tabelaDespesasDia.getModel();
        //retorna o DefaultTableModel
        return dTable;
    }

    public void limpaTabelaDesDia() {
        dTable = criaTabelaDesDia();
        while (dTable.getRowCount() > 0) {
            dTable.removeRow(0);
        }
    }

    private void preencheTabelaDesDia(ArrayList<DespesaBEAN> dados) {
        dTable = criaTabela();
        for (DespesaBEAN dado : dados) {

            dTable.addRow(new Object[]{dado.isDespesaCaixa(), dado.getCodigo(),
                dado.getNome(), dado.getDescricao(), dado.getPreco()});

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pagamento = new javax.swing.ButtonGroup();
        valorgrupo = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        btnRelatorios = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        btnReducaoZ = new javax.swing.JButton();
        btnLocalizar1 = new javax.swing.JButton();
        btnDespesas = new javax.swing.JButton();
        btnSangria = new javax.swing.JButton();
        btnAdicionar1 = new javax.swing.JButton();
        btnExcluir1 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        Principal = new javax.swing.JPanel();
        FecharMesa = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jtfMesa = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        comboPagamento = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jtfTotal = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jtfTotalD = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        radioAvista = new javax.swing.JRadioButton();
        radioParcelado = new javax.swing.JRadioButton();
        radioAprazo = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        radioTotal = new javax.swing.JRadioButton();
        radioTotalD = new javax.swing.JRadioButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabelaProdutosF = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tabelaProdutosCancelados = new javax.swing.JTable();
        Home = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel353 = new javax.swing.JLabel();
        labNumMesa = new javax.swing.JLabel();
        jLabel355 = new javax.swing.JLabel();
        jLabel356 = new javax.swing.JLabel();
        labCliente = new javax.swing.JLabel();
        labGarcom = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaProdutos = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        Close = new javax.swing.JPanel();
        jPanel118 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaProduClose = new javax.swing.JTable();
        jPanel119 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaDesClose = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtfFaturamento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtfDespesas = new javax.swing.JTextField();
        jtfSangria = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtfFaturamentoLiquido = new javax.swing.JTextField();
        jLabel357 = new javax.swing.JLabel();
        jtfTroco = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jtfNota2 = new javax.swing.JTextField();
        jtfNota100 = new javax.swing.JTextField();
        jtfNota50 = new javax.swing.JTextField();
        jtfNota10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jtfMoeda10 = new javax.swing.JTextField();
        jtfMoeda50 = new javax.swing.JTextField();
        jtfMoeda5 = new javax.swing.JTextField();
        jtfMoeda25 = new javax.swing.JTextField();
        jtfMoeda1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jtfTotalTroco = new javax.swing.JTextField();
        jtfNota5 = new javax.swing.JTextField();
        jtfNota20 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        buttonFinalizar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        lbSaldoCaixa = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        index = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        VendaBalcao = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jtfTotalFiscal = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jtfNunMesa = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jtfVenda = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        botaoPesquisar = new javax.swing.JButton();
        comboProduto = new javax.swing.JComboBox<>();
        btnFecharVenda = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelaProdutosBalcao = new javax.swing.JTable();
        btnAtualizar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Relatorio = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jtfCaixa = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        lbSaldo = new javax.swing.JLabel();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        rolagem = new javax.swing.JScrollPane();
        rola = new javax.swing.JPanel();
        painelvendas = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tabelaVendaFinalizada = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        lbTotalVendido = new javax.swing.JLabel();
        painelcancelados = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tabelaProCancelados = new javax.swing.JTable();
        painelcancelados1 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tabelaSangria = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        lbTotalSangrias = new javax.swing.JLabel();
        painelcancelados2 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tabelaDespesas = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        lbTotalDespesas = new javax.swing.JLabel();
        despesas = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tabelaDespesa = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tabelaDespesasDia = new javax.swing.JTable();
        jButton27 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Caixa");

        jPanel2.setBackground(new java.awt.Color(0, 153, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));

        btnRelatorios.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRelatorios.setText("RELATÓRIOS");
        btnRelatorios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatoriosActionPerformed(evt);
            }
        });

        btnAbrir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAbrir.setText("ABRIR CAIXA");
        btnAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirjButton6ActionPerformed(evt);
            }
        });

        btnFechar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFechar.setText("FECHAR CAIXA");
        btnFechar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharjButton7ActionPerformed(evt);
            }
        });

        btnReducaoZ.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnReducaoZ.setText("REDUÇÃO Z");
        btnReducaoZ.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReducaoZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReducaoZjButton8ActionPerformed(evt);
            }
        });

        btnLocalizar1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLocalizar1.setText("VENDA BALCÃO");
        btnLocalizar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLocalizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalizar1ActionPerformed(evt);
            }
        });

        btnDespesas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDespesas.setText("DESPESAS DO DIA");
        btnDespesas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDespesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDespesasActionPerformed(evt);
            }
        });

        btnSangria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSangria.setText("SANGRIA DE CAIXA");
        btnSangria.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSangria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSangriaActionPerformed(evt);
            }
        });

        btnAdicionar1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAdicionar1.setText("HOME");
        btnAdicionar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdicionar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionar1jButton6ActionPerformed(evt);
            }
        });

        btnExcluir1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnExcluir1.setText("LEITURA X");
        btnExcluir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluir1jButton8ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("CAIXA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExcluir1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAbrir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFechar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReducaoZ, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRelatorios, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLocalizar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDespesas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSangria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(btnAdicionar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(btnAdicionar1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLocalizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSangria, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExcluir1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReducaoZ, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127))
        );

        Principal.setBackground(new java.awt.Color(153, 0, 0));
        Principal.setLayout(new java.awt.CardLayout());

        FecharMesa.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel55.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel55.setText("Mesa:");

        jtfMesa.setEditable(false);
        jtfMesa.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtfMesa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMesaActionPerformed(evt);
            }
        });

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        comboPagamento.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        comboPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DINHEIRO", "CARTÃO CRÉDITO", "CARTÃO DÉBITO", "ANOTAR CONTA" }));
        comboPagamento.setAlignmentX(2.0F);
        comboPagamento.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        comboPagamento.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);

        jLabel27.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel27.setText("Opereção :");

        jLabel56.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel56.setText("Total da Compra :");

        jtfTotal.setEditable(false);
        jtfTotal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jtfTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTotalActionPerformed(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel58.setText("Total + 10% :");

        jtfTotalD.setEditable(false);
        jtfTotalD.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jtfTotalD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfTotalD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTotalDActionPerformed(evt);
            }
        });

        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel26.setText("Forma de Pagamento :");

        pagamento.add(radioAvista);
        radioAvista.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioAvista.setSelected(true);
        radioAvista.setText("À vista");
        radioAvista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioAvistaActionPerformed(evt);
            }
        });

        pagamento.add(radioParcelado);
        radioParcelado.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioParcelado.setText("Pacelado");
        radioParcelado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioParceladoActionPerformed(evt);
            }
        });

        pagamento.add(radioAprazo);
        radioAprazo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioAprazo.setText("À prazo");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel26)
                .addGap(28, 28, 28)
                .addComponent(radioAvista)
                .addGap(63, 63, 63)
                .addComponent(radioParcelado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(radioAprazo)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(radioAvista)
                    .addComponent(radioParcelado)
                    .addComponent(radioAprazo))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel23.setText("Valor :");

        valorgrupo.add(radioTotal);
        radioTotal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioTotal.setText("Total");
        radioTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioTotalMouseClicked(evt);
            }
        });
        radioTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTotalActionPerformed(evt);
            }
        });

        valorgrupo.add(radioTotalD);
        radioTotalD.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioTotalD.setSelected(true);
        radioTotalD.setText("Total + 10%");
        radioTotalD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioTotalDMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioTotal)
                .addGap(60, 60, 60)
                .addComponent(radioTotalD)
                .addGap(31, 31, 31))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(radioTotalD)
                    .addComponent(radioTotal))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jButton19.setBackground(new java.awt.Color(51, 102, 255));
        jButton19.setText("Emitir recibo Simples");

        jButton20.setBackground(new java.awt.Color(51, 102, 255));
        jButton20.setText("Emitir NFC-e");

        jButton21.setBackground(new java.awt.Color(0, 102, 51));
        jButton21.setText("Finalizar e Sair");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(204, 0, 0));
        jButton22.setText("Cancelar");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel24.setText("Total a Pagar :");

        lbTotal.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTotal.setText("1,00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addComponent(lbTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTotal))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel58)
                            .addComponent(jLabel56)
                            .addComponent(jLabel27))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfTotal)
                            .addComponent(jtfTotalD)
                            .addComponent(comboPagamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56)
                    .addComponent(jtfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58)
                    .addComponent(jtfTotalD, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(comboPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produtos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaProdutosF.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaProdutosF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tabelaProdutosF);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(204, 204, 204));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produtos Cancelados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaProdutosCancelados.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaProdutosCancelados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaProdutosCancelados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaProdutosCanceladosMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tabelaProdutosCancelados);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addGap(18, 18, 18)
                        .addComponent(jtfMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(jtfMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout FecharMesaLayout = new javax.swing.GroupLayout(FecharMesa);
        FecharMesa.setLayout(FecharMesaLayout);
        FecharMesaLayout.setHorizontalGroup(
            FecharMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FecharMesaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        FecharMesaLayout.setVerticalGroup(
            FecharMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FecharMesaLayout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Principal.add(FecharMesa, "mesa");

        Home.setBackground(new java.awt.Color(204, 204, 204));

        jPanel16.setBackground(new java.awt.Color(204, 204, 204));

        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jButton13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton13.setText("Fechar Mesa");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton14.setText("Relatório de Mesa");

        jButton15.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton15.setText("Atualizar Mesas");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel353.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel353.setText("N°:");

        labNumMesa.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        labNumMesa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labNumMesa.setText("0");

        jLabel355.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel355.setText("Cliente :");

        jLabel356.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel356.setText("Garçom:");

        labCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labCliente.setText("Fulano");

        labGarcom.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labGarcom.setText("Siclano");

        tabelaProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nome", "Preço", "Quantidade", "Hora"
            }
        ));
        jScrollPane3.setViewportView(tabelaProdutos);

        jButton17.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton17.setText("Cup.Finscal");

        jButton18.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton18.setText("Transferir de Mesa");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel353, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labNumMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel355)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel356)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labGarcom, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel355)
                            .addComponent(labCliente))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel356)
                            .addComponent(labGarcom)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel353, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labNumMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout HomeLayout = new javax.swing.GroupLayout(Home);
        Home.setLayout(HomeLayout);
        HomeLayout.setHorizontalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 1059, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(207, 207, 207))
        );
        HomeLayout.setVerticalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );

        Principal.add(Home, "home");

        jPanel118.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produtos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaProduClose.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelaProduClose);

        javax.swing.GroupLayout jPanel118Layout = new javax.swing.GroupLayout(jPanel118);
        jPanel118.setLayout(jPanel118Layout);
        jPanel118Layout.setHorizontalGroup(
            jPanel118Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel118Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel118Layout.setVerticalGroup(
            jPanel118Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel118Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel119.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaDesClose.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tabelaDesClose);

        javax.swing.GroupLayout jPanel119Layout = new javax.swing.GroupLayout(jPanel119);
        jPanel119.setLayout(jPanel119Layout);
        jPanel119Layout.setHorizontalGroup(
            jPanel119Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel119Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel119Layout.setVerticalGroup(
            jPanel119Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel119Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 153, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel2.setText("Despesas:");

        jtfFaturamento.setEditable(false);
        jtfFaturamento.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel3.setText("Faturamento:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel4.setText("Sangria:");

        jtfDespesas.setEditable(false);
        jtfDespesas.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jtfSangria.setEditable(false);
        jtfSangria.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtfFaturamento, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jtfSangria, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jLabel2)
                .addGap(44, 44, 44)
                .addComponent(jtfDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4)
                        .addComponent(jtfDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfSangria, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfFaturamento, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Líquido:");

        jtfFaturamentoLiquido.setEditable(false);
        jtfFaturamentoLiquido.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jtfFaturamentoLiquido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFaturamentoLiquidoActionPerformed(evt);
            }
        });
        jtfFaturamentoLiquido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfFaturamentoLiquidoKeyTyped(evt);
            }
        });

        jLabel357.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel357.setText("Troco:");

        jtfTroco.setEditable(false);
        jtfTroco.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jtfTroco.setText("0");
        jtfTroco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfTrocoKeyTyped(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Troco"));

        jLabel1.setText("Notas 2:");

        jLabel7.setText("Notas 5:");

        jLabel8.setText("Notas 10:");

        jLabel9.setText("Notas 20:");

        jLabel10.setText("Notas 50:");

        jLabel11.setText("Notas 100:");

        jtfNota2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNota2.setText("0");
        jtfNota2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNota2ActionPerformed(evt);
            }
        });
        jtfNota2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfNota2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfNota100.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNota100.setText("0");
        jtfNota100.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfNota100KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfNota50.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNota50.setText("0");
        jtfNota50.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfNota50KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfNota10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNota10.setText("0");
        jtfNota10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfNota10KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jLabel12.setText("Moeda de 5:");

        jLabel13.setText("Moeda de 25:");

        jLabel14.setText("Moeda de 50:");

        jLabel15.setText("Moeda de 10:");

        jLabel16.setText("Moeda de 1:");

        jtfMoeda10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfMoeda10.setText("0");
        jtfMoeda10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMoeda10KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfMoeda50.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfMoeda50.setText("0");
        jtfMoeda50.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMoeda50KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfMoeda5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfMoeda5.setText("0");
        jtfMoeda5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfMoeda25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfMoeda25.setText("0");
        jtfMoeda25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMoeda25KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jtfMoeda1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfMoeda1.setText("0");
        jtfMoeda1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMoeda1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfMoeda5KeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Total de Troco:");

        jtfTotalTroco.setEditable(false);
        jtfTotalTroco.setText("0");
        jtfTotalTroco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfTotalTrocoKeyTyped(evt);
            }
        });

        jtfNota5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNota5.setText("0");
        jtfNota5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNota5ActionPerformed(evt);
            }
        });
        jtfNota5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfNota5KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfNota5jtfMoeda5KeyTyped(evt);
            }
        });

        jtfNota20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNota20.setText("0");
        jtfNota20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNota20ActionPerformed(evt);
            }
        });
        jtfNota20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfNota20KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfNota20jtfMoeda5KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfMoeda1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jtfNota50, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                    .addComponent(jtfNota10, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtfNota2, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfMoeda25))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfMoeda5)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfMoeda50))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfMoeda10))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jtfNota5))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(0, 0, 0)
                                    .addComponent(jtfNota100, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtfNota20)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(13, 13, 13)
                        .addComponent(jtfTotalTroco)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jtfNota2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfNota5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jtfNota10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfNota20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jtfNota100, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfNota50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15)
                    .addComponent(jtfMoeda10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfMoeda5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(jtfMoeda50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfMoeda25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jtfMoeda1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jtfTotalTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        jLabel19.setText("Status");

        buttonFinalizar.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        buttonFinalizar.setText("Finalizar");
        buttonFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFinalizarActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jButton3.setText("Canselar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel24.setBackground(new java.awt.Color(204, 204, 204));

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel35.setText("Saldo do Caixa : R$");

        lbSaldoCaixa.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbSaldoCaixa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbSaldoCaixa.setText("1,00");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbSaldoCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(642, 642, 642))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbSaldoCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel25.setBackground(new java.awt.Color(0, 153, 102));
        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout CloseLayout = new javax.swing.GroupLayout(Close);
        Close.setLayout(CloseLayout);
        CloseLayout.setHorizontalGroup(
            CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CloseLayout.createSequentialGroup()
                .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(CloseLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel118, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel119, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CloseLayout.createSequentialGroup()
                        .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel357))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtfFaturamentoLiquido, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(jtfTroco, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(CloseLayout.createSequentialGroup()
                        .addComponent(buttonFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CloseLayout.setVerticalGroup(
            CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CloseLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CloseLayout.createSequentialGroup()
                        .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CloseLayout.createSequentialGroup()
                        .addComponent(jPanel119, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jtfFaturamentoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel357)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Principal.add(Close, "close");

        index.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logo_beta_M.jpg"))); // NOI18N

        javax.swing.GroupLayout indexLayout = new javax.swing.GroupLayout(index);
        index.setLayout(indexLayout);
        indexLayout.setHorizontalGroup(
            indexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 1276, Short.MAX_VALUE)
        );
        indexLayout.setVerticalGroup(
            indexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexLayout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Principal.add(index, "index");

        VendaBalcao.setBackground(new java.awt.Color(204, 204, 204));

        jPanel12.setBackground(new java.awt.Color(0, 153, 102));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel40.setText("PEDIDO DE VENDA :");

        jtfTotalFiscal.setEditable(false);
        jtfTotalFiscal.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfTotalFiscal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfTotalFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTotalFiscalActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel42.setText("TOTAL :");

        jtfNunMesa.setEditable(false);
        jtfNunMesa.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfNunMesa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfNunMesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNunMesaActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel44.setText("VENDA :");

        jtfVenda.setEditable(false);
        jtfVenda.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfVenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfVendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel40)
                .addGap(18, 18, 18)
                .addComponent(jtfNunMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel42)
                .addGap(18, 18, 18)
                .addComponent(jtfTotalFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jtfVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfNunMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel42)
                    .addComponent(jtfTotalFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 162, Short.MAX_VALUE))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Adcionar Produto"));

        botaoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loupe.png"))); // NOI18N
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });

        comboProduto.setEditable(true);
        comboProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboProdutoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboProduto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(botaoPesquisar)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botaoPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        btnFecharVenda.setBackground(new java.awt.Color(0, 153, 0));
        btnFecharVenda.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFecharVenda.setText("FECHAR VENDA-(ALT+F9)");
        btnFecharVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharVendaActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCancelar.setText("CANCELAR-(ALT+ESC)");

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produtos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaProdutosBalcao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Quantidade", "Valor", "Hora"
            }
        ));
        jScrollPane5.setViewportView(tabelaProdutosBalcao);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnAtualizar.setBackground(new java.awt.Color(0, 153, 0));
        btnAtualizar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAtualizar.setText("ATUALIZAR - (ALT+F5)");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        btnImprimir.setBackground(new java.awt.Color(0, 153, 0));
        btnImprimir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnImprimir.setText("IMPRIMIR-(ALT+F7)");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logotipo4.jpg"))); // NOI18N
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout VendaBalcaoLayout = new javax.swing.GroupLayout(VendaBalcao);
        VendaBalcao.setLayout(VendaBalcaoLayout);
        VendaBalcaoLayout.setHorizontalGroup(
            VendaBalcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, VendaBalcaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VendaBalcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(VendaBalcaoLayout.createSequentialGroup()
                        .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnFecharVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VendaBalcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(VendaBalcaoLayout.createSequentialGroup()
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 238, Short.MAX_VALUE))
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        VendaBalcaoLayout.setVerticalGroup(
            VendaBalcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VendaBalcaoLayout.createSequentialGroup()
                .addGroup(VendaBalcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFecharVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(VendaBalcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(VendaBalcaoLayout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Principal.add(VendaBalcao, "fiscal");

        jLabel57.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel57.setText("Caixa");

        jtfCaixa.setEditable(false);
        jtfCaixa.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtfCaixa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCaixaActionPerformed(evt);
            }
        });

        jPanel23.setBackground(new java.awt.Color(204, 204, 204));

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel34.setText("Saldo do Caixa : R$");

        lbSaldo.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        lbSaldo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbSaldo.setText("1,00");

        jButton25.setBackground(new java.awt.Color(0, 102, 51));
        jButton25.setText("IMPRIMIR");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setBackground(new java.awt.Color(204, 0, 0));
        jButton26.setText("Cancelar");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(241, 241, 241))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(0, 153, 102));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1054, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        rolagem.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        painelvendas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vendas Finalizadas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaVendaFinalizada.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaVendaFinalizada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane12.setViewportView(tabelaVendaFinalizada);

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel22.setText("Total:");

        lbTotalVendido.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lbTotalVendido.setText("00");

        javax.swing.GroupLayout painelvendasLayout = new javax.swing.GroupLayout(painelvendas);
        painelvendas.setLayout(painelvendasLayout);
        painelvendasLayout.setHorizontalGroup(
            painelvendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelvendasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelvendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addGroup(painelvendasLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbTotalVendido, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        painelvendasLayout.setVerticalGroup(
            painelvendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelvendasLayout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelvendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTotalVendido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        painelcancelados.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pedidos Cancelados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaProCancelados.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaProCancelados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaProCancelados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaProCanceladosMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tabelaProCancelados);

        javax.swing.GroupLayout painelcanceladosLayout = new javax.swing.GroupLayout(painelcancelados);
        painelcancelados.setLayout(painelcanceladosLayout);
        painelcanceladosLayout.setHorizontalGroup(
            painelcanceladosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelcanceladosLayout.createSequentialGroup()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );
        painelcanceladosLayout.setVerticalGroup(
            painelcanceladosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelcanceladosLayout.createSequentialGroup()
                .addComponent(jScrollPane13)
                .addContainerGap())
        );

        painelcancelados1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sagrias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaSangria.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaSangria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaSangria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaSangriaMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tabelaSangria);

        jLabel28.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel28.setText("Total:");

        lbTotalSangrias.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lbTotalSangrias.setText("00");

        javax.swing.GroupLayout painelcancelados1Layout = new javax.swing.GroupLayout(painelcancelados1);
        painelcancelados1.setLayout(painelcancelados1Layout);
        painelcancelados1Layout.setHorizontalGroup(
            painelcancelados1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelcancelados1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelcancelados1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelcancelados1Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbTotalSangrias, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelcancelados1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        painelcancelados1Layout.setVerticalGroup(
            painelcancelados1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelcancelados1Layout.createSequentialGroup()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelcancelados1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTotalSangrias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        painelcancelados2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaDespesas.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaDespesas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaDespesas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaDespesasMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tabelaDespesas);

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel25.setText("Total:");

        lbTotalDespesas.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lbTotalDespesas.setText("00");

        javax.swing.GroupLayout painelcancelados2Layout = new javax.swing.GroupLayout(painelcancelados2);
        painelcancelados2.setLayout(painelcancelados2Layout);
        painelcancelados2Layout.setHorizontalGroup(
            painelcancelados2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelcancelados2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelcancelados2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelcancelados2Layout.createSequentialGroup()
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelcancelados2Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbTotalDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        painelcancelados2Layout.setVerticalGroup(
            painelcancelados2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelcancelados2Layout.createSequentialGroup()
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelcancelados2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbTotalDespesas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout rolaLayout = new javax.swing.GroupLayout(rola);
        rola.setLayout(rolaLayout);
        rolaLayout.setHorizontalGroup(
            rolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rolaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelvendas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(painelcancelados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(rolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painelcancelados2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(painelcancelados1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        rolaLayout.setVerticalGroup(
            rolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rolaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(painelvendas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(rolaLayout.createSequentialGroup()
                        .addComponent(painelcancelados2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(painelcancelados1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(painelcancelados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(235, Short.MAX_VALUE))
        );

        rolagem.setViewportView(rola);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addGap(18, 18, 18)
                        .addComponent(jtfCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(rolagem, javax.swing.GroupLayout.PREFERRED_SIZE, 1027, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(jtfCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(rolagem, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout RelatorioLayout = new javax.swing.GroupLayout(Relatorio);
        Relatorio.setLayout(RelatorioLayout);
        RelatorioLayout.setHorizontalGroup(
            RelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RelatorioLayout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 220, Short.MAX_VALUE))
        );
        RelatorioLayout.setVerticalGroup(
            RelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Principal.add(Relatorio, "Relatorio");

        jPanel15.setBackground(new java.awt.Color(0, 153, 102));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1274, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaDespesa.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        tabelaDespesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "incluir", "Codigo", "Nome", "Descrição", "Preço"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaDespesa.setEditingColumn(0);
        tabelaDespesa.setEditingRow(0);
        tabelaDespesa.setRowHeight(25);
        tabelaDespesa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabelaDespesa2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabelaDespesa2FocusLost(evt);
            }
        });
        tabelaDespesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaDespesa2MouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tabelaDespesa);
        if (tabelaDespesa.getColumnModel().getColumnCount() > 0) {
            tabelaDespesa.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabelaDespesa.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton11.setBackground(new java.awt.Color(0, 153, 0));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton11.setText("CADASTRAR");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 153, 0));
        jButton12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton12.setText("INCLUIR");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton23.setBackground(new java.awt.Color(0, 153, 0));
        jButton23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton23.setText("RETIRAR");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(204, 0, 0));
        jButton24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton24.setText("EXCLUIR");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas Do Dia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        tabelaDespesasDia.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        tabelaDespesasDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "incluir", "Codigo", "Nome", "Descrição", "Preço"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaDespesasDia.setEditingColumn(0);
        tabelaDespesasDia.setEditingRow(0);
        tabelaDespesasDia.setRowHeight(25);
        tabelaDespesasDia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabelaDespesasDia2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabelaDespesasDia2FocusLost(evt);
            }
        });
        tabelaDespesasDia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaDespesasDia2MouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tabelaDespesasDia);
        if (tabelaDespesasDia.getColumnModel().getColumnCount() > 0) {
            tabelaDespesasDia.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabelaDespesasDia.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton27.setBackground(new java.awt.Color(0, 153, 0));
        jButton27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton27.setText("ATUALIZAR");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout despesasLayout = new javax.swing.GroupLayout(despesas);
        despesas.setLayout(despesasLayout);
        despesasLayout.setHorizontalGroup(
            despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(despesasLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(despesasLayout.createSequentialGroup()
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(despesasLayout.createSequentialGroup()
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(280, 280, Short.MAX_VALUE))
            .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        despesasLayout.setVerticalGroup(
            despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, despesasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(175, 175, 175))
            .addGroup(despesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, despesasLayout.createSequentialGroup()
                    .addGap(0, 645, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        Principal.add(despesas, "despesas");

        jMenu1.setText("Arquivo");

        jMenuItem1.setText("Trocar Usuario");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/remove-symbol (2).png"))); // NOI18N
        jMenuItem2.setText("Voltar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem9.setText("Sair");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Caixa");

        jMenuItem3.setText("Atualizar");
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        mudarTela("Relatorio");
        atualizaRelatorio();
    }//GEN-LAST:event_btnRelatoriosActionPerformed

    private void btnAbrirjButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirjButton6ActionPerformed
        AlertAbrirCaixa a = new AlertAbrirCaixa();
        a.setC(this);
        a.setVisible(true);

    }//GEN-LAST:event_btnAbrirjButton6ActionPerformed


    private void btnFecharjButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharjButton7ActionPerformed
        getMesasAbertas();
    }//GEN-LAST:event_btnFecharjButton7ActionPerformed

    private void btnReducaoZjButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReducaoZjButton8ActionPerformed

    }//GEN-LAST:event_btnReducaoZjButton8ActionPerformed

    private void btnLocalizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocalizar1ActionPerformed
        mudarTela("fiscal");
        gerarMesaBalcao();


    }//GEN-LAST:event_btnLocalizar1ActionPerformed

    private void btnDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespesasActionPerformed
        mudarTela("despesas");
        atualizaTabela();
    }//GEN-LAST:event_btnDespesasActionPerformed

    private void btnSangriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSangriaActionPerformed
        float sangria = 0;
        AlertSangria a = new AlertSangria();
        a.setC(this);
        a.setVisible(true);


    }//GEN-LAST:event_btnSangriaActionPerformed

    private void jtfTotalFiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalFiscalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalFiscalActionPerformed

    private void jtfNunMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNunMesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNunMesaActionPerformed

    private void jtfMesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfMesaActionPerformed

    private void jtfNota2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNota2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota2ActionPerformed

    private void btnAdicionar1jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionar1jButton6ActionPerformed
        atualizaMesas();
    }//GEN-LAST:event_btnAdicionar1jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jtfFaturamentoLiquidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFaturamentoLiquidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfFaturamentoLiquidoActionPerformed

    private void btnExcluir1jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluir1jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluir1jButton8ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        atualizaMesas();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (!labNumMesa.getText().equals("0")) {
            jtfMesa.setText(labNumMesa.getText());
            mudarTela("mesa");
            somarTotal();
        }

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        if (!labNumMesa.getText().equals("0")) {
            String mesa = JOptionPane.showInputDialog("Insira o n° da mesa");
            Carregamento a = new Carregamento(this, true);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    a.setVisible(true);

                }
            });
            SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
            RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
            final Call<Void> call = api.transferiMesa(mesa, labNumMesa.getText(), sh.getEmpEmail(), sh.getEmpSenha());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    System.out.println(response.isSuccessful());
                    if (response.isSuccessful()) {
                        String auth = response.headers().get("auth");
                        if (auth.equals("1")) {
                            System.out.println("Login correto");

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    a.setVisible(false);
                                    atualizaMesas();
                                    //atualizarMesas
                                }
                            });

                        } else {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    a.setVisible(false);
                                }
                            });
                            System.out.println("Login incorreto");
                            // senha ou usuario incorreto

                        }
                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto- fora do ar");
                        //servidor fora do ar
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                }
            });

        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma Mesa");
        }

    }//GEN-LAST:event_jButton18ActionPerformed

    private void jtfVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfVendaActionPerformed

    private void radioAvistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioAvistaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioAvistaActionPerformed

    private void radioParceladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioParceladoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioParceladoActionPerformed

    private void jtfTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalActionPerformed

    private void jtfTotalDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalDActionPerformed

    private void radioTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioTotalActionPerformed

    private void radioTotalDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioTotalDMouseClicked
        if (radioTotalD.isSelected()) {
            lbTotal.setText(jtfTotalD.getText());
        } else {
            lbTotal.setText(jtfTotal.getText());
        }
    }//GEN-LAST:event_radioTotalDMouseClicked

    private void radioTotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioTotalMouseClicked
        if (radioTotalD.isSelected()) {
            lbTotal.setText(jtfTotalD.getText());
        } else {
            lbTotal.setText(jtfTotal.getText());
        }
    }//GEN-LAST:event_radioTotalMouseClicked

    private void tabelaProdutosCanceladosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProdutosCanceladosMouseClicked
        String cod = tabelaProdutosCancelados.getValueAt(tabelaProdutosCancelados.getSelectedRow(), 0) + "";
    }//GEN-LAST:event_tabelaProdutosCanceladosMouseClicked

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // atualizaMesas();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        String message = "Deseja realmente sair do sistema?";
        String title = "Confirmação";
//Exibe caixa de dialogo (veja figura) solicitando confirmação ou não. 
//Se o usuário clicar em "Sim" retorna 0 pra variavel reply, se informado não retorna 1
        int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        String message = "Deseja realmente fechar a mesa?";
        String title = "Confirmação";
        int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            atualizaVenda();
            atualizaMesas();
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        pesquisarProdutos();
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        buscarProdutosMesa(jtfNunMesa.getText());
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void comboProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboProdutoKeyPressed
        System.out.println("entrou");
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("entrou enter");
            pesquisarProdutos();
        }
    }//GEN-LAST:event_comboProdutoKeyPressed

    private void btnFecharVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharVendaActionPerformed
        if (!jtfVenda.getText().equals("")) {
            jtfMesa.setText(jtfNunMesa.getText());
            mudarTela("mesa");
            somarTotal();
            atualizaProdutos(Integer.parseInt(jtfNunMesa.getText()));
        } else {
            JOptionPane.showMessageDialog(null, "Insira algum produto, o casa já inserido, atualize a venda !");
        }

    }//GEN-LAST:event_btnFecharVendaActionPerformed

    private void jtfCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfCaixaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfCaixaActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26ActionPerformed

    private void tabelaProCanceladosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProCanceladosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaProCanceladosMouseClicked

    private void tabelaSangriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaSangriaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaSangriaMouseClicked

    private void tabelaDespesasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaDespesasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaDespesasMouseClicked

    private void jtfMoeda5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMoeda5KeyTyped
        bloqueaLetras(evt);
        atualizaTroco();

    }//GEN-LAST:event_jtfMoeda5KeyTyped

    private void buttonFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonFinalizarActionPerformed

        String message = "Deseja realmente fechar o caixa ?";
        String title = "Confirmação";
//Exibe caixa de dialogo (veja figura) solicitando confirmação ou não. 
//Se o usuário clicar em "Sim" retorna 0 pra variavel reply, se informado não retorna 1
        int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            if (!jtfFaturamentoLiquido.getText().equals("") || !jtfTroco.getText().equals("")) {
                if (Float.parseFloat(jtfFaturamentoLiquido.getText()) == Float.parseFloat(jtfTroco.getText())) {

                    Carregamento a = new Carregamento(this, true);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {

                            a.setVisible(true);

                        }
                    });
                    SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
                    RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
                    final Call<Void> call = api.fecharCaixa("" + Float.parseFloat(jtfTroco.getText()), sh.getEmpEmail(), sh.getEmpSenha());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            System.out.println(response.isSuccessful());
                            if (response.isSuccessful()) {
                                String auth = response.headers().get("auth");
                                if (auth.equals("1")) {
                                    System.out.println("Login correto");

                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                            atualizaTabelaprodusClose();

                                            a.setVisible(false);

                                        }
                                    });

                                } else {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            a.setVisible(false);
                                        }
                                    });
                                    System.out.println("Login incorreto");
                                    // senha ou usuario incorreto

                                }
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        a.setVisible(false);
                                    }
                                });
                                System.out.println("Login incorreto- fora do ar");
                                //servidor fora do ar
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    a.setVisible(false);
                                }
                            });
                        }
                    });

                } else {
                    JOptionPane.showMessageDialog(null, "Verifique seu troco, incompativel com o seu dinheiro em caixa!!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!!");
        }
    }//GEN-LAST:event_buttonFinalizarActionPerformed

    private void jtfNota5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNota5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota5ActionPerformed

    private void jtfNota5jtfMoeda5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota5jtfMoeda5KeyTyped
        bloqueaLetras(evt);
        atualizaTroco();
    }//GEN-LAST:event_jtfNota5jtfMoeda5KeyTyped

    private void jtfNota20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNota20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota20ActionPerformed

    private void jtfNota20jtfMoeda5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota20jtfMoeda5KeyTyped
        bloqueaLetras(evt);
        atualizaTroco();
    }//GEN-LAST:event_jtfNota20jtfMoeda5KeyTyped

    private void tabelaDespesasDia2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaDespesasDia2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaDespesasDia2MouseClicked

    private void tabelaDespesasDia2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabelaDespesasDia2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaDespesasDia2FocusLost

    private void tabelaDespesasDia2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabelaDespesasDia2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaDespesasDia2FocusGained

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        ArrayList<DespesaBEAN> des = getDespesas();
        getSaldoAtualCaixa();
        float total = 0;
        if (des.size() > 0) {
            for (DespesaBEAN de : des) {
                total += de.getPreco();
            }
            if (total <= saldo) {
                adicionarDespesaDia(des);
            } else {
                JOptionPane.showMessageDialog(null, "Saldo INSSUFICIENTE!!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma despesa selecionada!!");
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        FRMDespesa d = new FRMDespesa();
        d.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void tabelaDespesa2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaDespesa2MouseClicked
        // excluir despesa dia
    }//GEN-LAST:event_tabelaDespesa2MouseClicked

    private void tabelaDespesa2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabelaDespesa2FocusLost

    }//GEN-LAST:event_tabelaDespesa2FocusLost

    private void tabelaDespesa2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabelaDespesa2FocusGained

    }//GEN-LAST:event_tabelaDespesa2FocusGained

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        ArrayList<DespesaBEAN> des = getDespesas();
        if (des.size() > 0) {
            excluirDespesa(des);
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma despesa selecionada!!");
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        ArrayList<DespesaBEAN> des = getDespesasDoDia();
        if (des.size() > 0) {
            retirarDespesa(des);
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma despesa selecionada!!");
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        listarDespesaDia();
        listarDespesas();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jtfNota2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfNota5.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota2KeyPressed

    private void jtfNota5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfNota10.requestFocus();
        }
    }//GEN-LAST:event_jtfNota5KeyPressed

    private void jtfNota10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota10KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfNota20.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota10KeyPressed

    private void jtfNota20KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota20KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfNota50.requestFocus();
        }
    }//GEN-LAST:event_jtfNota20KeyPressed

    private void jtfNota50KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota50KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfNota100.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota50KeyPressed

    private void jtfNota100KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfNota100KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfMoeda5.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNota100KeyPressed

    private void jtfMoeda5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMoeda5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfMoeda10.requestFocus();
        }
    }//GEN-LAST:event_jtfMoeda5KeyPressed

    private void jtfMoeda10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMoeda10KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfMoeda25.requestFocus();
        }
    }//GEN-LAST:event_jtfMoeda10KeyPressed

    private void jtfMoeda25KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMoeda25KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfMoeda50.requestFocus();
        }
    }//GEN-LAST:event_jtfMoeda25KeyPressed

    private void jtfMoeda50KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMoeda50KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfMoeda1.requestFocus();
        }
    }//GEN-LAST:event_jtfMoeda50KeyPressed

    private void jtfMoeda1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMoeda1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buttonFinalizar.requestFocus();
        }
    }//GEN-LAST:event_jtfMoeda1KeyPressed

    private void jtfFaturamentoLiquidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFaturamentoLiquidoKeyTyped
        bloqueaLetras(evt);
    }//GEN-LAST:event_jtfFaturamentoLiquidoKeyTyped

    private void jtfTrocoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfTrocoKeyTyped
        bloqueaLetras(evt);
    }//GEN-LAST:event_jtfTrocoKeyTyped

    private void jtfTotalTrocoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfTotalTrocoKeyTyped
        bloqueaLetras(evt);
    }//GEN-LAST:event_jtfTotalTrocoKeyTyped
    private void bloqueaLetras(java.awt.event.KeyEvent evt) {
        String caracteres = "0987654321";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FRMCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FRMCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FRMCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FRMCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FRMCaixa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Close;
    private javax.swing.JPanel FecharMesa;
    private javax.swing.JPanel Home;
    private javax.swing.JPanel Principal;
    private javax.swing.JPanel Relatorio;
    private javax.swing.JPanel VendaBalcao;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnAdicionar1;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDespesas;
    private javax.swing.JButton btnExcluir1;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnFecharVenda;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLocalizar1;
    private javax.swing.JButton btnReducaoZ;
    private javax.swing.JButton btnRelatorios;
    private javax.swing.JButton btnSangria;
    private javax.swing.JButton buttonFinalizar;
    private javax.swing.JComboBox<String> comboPagamento;
    private javax.swing.JComboBox<String> comboProduto;
    private javax.swing.JPanel despesas;
    private javax.swing.JPanel index;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel353;
    private javax.swing.JLabel jLabel355;
    private javax.swing.JLabel jLabel356;
    private javax.swing.JLabel jLabel357;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel118;
    private javax.swing.JPanel jPanel119;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jtfCaixa;
    private javax.swing.JTextField jtfDespesas;
    private javax.swing.JTextField jtfFaturamento;
    private javax.swing.JTextField jtfFaturamentoLiquido;
    private javax.swing.JTextField jtfMesa;
    private javax.swing.JTextField jtfMoeda1;
    private javax.swing.JTextField jtfMoeda10;
    private javax.swing.JTextField jtfMoeda25;
    private javax.swing.JTextField jtfMoeda5;
    private javax.swing.JTextField jtfMoeda50;
    private javax.swing.JTextField jtfNota10;
    private javax.swing.JTextField jtfNota100;
    private javax.swing.JTextField jtfNota2;
    private javax.swing.JTextField jtfNota20;
    private javax.swing.JTextField jtfNota5;
    private javax.swing.JTextField jtfNota50;
    private javax.swing.JTextField jtfNunMesa;
    private javax.swing.JTextField jtfSangria;
    private javax.swing.JTextField jtfTotal;
    private javax.swing.JTextField jtfTotalD;
    private javax.swing.JTextField jtfTotalFiscal;
    private javax.swing.JTextField jtfTotalTroco;
    private javax.swing.JTextField jtfTroco;
    private javax.swing.JTextField jtfVenda;
    private javax.swing.JLabel labCliente;
    private javax.swing.JLabel labGarcom;
    private javax.swing.JLabel labNumMesa;
    private javax.swing.JLabel lbSaldo;
    private javax.swing.JLabel lbSaldoCaixa;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JLabel lbTotalDespesas;
    private javax.swing.JLabel lbTotalSangrias;
    private javax.swing.JLabel lbTotalVendido;
    private javax.swing.ButtonGroup pagamento;
    private javax.swing.JPanel painelcancelados;
    private javax.swing.JPanel painelcancelados1;
    private javax.swing.JPanel painelcancelados2;
    private javax.swing.JPanel painelvendas;
    private javax.swing.JRadioButton radioAprazo;
    private javax.swing.JRadioButton radioAvista;
    private javax.swing.JRadioButton radioParcelado;
    private javax.swing.JRadioButton radioTotal;
    private javax.swing.JRadioButton radioTotalD;
    private javax.swing.JPanel rola;
    private javax.swing.JScrollPane rolagem;
    private javax.swing.JTable tabelaDesClose;
    private javax.swing.JTable tabelaDespesa;
    private javax.swing.JTable tabelaDespesas;
    private javax.swing.JTable tabelaDespesasDia;
    private javax.swing.JTable tabelaProCancelados;
    private javax.swing.JTable tabelaProduClose;
    private javax.swing.JTable tabelaProdutos;
    private javax.swing.JTable tabelaProdutosBalcao;
    private javax.swing.JTable tabelaProdutosCancelados;
    private javax.swing.JTable tabelaProdutosF;
    private javax.swing.JTable tabelaSangria;
    private javax.swing.JTable tabelaVendaFinalizada;
    private javax.swing.ButtonGroup valorgrupo;
    // End of variables declaration//GEN-END:variables

    public void setNunMesa(String mesa) {
        labNumMesa.setText(mesa);
        atualizaProdutos(Integer.parseInt(mesa));
        atualizaProdutosCancelados(Integer.parseInt(mesa));
    }

    private void home() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 5));
        int controle = 100;
        for (int i = 1; i <= controle; i++) {
            Mesa v = new Mesa();
            v.setValor("00");
            v.setMesa("" + i);
            v.setCaixa(this);
            p.add(v);
        }
        jScrollPane9.add(p);
        //defini um tamanho preferido pro scrollpane
        //defini o painel de checkboxes como viewport do scrollpane
        jScrollPane9.setViewportView(p);

    }

    private void atualizaMesas() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<modelo.Mesa>> call = api.getMesasAbertas(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<modelo.Mesa>>() {
            @Override
            public void onResponse(Call<ArrayList<modelo.Mesa>> call, Response<ArrayList<modelo.Mesa>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        ArrayList<modelo.Mesa> u = response.body();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                setMesas(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<modelo.Mesa>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void setMesas(ArrayList<modelo.Mesa> m) {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 4));
        int controle = 100;
        for (int i = 1; i <= controle; i++) {
            Mesa v = new Mesa();
            v.setValor("00");
            v.setMesa("" + i);
            v.setCaixa(this);
            for (modelo.Mesa mesa : m) {
                if (mesa.getMesa() == i) {
                    v.setValor(mesa.getValor() + "");
                    v.setMesa("" + i);
                    v.setCaixa(this);
                    v.setCorPainel();
                    p.add(v);
                }
            }
            p.add(v);
        }
        jScrollPane9.add(p);
        //defini um tamanho preferido pro scrollpane
        //defini o painel de checkboxes como viewport do scrollpane
        jScrollPane9.setViewportView(p);
        mudarTela("home");
    }

    private DefaultTableModel criaTabelaProdutos() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false
            };
        };
        //retorna o DefaultTableModel
        return dTable;
    }

    public void atualizaProdutos(int mesa) {
        listarProdutosMesa(mesa);

    }

    private void preencheTabelaProdutos(ArrayList<ProdutosGravados> dados) {
        dTable = criaTabelaProdutos();
        //seta o nome das colunas da tabela
        dTable.addColumn("Código");
        dTable.addColumn("Nome");
        dTable.addColumn("Quantidade");
        dTable.addColumn("Valor");
        dTable.addColumn("Hora");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (ProdutosGravados dado : dados) {
            dTable.addRow(new Object[]{dado.getCodProduto(), dado.getNome(),
                dado.getQuantidade(), dado.getValor(), dado.getTime()});
        }
        //set o modelo da tabela
        tabelaProdutos.setModel(dTable);
        tabelaProdutosF.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProdutos.setRowSorter(tr);
        tabelaProdutosF.setRowSorter(tr);

    }

    private void preencheTabelaProdutosBalcao(ArrayList<ProdutosGravados> dados) {
        dTable = criaTabelaProdutos();
        //seta o nome das colunas da tabela
        dTable.addColumn("Código");
        dTable.addColumn("Nome");
        dTable.addColumn("Quantidade");
        dTable.addColumn("Valor");
        dTable.addColumn("Hora");
        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        if (dados != null) {
            for (ProdutosGravados dado : dados) {
                dTable.addRow(new Object[]{dado.getCodProduto(), dado.getNome(),
                    dado.getQuantidade(), dado.getValor(), dado.getTime()});
            }
        }
        //set o modelo da tabela
        tabelaProdutosBalcao.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProdutosBalcao.setRowSorter(tr);
    }

    private void somarTotal() {
        getValorMesa(Integer.parseInt(jtfMesa.getText() + ""));

    }

    //SUDO TABELA PRODUTOS EXCLUIDOS
    private DefaultTableModel criaTabelaProdutosExcluidos() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false
            };
        };
        //retorna o DefaultTableModel
        return dTable;
    }

    public void atualizaProdutosCancelados(int mesa) {
        listarExclusaoVenda(mesa);

    }

    private void preencheTabelaProdutosExcluidos(ArrayList<ExcluzaoBEAN> dados) {
        dTable = criaTabelaProdutosExcluidos();
        //seta o nome das colunas da tabela

        dTable.addColumn("Código");
        dTable.addColumn("Motivo");
        dTable.addColumn("Hora");
        dTable.addColumn("Funcionario");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (ExcluzaoBEAN dado : dados) {
            dTable.addRow(new Object[]{dado.getCodigo(), dado.getMotivo(), dado.getTime(),
                dado.getFuncionarioN()
            });
        }
        //set o modelo da tabela
        tabelaProdutosCancelados.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProdutosCancelados.setRowSorter(tr);

    }

    private void atualizaVenda() {
        VendaBEAN v = getDadosVenda();
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.atualizaVenda(new Gson().toJson(v), sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                a.setVisible(false);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private VendaBEAN getDadosVenda() {
        VendaBEAN venda = new VendaBEAN();
        venda.setMesa(Integer.parseInt(jtfMesa.getText()));
        venda.setCheckOut(Time.getTime());
        venda.setPagamento(comboPagamento.getSelectedItem() + "");
        venda.setValor(Float.parseFloat(lbTotal.getText()));
        return venda;
    }

    // VENDA A BALCA
    private void atualizar() {
        // buscar produtos e valor total
        buscarProdutosMesa(jtfNunMesa.getText());
    }

    private DefaultTableModel criaTabelaProdutosBalcao() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

            /*@Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }*/
        ;

        };
        //retorna o DefaultTableModel
    return dTable;
    }

    private void pesquisarProdutos() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Produtos> call = api.buscarUmProduto(sh.getEmpEmail(), sh.getEmpSenha(), comboProduto.getSelectedItem() + "");
        call.enqueue(new Callback<Produtos>() {
            @Override
            public void onResponse(Call<Produtos> call, Response<Produtos> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        Produtos u = response.body();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                if (u != null) {
                                    FRMRealizarVenda r = new FRMRealizarVenda();
                                    r.setDados(jtfNunMesa.getText() + "");
                                    r.setProdutos(u);
                                    r.setVisible(true);

                                } else {
                                    JOptionPane.showMessageDialog(null, "NÃO encontrado!!");
                                }
                            }

                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Produtos> call, Throwable thrwbl) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void buscarProdutosMesa(String mesa) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<ProdutosGravados>> call = api.listarProdutosMesa(sh.getEmpEmail(), sh.getEmpSenha(), mesa + "");
        call.enqueue(new Callback<ArrayList<ProdutosGravados>>() {
            @Override
            public void onResponse(Call<ArrayList<ProdutosGravados>> call, Response<ArrayList<ProdutosGravados>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<ProdutosGravados> u = response.body();
                                a.setVisible(false);
                                preencheTabelaProdutosBalcao(u);
                                if (u != null) {
                                    jtfVenda.setText(u.get(0).getCodPedidVenda() + "");
                                    float total = 0;
                                    for (ProdutosGravados p : u) {
                                        total += (p.getQuantidade() * p.getValor());
                                    }
                                    jtfTotalFiscal.setText(total + "");
                                } else if (!jtfTotalFiscal.equals("")) {
                                    limparCampoVendaBalcao();
                                    gerarMesaBalcao();
                                }
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProdutosGravados>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void limparCampoVendaBalcao() {
        jtfVenda.setText("0");
        float total = 0;
        jtfTotalFiscal.setText(total + "");
    }

    private ArrayList<DespesaBEAN> getDespesas() {
        ArrayList<DespesaBEAN> d = new ArrayList<>();
        for (int i = 0; i <= tabelaDespesa.getRowCount() - 1; i++) {
            if (Boolean.valueOf(tabelaDespesa.getValueAt(i, 0).toString()) == true) {
                DespesaBEAN des = new DespesaBEAN();
                des.setCodigo(Integer.parseInt(tabelaDespesa.getValueAt(i, 1).toString()));
                des.setNome((tabelaDespesa.getValueAt(i, 2).toString()));
                des.setDescricao((tabelaDespesa.getValueAt(i, 3).toString()));
                des.setPreco(Float.parseFloat(tabelaDespesa.getValueAt(i, 4).toString()));
                d.add(des);
            }
        }
        return d;

    }

    private ArrayList<DespesaBEAN> getDespesasDoDia() {
        ArrayList<DespesaBEAN> d = new ArrayList<>();
        for (int i = 0; i <= tabelaDespesasDia.getRowCount() - 1; i++) {
            if (Boolean.valueOf(tabelaDespesasDia.getValueAt(i, 0).toString()) == true) {
                DespesaBEAN des = new DespesaBEAN();
                des.setCodigo(Integer.parseInt(tabelaDespesasDia.getValueAt(i, 1).toString()));
                des.setNome((tabelaDespesasDia.getValueAt(i, 2).toString()));
                des.setDescricao((tabelaDespesasDia.getValueAt(i, 3).toString()));
                des.setPreco(Float.parseFloat(tabelaDespesasDia.getValueAt(i, 4).toString()));
                d.add(des);
            }
        }
        return d;

    }
//relatorio

    private void atualizaRelatorio() {
        listarPedidosCanceladosCaixa();
        listarDespesaDia();
        listarSangiaCaixa();
        listarValoresCaixa();
        listarVendasFinalizadasCaixa();

    }

    private DefaultTableModel criaTabelaVenda() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.Float.class, java.lang.Integer.class, java.lang.Integer.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

        ;

        };
        //retorna o DefaultTableModel
    return dTable;
    }

    private void preencheTabelaVenda(ArrayList<VendaBEAN> dados) {
        dTable = criaTabelaVenda();
        //seta o nome das colunas da tabela
        dTable.addColumn("Codigo");
        dTable.addColumn("Mesa");
        dTable.addColumn("CheckIN");
        dTable.addColumn("CheckOUT");
        dTable.addColumn("Valor");
        dTable.addColumn("Pagamento");
        dTable.addColumn("Caixa");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (VendaBEAN dado : dados) {
            dTable.addRow(new Object[]{dado.getCodigo(), dado.getMesa(), dado.getCheckIn(), dado.getCheckOut(),
                dado.getValor(), dado.getPagamento(), dado.getCaixa()});
        }
        //set o modelo da tabela
        tabelaVendaFinalizada.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaVendaFinalizada.setRowSorter(tr);
        float total = 0;
        for (VendaBEAN dado : dados) {
            total += dado.getValor();
        }
        lbTotalVendido.setText(total + "");

    }

    private void preencheTabelaCancelados(ArrayList<ExcluzaoBEAN> dados) {
        dTable = criaTabelaProdutosExcluidos();
        //seta o nome das colunas da tabela
        dTable.addColumn("Código");
        dTable.addColumn("Motivo");
        dTable.addColumn("Hora");
        dTable.addColumn("Funcionario");
        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (ExcluzaoBEAN dado : dados) {
            dTable.addRow(new Object[]{dado.getCodigo(), dado.getMotivo(), dado.getTime(),
                dado.getFuncionarioN()
            });
        }
        //set o modelo da tabela
        tabelaProCancelados.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProCancelados.setRowSorter(tr);

    }

    private void preencheTabelaDespesas(ArrayList<DespesaBEAN> dados) {
        dTable = criaTabelaDespesas();
        //seta o nome das colunas da tabela
        dTable.addColumn("Código");
        dTable.addColumn("Nome");
        dTable.addColumn("Descrição");
        dTable.addColumn("Valor");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (DespesaBEAN dado : dados) {
            dTable.addRow(new Object[]{dado.getCodigo(), dado.getNome(), dado.getDescricao(),
                dado.getPreco()
            });
        }
        //set o modelo da tabela
        tabelaDespesas.setModel(dTable);
        tabelaDesClose.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaDespesas.setRowSorter(tr);
        tabelaDesClose.setRowSorter(tr);
        totalDespesas(dados);

    }

    private void totalDespesas(ArrayList<DespesaBEAN> dados) {
        float total = 0;
        for (DespesaBEAN dado : dados) {
            total += dado.getPreco();
        }
        lbTotalDespesas.setText("" + total);
    }

    private void preencheTabelaDespesasDia(ArrayList<DespesaBEAN> dados) {
        limpaTabelaDesDia();
        dTable = criaTabelaDesDia();
        for (DespesaBEAN dado : dados) {

            dTable.addRow(new Object[]{dado.isDespesaCaixa(), dado.getCodigo(),
                dado.getNome(), dado.getDescricao(), dado.getPreco()});

        }
        tabelaDespesas.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaDespesas.setRowSorter(tr);
    }

    private void preencheTabelaSangria(ArrayList<SangriaBEAN> dados) {
        dTable = criaTabelaSangria();
        //seta o nome das colunas da tabela
        dTable.addColumn("Código");
        dTable.addColumn("Valor");
        dTable.addColumn("Hora");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (SangriaBEAN dado : dados) {
            dTable.addRow(new Object[]{dado.getCodigo(), dado.getValor(), dado.getTime()
            });
        }
        //set o modelo da tabela
        tabelaSangria.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaSangria.setRowSorter(tr);
        totalSangria(dados);
        if (dados.size() > 0) {
            jtfCaixa.setText(dados.get(0).getCaixa() + "");
        }

    }

    private void totalSangria(ArrayList<SangriaBEAN> dados) {
        float total = 0;
        for (SangriaBEAN dado : dados) {
            total += dado.getValor();
        }
        lbTotalSangrias.setText(total + "");
    }

    private DefaultTableModel criaTabelaSangria() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.Float.class, java.lang.String.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

        ;

        };
        //retorna o DefaultTableModel
    return dTable;
    }

    private DefaultTableModel criaTabelaDespesas() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

        ;

        };
        //retorna o DefaultTableModel
    return dTable;
    }

    private void atualizaTabelaprodusClose() {
        listarProdutosVendidosCaixa();
        listarValoresCaixa();
        listarDespesaDia();
    }

    private void preencheTabelaProdutosClose(ArrayList<ProdutosGravados> dados) {
        dTable = criaTabelaProdutosBalcao();
        //seta o nome das colunas da tabela
        dTable.addColumn("Produto");
        dTable.addColumn("Nome");
        dTable.addColumn("Quantidade");
        dTable.addColumn("Valor UN");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        for (ProdutosGravados dado : dados) {
            dTable.addRow(new Object[]{dado.getCodProduto(), dado.getNome(),
                dado.getQuantidade(), dado.getValor()});
        }
        //set o modelo da tabela
        tabelaProduClose.setModel(dTable);

        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProduClose.setRowSorter(tr);
        listarDespesaDia();

    }

    private void atualizaTroco() {
        float troco = 0;
        if (!jtfNota2.getText().equals("")) {
            troco += (2 * Integer.parseInt(jtfNota2.getText()));
        }
        if (!jtfNota5.getText().equals("")) {
            troco += (5f * Integer.parseInt(jtfNota5.getText()));
        }
        if (!jtfNota10.getText().equals("")) {
            troco += (10 * Integer.parseInt(jtfNota10.getText()));
        }
        if (!jtfNota20.getText().equals("")) {
            troco += (20f * Integer.parseInt(jtfNota20.getText()));
        }
        if (!jtfNota50.getText().equals("")) {
            troco += (50 * Integer.parseInt(jtfNota50.getText()));
        }
        if (!jtfNota100.getText().equals("")) {
            troco += (100 * Integer.parseInt(jtfNota100.getText()));
        }
        if (!jtfMoeda5.getText().equals("")) {
            troco += (Integer.parseInt(jtfMoeda5.getText()) * 0.05f);
        }
        if (!jtfMoeda10.getText().equals("")) {
            troco += (Integer.parseInt(jtfMoeda10.getText()) * 0.1f);
        }
        if (!jtfMoeda25.getText().equals("")) {
            troco += (Integer.parseInt(jtfMoeda25.getText()) * 0.25f);
        }
        if (!jtfMoeda50.getText().equals("")) {
            troco += ((Integer.parseInt(jtfMoeda50.getText())) * 0.5f);
        }
        if (!jtfMoeda1.getText().equals("")) {
            troco += Integer.parseInt(jtfMoeda1.getText());
        }
        NumberFormat df = new DecimalFormat("0.00");
        String numero = String.valueOf(df.format(troco));
        numero = numero.replace(",", ".");
        jtfTroco.setText(numero);
        jtfTotalTroco.setText(numero);
        if (Float.parseFloat(jtfFaturamentoLiquido.getText()) < troco) {
            JOptionPane.showMessageDialog(null, "Verifique seu troco, incompativel com o seu dinheiro em caixa!!");
        }
    }

    public void abrirCaixa(CaixaBEAN c) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        SharedPreferencesEmpresaBEAN sh1 = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        CaixaBEAN cc = c;
        cc.setFuncionario(sh.getFunCodigo());
        System.out.println("codigo funcionario " + sh.getFunCodigo());
        final Call<Void> call = api.abrirCaixa(new Gson().toJson(cc), sh1.getEmpEmail(), sh1.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                a.setVisible(false);
                                //atualizarMesas
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    public void getSaldoAtual(Float sangria) {

        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.saldoAtualCaixa(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                saldo = Float.parseFloat(response.headers().get("sucesso"));
                                a.setVisible(false);
                                if (saldo == -1) {
                                    JOptionPane.showMessageDialog(null, "Caixa está Fechado");
                                } else if (sangria > saldo) {
                                    JOptionPane.showMessageDialog(null, "Valor Maior que Saldo Atual");

                                } else {
                                    SangriaBEAN s = new SangriaBEAN();
                                    s.setValor(sangria);
                                    s.setTime(Time.getTime());
                                    //s.setCaixa(controleCaixa.getCaixa());
                                    int reply = JOptionPane.showConfirmDialog(null, "Deseja realmete retirar R$ " + sangria + " do caixa?", "Comfirmação", JOptionPane.YES_NO_OPTION);
                                    if (reply == JOptionPane.YES_OPTION) {
                                        inserirSangria(s);
                                        JOptionPane.showMessageDialog(null, "Operação Liberada com Sucesso!!");
                                    }
                                }

                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void inserirSangria(SangriaBEAN s) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.inserirSangria(sh.getEmpEmail(), sh.getEmpSenha(), new Gson().toJson(s));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                a.setVisible(false);
                                //atualizarMesas
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void listarProdutosVendidosCaixa() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<ProdutosGravados>> call = api.listarProdutosVendidos(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<ProdutosGravados>>() {
            @Override
            public void onResponse(Call<ArrayList<ProdutosGravados>> call, Response<ArrayList<ProdutosGravados>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<ProdutosGravados> u = response.body();
                                a.setVisible(false);
                                preencheTabelaProdutosClose(u);
                                //atualizarMesas
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProdutosGravados>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void listarDespesaDia() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<DespesaBEAN>> call = api.listarDespesasDia(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<DespesaBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<DespesaBEAN>> call, Response<ArrayList<DespesaBEAN>> response) {

                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<DespesaBEAN> u = response.body();
                                a.setVisible(false);
                                preencheTabelaDespesasDia(u);
                                preencheTabelaDespesas(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DespesaBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void listarValoresCaixa() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Caixa> call = api.buscarValoresCaixa(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Caixa>() {
            @Override
            public void onResponse(Call<Caixa> call, Response<Caixa> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                Caixa u = response.body();
                                a.setVisible(false);
                                jtfFaturamento.setText(u.getFaturamento() + "");
                                jtfSangria.setText(u.getSangria() + "");
                                jtfDespesas.setText(u.getDespesas() + "");
                                jtfFaturamentoLiquido.setText(u.getSaldo() + "");
                                lbSaldo.setText(u.getSaldo() + "");
                                lbTotalVendido.setText(u.getFaturamento() + "");
                                jtfCaixa.setText(u.getCaixa() + "");
                                lbSaldoCaixa.setText(u.getSaldo() + "");
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Caixa> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void listarDespesas() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<DespesaBEAN>> call = api.listarDespesas(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<DespesaBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<DespesaBEAN>> call, Response<ArrayList<DespesaBEAN>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<DespesaBEAN> u = response.body();
                                a.setVisible(false);
                                preencheTabela(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DespesaBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void gerarMesaBalcao() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.gerarMesaBalcao(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                String mesa = response.headers().get("sucesso");
                                a.setVisible(false);
                                jtfNunMesa.setText(mesa);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void adicionarDespesaDia(ArrayList<DespesaBEAN> des) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.incluirDespesasDia(new Gson().toJson(des), sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                a.setVisible(false);
                                listarDespesaDia();
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void excluirDespesa(ArrayList<DespesaBEAN> des) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.excluiDespesa(new Gson().toJson(des), sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                a.setVisible(false);
                                atualizaTabela();
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void listarProdutosMesa(int mesa) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<ProdutosGravados>> call = api.listarProdutosMesa(sh.getEmpEmail(), sh.getEmpSenha(), mesa + "");
        call.enqueue(new Callback<ArrayList<ProdutosGravados>>() {
            @Override
            public void onResponse(Call<ArrayList<ProdutosGravados>> call, Response<ArrayList<ProdutosGravados>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<ProdutosGravados> u = response.body();
                                a.setVisible(false);
                                if (u != null) {
                                    preencheTabelaProdutos(u);

                                }
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProdutosGravados>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void getValorMesa(int mesa) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.getValorMesa(sh.getEmpEmail(), sh.getEmpSenha(), mesa + "");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        float total = Float.parseFloat(response.headers().get("sucesso"));
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jtfTotal.setText(total + "");
                                float totalD = (float) (total + (total * 0.1));
                                if (radioTotalD.isSelected() == true) {
                                    lbTotal.setText(totalD + "");
                                } else {
                                    lbTotal.setText(jtfTotal.getText());
                                }

                                jtfTotalD.setText(totalD + "");
                                a.setVisible(false);
                                atualizaProdutos(mesa);
                                atualizaProdutosCancelados(mesa);

                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void listarExclusaoVenda(int mesa) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<ExcluzaoBEAN>> call = api.listarExcluzaoMesa(sh.getEmpEmail(), sh.getEmpSenha(), mesa + "");
        call.enqueue(new Callback<ArrayList<ExcluzaoBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<ExcluzaoBEAN>> call, Response<ArrayList<ExcluzaoBEAN>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<ExcluzaoBEAN> u = response.body();
                                a.setVisible(false);
                                preencheTabelaProdutosExcluidos(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ExcluzaoBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void retirarDespesa(ArrayList<DespesaBEAN> des) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        System.out.println("codigo " + des.get(0).getCodigo());
        final Call<Void> call = api.retirarDespesa(new Gson().toJson(des), sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.code());
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                a.setVisible(false);
                                atualizaTabela();
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void getSaldoAtualCaixa() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.saldoAtualCaixa(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.code());
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                saldo = Float.parseFloat(response.headers().get("sucesso"));
                                a.setVisible(false);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void getMesasAbertas() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.isMesasAbertas(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        String sucesso = response.headers().get("sucesso");
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                if (sucesso.equals("0")) {
                                    mudarTela("close");
                                    atualizaTabelaprodusClose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Há mesas aertas ainda, feche elas!!");
                                }
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    /*--------------------------------------------------RELATORIO---------------------------------------------------------------------------------------------*/
    private void listarVendasFinalizadasCaixa() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<VendaBEAN>> call = api.listarVendasFinalizadas(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<VendaBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<VendaBEAN>> call, Response<ArrayList<VendaBEAN>> response) {
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<VendaBEAN> u = response.body();
                                a.setVisible(false);
                                preencheTabelaVenda(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });

                        // senha ou usuario incorreto
                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VendaBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                        System.out.println("falha");
                    }
                });
            }
        });

    }

    private void listarPedidosCanceladosCaixa() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<ExcluzaoBEAN>> call = api.listarExcluzaoCaixa(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<ExcluzaoBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<ExcluzaoBEAN>> call, Response<ArrayList<ExcluzaoBEAN>> response) {
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<ExcluzaoBEAN> u = response.body();
                                a.setVisible(false);
                                preencheTabelaCancelados(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });

                        // senha ou usuario incorreto
                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ExcluzaoBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void listarSangiaCaixa() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<SangriaBEAN>> call = api.listarSangria(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<SangriaBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<SangriaBEAN>> call, Response<ArrayList<SangriaBEAN>> response) {

                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ArrayList<SangriaBEAN> u = response.body();
                                a.setVisible(false);
                                preencheTabelaSangria(u);
                            }
                        });

                    } else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                            }
                        });
                        System.out.println("Login incorreto");
                        // senha ou usuario incorreto

                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            a.setVisible(false);
                        }
                    });
                    System.out.println("Login incorreto- fora do ar");
                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SangriaBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }
}
