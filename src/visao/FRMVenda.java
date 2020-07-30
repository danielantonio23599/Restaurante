/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import com.google.gson.Gson;
import controle.SharedPEmpresa_Control;

import controle.SharedP_Control;
import controleService.ControleLogin;

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
import modelo.DevolucaoBEAN;
import modelo.Produtos;
import modelo.ProdutosGravados;
import modelo.SangriaBEAN;
import modelo.Venda;
import modelo.VendaBEAN;
import modelo.local.SharedPreferencesBEAN;
import modelo.local.SharedPreferencesEmpresaBEAN;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sync.SyncDefault;
import util.SalvaDownload;

import util.Time;
import visao.util.AlertAbrirCaixa;
import visao.util.AlertSangria;
import visao.util.Carregamento;
import sync.RestauranteAPI;
import util.ManipularImagem;
import visao.util.AlertAbrirVenda;
import visao.util.FRMVendasAbertas;

/**
 *
 * @author Daniel
 */
public class FRMVenda extends javax.swing.JFrame {

    private DefaultTableModel dTable;
    private TableRowSorter<TableModel> tr;
    private float saldoMesa;
    private float saldo;

    /**
     * Creates new form FRMCaixa
     */
    public FRMVenda() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        mudarTela("index");
        setDados();
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
        if (cadenaEscrita.length() <= 10) {

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
        } else {
            pesquisarProdutos();
        }
    }

    private void mudarTela(String nome) {
        CardLayout card = (CardLayout) Principal.getLayout();
        card.show(Principal, nome);
    }

    public void limpaTabelaProdutosBalcao() {
        dTable = criaTabelaProdutos();
        while (dTable.getRowCount() > 0) {
            dTable.removeRow(0);
        }
        tabelaProdutosBalcao.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProdutosBalcao.setRowSorter(tr);
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
        MenuLateral = new javax.swing.JPanel();
        btnRelatorios = new javax.swing.JButton();
        btnLocalizar1 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        btnRelatorios1 = new javax.swing.JButton();
        btnRelatorios3 = new javax.swing.JButton();
        Principal = new javax.swing.JPanel();
        FecharMesa = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jtfVendaF = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        comboPagamento = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jtfTotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        radioAvista = new javax.swing.JRadioButton();
        radioAprazo = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        radioTotal = new javax.swing.JRadioButton();
        radioDesc = new javax.swing.JRadioButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jtfDesconto = new javax.swing.JTextField();
        jtfDescontoP = new javax.swing.JTextField();
        jtfFrete = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jtfClienteF = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabelaProdutosF = new javax.swing.JTable();
        index = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        VendaBalcao = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jtfTotalFiscal = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jtfCliente = new javax.swing.JTextField();
        jtfVenda = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
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
        lbLogo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Caixa");

        MenuLateral.setBackground(new java.awt.Color(0, 153, 102));
        MenuLateral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));

        btnRelatorios.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRelatorios.setText("VOLTAR");
        btnRelatorios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatoriosActionPerformed(evt);
            }
        });

        btnLocalizar1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLocalizar1.setText("PONTO DE VENDA");
        btnLocalizar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLocalizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalizar1ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("VENDA");

        btnRelatorios1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRelatorios1.setText("VENDAS");
        btnRelatorios1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelatorios1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatorios1ActionPerformed(evt);
            }
        });

        btnRelatorios3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRelatorios3.setText("CLIENTES");
        btnRelatorios3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelatorios3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatorios3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLateralLayout = new javax.swing.GroupLayout(MenuLateral);
        MenuLateral.setLayout(MenuLateralLayout);
        MenuLateralLayout.setHorizontalGroup(
            MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(btnRelatorios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLocalizar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRelatorios1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRelatorios3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        MenuLateralLayout.setVerticalGroup(
            MenuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(btnLocalizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnRelatorios1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnRelatorios3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(353, 353, 353))
        );

        Principal.setBackground(new java.awt.Color(153, 0, 0));
        Principal.setLayout(new java.awt.CardLayout());

        FecharMesa.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel55.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel55.setText("Venda :");

        jtfVendaF.setEditable(false);
        jtfVendaF.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtfVendaF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfVendaF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfVendaFActionPerformed(evt);
            }
        });

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        comboPagamento.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        comboPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DINHEIRO", "CARTÃO CRÉDITO", "CARTÃO DÉBITO", "ANOTADA" }));
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioAprazo)
                .addGap(96, 96, 96))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(radioAvista)
                    .addComponent(radioAprazo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel23.setText("Valor :");

        valorgrupo.add(radioTotal);
        radioTotal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioTotal.setSelected(true);
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

        valorgrupo.add(radioDesc);
        radioDesc.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        radioDesc.setText("Desconto");
        radioDesc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioDescMouseClicked(evt);
            }
        });
        radioDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDescActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jLabel23)
                .addGap(26, 26, 26)
                .addComponent(radioDesc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioTotal)
                .addGap(100, 100, 100))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(radioTotal)
                    .addComponent(radioDesc))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton19.setBackground(new java.awt.Color(51, 102, 255));
        jButton19.setText("Finalizar e Emitir Recibo ");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(51, 102, 255));
        jButton20.setText("Finalizar e Emitir NFC-e");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel18.setText("Desconto:");

        jLabel29.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel29.setText("% desconto:");

        jtfDesconto.setEditable(false);
        jtfDesconto.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jtfDesconto.setForeground(new java.awt.Color(204, 0, 0));
        jtfDesconto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfDesconto.setText("0");
        jtfDesconto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfDescontoFocusLost(evt);
            }
        });
        jtfDesconto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescontoActionPerformed(evt);
            }
        });
        jtfDesconto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfDescontoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfDescontoKeyTyped(evt);
            }
        });

        jtfDescontoP.setEditable(false);
        jtfDescontoP.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jtfDescontoP.setForeground(new java.awt.Color(204, 0, 0));
        jtfDescontoP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfDescontoP.setText("0");
        jtfDescontoP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfDescontoPFocusLost(evt);
            }
        });
        jtfDescontoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescontoPActionPerformed(evt);
            }
        });
        jtfDescontoP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfDescontoPKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfDescontoPKeyTyped(evt);
            }
        });

        jtfFrete.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jtfFrete.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfFrete.setText("0");
        jtfFrete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfFreteFocusLost(evt);
            }
        });
        jtfFrete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFreteActionPerformed(evt);
            }
        });
        jtfFrete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfFreteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfFreteKeyTyped(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel58.setText("Frete :");

        jtfClienteF.setEditable(false);
        jtfClienteF.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfClienteF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfClienteF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfClienteFActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton3.setText("CLIENTE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 147, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel56)
                                .addComponent(jLabel27)
                                .addComponent(jLabel18)
                                .addComponent(jLabel29)
                                .addComponent(jLabel58))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfClienteF)
                            .addComponent(jtfFrete)
                            .addComponent(jtfTotal)
                            .addComponent(comboPagamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtfDesconto)
                            .addComponent(jtfDescontoP))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56)
                    .addComponent(jtfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfFrete, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jtfDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jtfDescontoP, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(comboPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfClienteF, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70))
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
                .addComponent(jScrollPane8)
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
                        .addComponent(jtfVendaF, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jtfVendaF, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        index.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logo_beta_M.jpg"))); // NOI18N

        javax.swing.GroupLayout indexLayout = new javax.swing.GroupLayout(index);
        index.setLayout(indexLayout);
        indexLayout.setHorizontalGroup(
            indexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 1285, Short.MAX_VALUE)
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

        jtfTotalFiscal.setEditable(false);
        jtfTotalFiscal.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfTotalFiscal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfTotalFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTotalFiscalActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 51, 153));
        jLabel42.setText("TOTAL :");

        jtfCliente.setEditable(false);
        jtfCliente.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfClienteActionPerformed(evt);
            }
        });

        jtfVenda.setEditable(false);
        jtfVenda.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jtfVenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfVendaActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("CLIENTE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setText("VENDA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jtfTotalFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfTotalFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 168, Short.MAX_VALUE))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Adcionar Produto"));

        botaoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loupe.png"))); // NOI18N
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });

        comboProduto.setEditable(true);
        comboProduto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboProdutoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboProdutoKeyTyped(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botaoPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(comboProduto))
                .addContainerGap())
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

        lbLogo.setBackground(new java.awt.Color(255, 255, 255));
        lbLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(lbLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(0, 247, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Principal.add(VendaBalcao, "fiscal");

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

        jMenu2.setText("Venda");

        jMenuItem3.setText("Atualizar");
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuLateral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MenuLateral, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        dispose();
    }//GEN-LAST:event_btnRelatoriosActionPerformed
    private void limparFechamentoMesa() {
        jtfTotal.setText("0");
        jtfFrete.setText("0");
        jtfDesconto.setText("0");
        jtfDescontoP.setText("0");
        lbTotal.setText("0");
        jtfVendaF.setText("0");
        comboProduto.removeAllItems();
        limpaTabelaProdutosBalcao();
    }

    public void abrirVendaBalcao() {

        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.isCaixaAberto(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        String sucesso = response.headers().get("sucesso");
                        int cod = Integer.parseInt(sucesso);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);

                            }
                        });
                        if (cod > 0) {
                            mudarTela("fiscal");
                            abrirVenda();
                        } else {
                            JOptionPane.showMessageDialog(null, "Caixa fechado, favor abri-lo primeiro");
                        }
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
                System.out.println("Login incorreto- erro");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        a.setVisible(false);

                    }
                });
            }
        });
    }
    private void btnLocalizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocalizar1ActionPerformed
        AlertAbrirVenda v = new AlertAbrirVenda();
        v.setV(this);
        v.setVisible(true);

    }//GEN-LAST:event_btnLocalizar1ActionPerformed

    private void jtfTotalFiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalFiscalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalFiscalActionPerformed

    private void jtfClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfClienteActionPerformed

    private void jtfVendaFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfVendaFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfVendaFActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jtfVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfVendaActionPerformed

    private void radioAvistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioAvistaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioAvistaActionPerformed

    private void jtfTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalActionPerformed

    private void radioTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioTotalActionPerformed

    private void radioTotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioTotalMouseClicked
        if (radioTotal.isSelected()) {
            lbTotal.setText(jtfTotal.getText());
            jtfDesconto.setEditable(false);
            jtfDescontoP.setEditable(false);
            jtfDesconto.setText("0.0");
            jtfDescontoP.setText("0");
        } else {
            jtfDesconto.setEditable(true);
            jtfDescontoP.setEditable(true);
        }
    }//GEN-LAST:event_radioTotalMouseClicked

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
            // atualizaMesas();
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        pesquisarProdutos();
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        buscarProdutosVenda(jtfVenda.getText());
        listarVenda();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void comboProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboProdutoKeyPressed

    }//GEN-LAST:event_comboProdutoKeyPressed

    private void btnFecharVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharVendaActionPerformed
        if (!jtfVenda.getText().equals("")) {
            jtfVendaF.setText(jtfVenda.getText());
            mudarTela("mesa");
            somarTotal();
            atualizaProdutos(Integer.parseInt(jtfVenda.getText()));
        } else {
            JOptionPane.showMessageDialog(null, "Insira algum produto, o casa já inserido, atualize a venda !");
        }

    }//GEN-LAST:event_btnFecharVendaActionPerformed

    private void jtfDescontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDescontoActionPerformed

    }//GEN-LAST:event_jtfDescontoActionPerformed

    private void jtfDescontoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDescontoPActionPerformed

    }//GEN-LAST:event_jtfDescontoPActionPerformed

    private void jtfDescontoPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfDescontoPFocusLost
        if (radioDesc.isSelected()) {
            String por = jtfDescontoP.getText();
            if (!por.equals("")) {
                float des = Float.parseFloat(por);
                des = des / 100;
                lbTotal.setText(somarTotalFinal() + "");
                jtfDesconto.setText(des * Float.parseFloat(lbTotal.getText()) + "");

            }
        }
    }//GEN-LAST:event_jtfDescontoPFocusLost

    private void jtfDescontoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfDescontoFocusLost
        if (radioDesc.isSelected()) {
            String por = jtfDesconto.getText();
            if (!por.equals("")) {
                float des = Float.parseFloat(por);
                jtfDescontoP.setText((des / Float.parseFloat(lbTotal.getText())) * 100 + "");
                lbTotal.setText(somarTotalFinal() + "");
            }
        }
    }//GEN-LAST:event_jtfDescontoFocusLost
    private Float somarTotalFinal() {
        float tof = 0;
        float to = Float.parseFloat(jtfTotal.getText());
        float des = Float.parseFloat(jtfDesconto.getText());
        float fret = Float.parseFloat(jtfFrete.getText());
        tof = (to + fret) - des;
        return tof;

    }
    private void jtfDescontoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfDescontoKeyTyped
        bloqueaLetras(evt);
    }//GEN-LAST:event_jtfDescontoKeyTyped

    private void jtfDescontoPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfDescontoPKeyTyped
        bloqueaLetras(evt);
    }//GEN-LAST:event_jtfDescontoPKeyTyped

    private void radioDescMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioDescMouseClicked
        if (radioDesc.isSelected()) {
            lbTotal.setText(somarTotalFinal() + "");
            jtfDesconto.setEditable(true);
            jtfDescontoP.setEditable(true);
        } else {
            jtfDesconto.setEditable(false);
            jtfDescontoP.setEditable(false);
        }
    }//GEN-LAST:event_radioDescMouseClicked

    private void radioDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioDescActionPerformed

    private void jtfDescontoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfDescontoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfDescontoP.requestFocus();
        }
    }//GEN-LAST:event_jtfDescontoKeyPressed

    private void jtfDescontoPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfDescontoPKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            lbTotal.requestFocus();
        }
    }//GEN-LAST:event_jtfDescontoPKeyPressed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        String message = "Deseja realmente fechar a mesa?";
        String title = "Confirmação";
        int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            VendaBEAN v = getDadosVenda();
            RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
            SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
            Carregamento a = new Carregamento(this, true);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    a.setVisible(true);

                }
            });

            final Call<ResponseBody> call = api.atualizaVendaNota(new Gson().toJson(v), sh.getEmpEmail(), sh.getEmpSenha());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println(response.isSuccessful());
                    System.out.println(response);
                    if (response.isSuccessful()) {
                        String auth = response.headers().get("auth");
                        if (auth.equals("1")) {
                            String nome = response.headers().get("nome");
                            if (!nome.equals("0")) {
                                boolean writtenToDisk = SalvaDownload.writeResponseBodyToDisk(response.body(), nome);
                                System.out.println("Login correto");
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        a.setVisible(false);

                                    }
                                });
                                System.out.println("file download was a success? " + writtenToDisk);
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        a.setVisible(false);

                                    }
                                });
                                JOptionPane.showMessageDialog(null, "Houve algum erro no gerar arquivo!");
                            }
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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Login incorreto- erro");
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            a.setVisible(false);

                        }
                    });
                }
            });
        }
        // atualizaMesas();
    }//GEN-LAST:event_jButton19ActionPerformed
    public void setVenda(Venda v) {
        jtfVenda.setText(v.getCodigo() + "");
        jtfCliente.setText(v.getCliente() + "");
        jtfClienteF.setText(v.getCliente() + "");
        jtfTotalFiscal.setText(v.getValor() + "");
        buscarProdutosVenda(jtfVenda.getText());
        mudarTela("fiscal");
    }
    private void comboProdutoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboProdutoKeyTyped
        if (KeyEvent.VK_ENTER == evt.getKeyCode()) {
            pesquisarProdutos();
        } else {
            evt.consume();
        }

    }//GEN-LAST:event_comboProdutoKeyTyped

    private void btnRelatorios1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatorios1ActionPerformed
        FRMListaVendas l = new FRMListaVendas();
        l.setV(this);
        l.setVisible(true);
    }//GEN-LAST:event_btnRelatorios1ActionPerformed

    private void btnRelatorios3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatorios3ActionPerformed
        FRMClientes c = new FRMClientes();
        c.setVisible(true);
    }//GEN-LAST:event_btnRelatorios3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        FRMVendasAbertas v = new FRMVendasAbertas();
        v.setV(this);
        v.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jtfFreteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFreteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfFreteActionPerformed

    private void jtfFreteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfFreteFocusLost
        lbTotal.setText(somarTotalFinal() + "");
    }//GEN-LAST:event_jtfFreteFocusLost

    private void jtfFreteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFreteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtfDesconto.requestFocus();
        }
    }//GEN-LAST:event_jtfFreteKeyPressed

    private void jtfFreteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFreteKeyTyped
        bloqueaLetras(evt);
    }//GEN-LAST:event_jtfFreteKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FRMClientes c = new FRMClientes();
        c.setV(this);
        c.setVenda(Integer.parseInt(jtfVenda.getText()));
        c.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jtfClienteFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfClienteFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfClienteFActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        FRMClientes c = new FRMClientes();
        c.setV(this);
        c.setVenda(Integer.parseInt(jtfVenda.getText()));
        c.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void bloqueaLetras(java.awt.event.KeyEvent evt) {
        String caracteres = "0987654321.";
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
            java.util.logging.Logger.getLogger(FRMVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FRMVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FRMVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FRMVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FRMVenda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FecharMesa;
    private javax.swing.JPanel MenuLateral;
    private javax.swing.JPanel Principal;
    private javax.swing.JPanel VendaBalcao;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFecharVenda;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLocalizar1;
    private javax.swing.JButton btnRelatorios;
    private javax.swing.JButton btnRelatorios1;
    private javax.swing.JButton btnRelatorios3;
    private javax.swing.JComboBox<String> comboPagamento;
    private javax.swing.JComboBox<String> comboProduto;
    private javax.swing.JPanel index;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextField jtfCliente;
    private javax.swing.JTextField jtfClienteF;
    private javax.swing.JTextField jtfDesconto;
    private javax.swing.JTextField jtfDescontoP;
    private javax.swing.JTextField jtfFrete;
    private javax.swing.JTextField jtfTotal;
    private javax.swing.JTextField jtfTotalFiscal;
    private javax.swing.JTextField jtfVenda;
    private javax.swing.JTextField jtfVendaF;
    private javax.swing.JLabel lbLogo;
    private javax.swing.JLabel lbTotal;
    private javax.swing.ButtonGroup pagamento;
    private javax.swing.JRadioButton radioAprazo;
    private javax.swing.JRadioButton radioAvista;
    private javax.swing.JRadioButton radioDesc;
    private javax.swing.JRadioButton radioTotal;
    private javax.swing.JTable tabelaProdutosBalcao;
    private javax.swing.JTable tabelaProdutosF;
    private javax.swing.ButtonGroup valorgrupo;
    // End of variables declaration//GEN-END:variables

    public void setNunMesa(String mesa) {
        //labNumMesa.setText(mesa);
        atualizaProdutos(Integer.parseInt(mesa));
    }

    private void home() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0, 5));
        int controle = 100;

    }

    /*private void atualizaMesas() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        LojaAPI api = SyncDefault.RETROFIT_LOJA.create(LojaAPI.class);
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
    }*/
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
        tabelaProdutosF.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaProdutosF.setRowSorter(tr);

    }

    public void atualizaProdutos(int mesa) {
        listarProdutosMesa(mesa);

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
        getValorMesa(Integer.parseInt(jtfVendaF.getText() + ""));

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
                                a.setVisible(false);
                                JOptionPane.showMessageDialog(null, response.headers().get("sucesso"));
                                limparFechamentoMesa();
                                mudarTela("index");
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
        venda.setCodigo(Integer.parseInt(jtfVendaF.getText()));
        venda.setPagamento(comboPagamento.getSelectedItem() + "");
        venda.setValor(Float.parseFloat(jtfTotal.getText()));
        if (radioDesc.isSelected()) {
            venda.setDesconto(Float.parseFloat(jtfDesconto.getText()));
        } else {
            venda.setDesconto(0.0f);
        }
        if (radioAprazo.isSelected()) {
            venda.setStatus("Anotada");
        } else {
            venda.setStatus("Fechada");
        }
        //venda.setFrete(Float.parseFloat(jtfFrete.getText()));
        // venda.setValorFin(Float.parseFloat(lbTotal.getText()));
        // System.out.println(venda.getFrete() + "");
        return venda;
    }

    // VENDA A BALCA
    public void atualizar() {
        // buscar produtos e valor total
        buscarProdutosVenda(jtfVenda.getText());
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
                                    r.setDados(jtfVenda.getText() + "");
                                    // r.setV(FRMVenda.this);
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

    public void listarVenda() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Venda> call = api.listarVenda(sh.getEmpEmail(), sh.getEmpSenha(), jtfVenda.getText());
        call.enqueue(new Callback<Venda>() {
            @Override
            public void onResponse(Call<Venda> call, Response<Venda> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                Venda u = response.body();
                                a.setVisible(false);
                                if (u != null) {
                                    setVenda(u);
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
            public void onFailure(Call<Venda> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });

    }

    private void buscarProdutosVenda(String mesa) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<ArrayList<ProdutosGravados>> call = api.listarProdutosVenda(sh.getEmpEmail(), sh.getEmpSenha(), mesa + "");
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
                                    float total = 0;
                                    for (ProdutosGravados p : u) {
                                        //total += p.getValorFinal();
                                    }
                                    jtfTotalFiscal.setText(total + "");
                                } else if (!jtfTotalFiscal.equals("")) {
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

//relatorio
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

    private void atualizaTabelaprodusClose() {
        listarProdutosVendidosCaixa();
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

    public void abrirVenda() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.abrirVenda(sh.getEmpEmail(), sh.getEmpSenha());
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
                                String venda = response.headers().get("sucesso");
                                a.setVisible(false);
                                jtfVenda.setText(venda);
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
        final Call<ArrayList<ProdutosGravados>> call = api.listarProdutosVenda(sh.getEmpEmail(), sh.getEmpSenha(), mesa + "");
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
                        System.out.println(total);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jtfTotal.setText(total + "");
                                float totalD = (float) (total + (total * 0.10));
                                System.out.println(totalD);

                                lbTotal.setText(jtfTotal.getText());

                                a.setVisible(false);
                                atualizaProdutos(mesa);

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

    private void setDados() {
        ControleLogin l = new ControleLogin();
        SharedPreferencesEmpresaBEAN e = l.listarEmpresa();
        if (e != null) {
            if (e.getEmpLogo() != null) {
                ManipularImagem m = new ManipularImagem();
                m.exibiImagemLabel(e.getEmpLogo(), lbLogo);
            }
        }
    }

    /*--------------------------------------------------RELATORIO---------------------------------------------------------------------------------------------*/
}
