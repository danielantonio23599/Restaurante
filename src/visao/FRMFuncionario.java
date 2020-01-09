/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import com.google.gson.Gson;
import controle.SharedP_Control;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.CargoBEAN;
import modelo.Email;
import modelo.FuncionarioBEAN;
import modelo.local.SharedPreferencesBEAN;
import org.apache.commons.mail.EmailException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sync.RestauranteAPI;
import sync.SyncDefault;
import util.EnviaEmail;
import util.WaitLayerUI;
import visao.util.Carregamento;

/**
 *
 * @author Daniel
 */
public class FRMFuncionario extends javax.swing.JFrame {

    private TableRowSorter<TableModel> tr;
    private DefaultTableModel dTable;
    ArrayList<FuncionarioBEAN> dados;
    ArrayList<CargoBEAN> pegaCargo;

    //FuncionarioControle controle = new FuncionarioControle();
    private int codExcluir = 0;
    private boolean emailEnviado = false;

    /**
     * Creates new form FRMFuncionario
     */
    public FRMFuncionario() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        listarALL();

        atualizaTabela();
    }

    private void prencheCombo() {
        if (pegaCargo != null) {
            for (CargoBEAN car : pegaCargo) {
                comboCargo.addItem(car.getNome());
            }
        }
    }

    private void atualizaTabela() {
        buscarFuncionarios();

    }

    private void listarALL() {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.setVisible(true);
            }
        });
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        System.out.println(sh.getFunEmail() + "/" + sh.getFunSenha());
        final Call<ArrayList<CargoBEAN>> call = api.listarCargos(sh.getFunEmail(), sh.getFunSenha());
        call.enqueue(new Callback<ArrayList<CargoBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<CargoBEAN>> call, Response<ArrayList<CargoBEAN>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        ArrayList<CargoBEAN> u = response.body();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                pegaCargo = u;
                                prencheCombo();
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
            public void onFailure(Call<ArrayList<CargoBEAN>> call, Throwable t) {
                //Servidor fora do ar
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Login Incorreto erro");
                    }
                });

                System.out.println("Login incorreto");

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        painelFuncionario = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaFuncionarios = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtfTelefone = new javax.swing.JTextField();
        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("(##)#####-####");    jtfTelefone = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }
        jtfCPF = new javax.swing.JTextField();
        jtfRG = new javax.swing.JTextField();
        jtfNome = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaEndereco = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jtfNasc = new javax.swing.JTextField();
        jtfAdm = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jtfUniforme = new javax.swing.JTextField();
        jtfSalario = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        comboCargo = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jtfNumCatao = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jpSenha = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnLocalizar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        painelFuncionario.setBackground(new java.awt.Color(255, 255, 255));
        painelFuncionario.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        tabelaFuncionarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabelaFuncionarios.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        tabelaFuncionarios.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelaFuncionarios.setRowHeight(25);
        tabelaFuncionarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaFuncionariosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaFuncionarios);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1147, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );

        painelFuncionario.addTab("Todos", jPanel5);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("*Nome:");

        jLabel2.setText("*Telefone:");

        jLabel3.setText("*CPF:");

        jLabel4.setText("*RG:");

        jLabel5.setText("*Endereço:");

        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("###.###.###-##");    jtfCPF = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }
        jtfCPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCPFActionPerformed(evt);
            }
        });

        jtaEndereco.setColumns(20);
        jtaEndereco.setRows(5);
        jScrollPane1.setViewportView(jtaEndereco);

        jLabel7.setText("Data Nasc :");

        jLabel8.setText("Data Adm :");

        jLabel9.setText("*Email:");

        jLabel12.setText("*Senha:");

        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("##-##-####");    jtfNasc = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }

        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("##-##-####");    jtfAdm = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }

        jLabel11.setText("*N° uniformes :");

        jLabel10.setText("*Sálario:");

        jtfUniforme.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfUniformeKeyTyped(evt);
            }
        });

        jtfSalario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSalarioActionPerformed(evt);
            }
        });
        jtfSalario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfSalarioKeyTyped(evt);
            }
        });

        jLabel13.setText("*Cargo:");

        comboCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCargoActionPerformed(evt);
            }
        });

        jLabel14.setText("N° Cartão de Ponto:");

        jtfNumCatao.setEditable(false);

        jButton1.setText("Gerar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfUniforme, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(26, 26, 26)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jtfNumCatao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(82, 82, 82)
                    .addComponent(jtfSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(565, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(comboCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jtfNumCatao, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jtfUniforme, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(134, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jtfSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(218, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 484, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtfCPF)
                                    .addComponent(jtfRG)
                                    .addComponent(jtfNasc)
                                    .addComponent(jtfAdm)
                                    .addComponent(jpSenha)
                                    .addComponent(jtfEmail)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jLabel1))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addGap(4, 4, 4))
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtfNome, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                                    .addComponent(jtfTelefone)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(11, 11, 11)
                                .addComponent(jScrollPane1)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfRG, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jtfAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        painelFuncionario.addTab("Cadastro", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelFuncionario)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jLabel79)
                    .addContainerGap(1242, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelFuncionario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(114, 114, 114)
                    .addComponent(jLabel79)
                    .addContainerGap(589, Short.MAX_VALUE)))
        );

        jPanel2.setBackground(new java.awt.Color(0, 153, 102));

        btnLocalizar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLocalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/remove-symbol (2).png"))); // NOI18N
        btnLocalizar.setText("Voltar");
        btnLocalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalizarActionPerformed(evt);
            }
        });

        btnAdicionar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        btnAdicionar.setText("Adicionar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarjButton6ActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/edit.png"))); // NOI18N
        btnEditar.setText("Reenviar Email");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarjButton7ActionPerformed(evt);
            }
        });

        btnExcluir.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirjButton8ActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logo.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("CAD. FUNCIONARIOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLocalizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addGap(30, 30, 30)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnLocalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLocalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocalizarActionPerformed
        dispose();
    }//GEN-LAST:event_btnLocalizarActionPerformed

    private void btnAdicionarjButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarjButton6ActionPerformed
        String v = verificaCampos();
        if (v.equals("")) {
            try {
                FuncionarioBEAN f = getDados();
                cadastrar(f);

            } catch (ParseException ex) {
                Logger.getLogger(FRMFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            }
            atualizaTabela();
        }
    }//GEN-LAST:event_btnAdicionarjButton6ActionPerformed

    private void btnEditarjButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarjButton7ActionPerformed
        //reenviar email
    }//GEN-LAST:event_btnEditarjButton7ActionPerformed

    private void btnExcluirjButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirjButton8ActionPerformed
        if (codExcluir != 0) {
            Carregamento a = new Carregamento(this, true);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    a.setVisible(true);

                }
            });
            SharedPreferencesBEAN sh = SharedP_Control.listar();
            RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
            final Call<Void> call = api.excluiFuncionario(codExcluir + "", sh.getFunEmail(), sh.getFunSenha());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    System.out.println(response.isSuccessful());
                    if (response.isSuccessful()) {
                        String auth = response.headers().get("auth");
                        if (auth.equals("1")) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    a.setVisible(false);
                                    jtfNumCatao.setText(response.headers().get("sucesso"));
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
            JOptionPane.showMessageDialog(null, "Usuario excluido com sucesso");
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(null, "selecione o Funcionario");

        }
    }//GEN-LAST:event_btnExcluirjButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// selvet para gerar numero
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<Void> call = api.gerarNumFunPonto(sh.getFunEmail(), sh.getFunSenha());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                jtfNumCatao.setText(response.headers().get("sucesso"));
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboCargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCargoActionPerformed

    private void tabelaFuncionariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaFuncionariosMouseClicked
        int linha = tabelaFuncionarios.getSelectedRow();
        //selvet para localizar funcionario
        localizar(Integer.parseInt(tabelaFuncionarios.getValueAt(linha, 0) + ""));
    }//GEN-LAST:event_tabelaFuncionariosMouseClicked

    private void jtfSalarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfSalarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfSalarioActionPerformed

    private void jtfUniformeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfUniformeKeyTyped
        String caracteres = "0987654321";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
}    }//GEN-LAST:event_jtfUniformeKeyTyped

    private void jtfSalarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfSalarioKeyTyped
        String caracteres = "0987654321";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_jtfSalarioKeyTyped

    private void jtfCPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfCPFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfCPFActionPerformed

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
            java.util.logging.Logger.getLogger(FRMFuncionario.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FRMFuncionario.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FRMFuncionario.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FRMFuncionario.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FRMFuncionario().setVisible(true);
            }
        });
    }

    private Email pegaDadosEmailEX(String desti) {
        Email e = new Email();
        e.setAssunto("Cadastro De Adimissão");
        e.setCaminhoAnexo("");
        e.setDestinatario(desti);
        e.setMensagem("Seja Bem vindo a nossa empresa, desejamos muitas felicidades para o desenpenho das suas funções!!"
                + "segue os seus dados de login 'SENHA' = " + jpSenha.getText());
        e.setRemetente("danielantonio23599@gmail.com");
        e.setSenha("galodoido13");
        e.setNomeRemetente("Daniel");
        return e;
    }

    private DefaultTableModel criaTabela() {
        //sempre que usar JTable é necessário ter um DefaulttableModel
        DefaultTableModel dTable = new DefaultTableModel() {
            //Define o tipo dos campos (coluna) na mesma ordem que as colunas foram criadas
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            //define se os campos podem ser editados na propria tabela
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        //retorna o DefaultTableModel
        return dTable;
    }

    private void preencheTabela(ArrayList<FuncionarioBEAN> dados) {
        dTable = criaTabela();
        //seta o nome das colunas da tabela
        dTable.addColumn("Código");
        dTable.addColumn("Nome");
        dTable.addColumn("Cargo");
        dTable.addColumn("CPF");
        dTable.addColumn("Data Adimição");
        dTable.addColumn("Data Nascimento");
        dTable.addColumn("Telefone");
        dTable.addColumn("Endereço");
        dTable.addColumn("Email");

        //pega os dados do ArrayList
        //cada célula do arrayList vira uma linha(row) na tabela
        DateFormat formatBR = new SimpleDateFormat("dd-mm-yyyy");
        DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        Date date2 = null;
        for (FuncionarioBEAN dado : dados) {

            try {
                date = formatUS.parse(dado.getDataAdmicao());
            } catch (ParseException ex) {
                Logger.getLogger(FRMFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                date2 = formatUS.parse(dado.getDataNacimento());
            } catch (ParseException ex) {
                Logger.getLogger(FRMFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            }

//Depois formata data
            dTable.addRow(new Object[]{dado.getCodigo(), dado.getNome(),
                dado.getCargo(), dado.getCPF(),
                formatBR.format(date), formatBR.format(date2), dado.getTelefone(),
                dado.getEndereco(), dado.getEmail()});
        }
        //set o modelo da tabela
        tabelaFuncionarios.setModel(dTable);
        tr = new TableRowSorter<TableModel>(dTable);
        tabelaFuncionarios.setRowSorter(tr);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLocalizar;
    private javax.swing.JComboBox<String> comboCargo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPasswordField jpSenha;
    private javax.swing.JTextArea jtaEndereco;
    private javax.swing.JTextField jtfAdm;
    private javax.swing.JTextField jtfCPF;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfNasc;
    private javax.swing.JTextField jtfNome;
    private javax.swing.JTextField jtfNumCatao;
    private javax.swing.JTextField jtfRG;
    private javax.swing.JTextField jtfSalario;
    private javax.swing.JTextField jtfTelefone;
    private javax.swing.JTextField jtfUniforme;
    private javax.swing.JTabbedPane painelFuncionario;
    private javax.swing.JTable tabelaFuncionarios;
    // End of variables declaration//GEN-END:variables

    private String verificaCampos() {
        String retorno = "";
        if (jtfNome.getText().equals("")) {
            retorno = "Campo Nome";
        }
        if (jtfAdm.getText().equals("")) {
            retorno += ", Data Admição";
        }
        if (jtfCPF.getText().equals("")) {
            retorno += ", CPF";
        }
        if (jtfEmail.getText().equals("")) {
            retorno += ", Email";
        }
        if (jtfNasc.getText().equals("")) {
            retorno += ", Data Nascimento";
        }
        if (jtfRG.getText().equals("")) {
            retorno += ", RG";
        }
        if (jtfTelefone.getText().equals("")) {
            retorno += ", Telefone";
        }
        if (jtfNumCatao.getText().equals("")) {
            retorno += ", Numero do cartão";
        }
        if (jtfSalario.getText().equals("")) {
            retorno += ", Salário";
        }
        if (jtfUniforme.getText().equals("")) {
            retorno += ",Quantidade de Uniformes";
        }
        if (jtfNumCatao.getText().equals("")) {
            retorno += ",Quantidade de Uniformes";
        }

        if (!retorno.equals("")) {
            retorno += " se encontra(ão) 'vazio(s)', preencha-o(s) por gentileza!!";
        }
        return retorno;
    }

    private FuncionarioBEAN getDados() throws ParseException {
        int cargo = 0;
        for (CargoBEAN dado : pegaCargo) {
            if (dado.getNome().equals(comboCargo.getSelectedItem() + "")) {
                cargo = dado.getCodigo();
            }
        }

        DateFormat formatUS = new SimpleDateFormat("dd-mm-yyyy");
        Date date = formatUS.parse(jtfAdm.getText());
        Date date2 = formatUS.parse(jtfNasc.getText());

//Depois formata data
        DateFormat formatBR = new SimpleDateFormat("yyyy-mm-dd");
        String dateFormated = formatBR.format(date);
        String dateFormated2 = formatBR.format(date2);
        FuncionarioBEAN f = new FuncionarioBEAN();
        f.setCPF(jtfCPF.getText() + "");
        f.setDataAdmicao(dateFormated);
        f.setDataNacimento(dateFormated2);
        f.setCargo(cargo);
        f.setEmail(jtfEmail.getText() + "");
        f.setEndereco(jtaEndereco.getText() + "");
        f.setNome(jtfNome.getText() + "");
        f.setRG(jtfRG.getText() + "");
        f.setSalario(Float.parseFloat(jtfSalario.getText() + ""));
        f.setSenha(jpSenha.getText() + "");
        f.setTelefone(jtfTelefone.getText() + "");
        f.setUniforme(Integer.parseInt(jtfUniforme.getText() + ""));
        f.setCartao(Integer.parseInt(jtfNumCatao.getText()));
        return f;
    }

    private void limpaCampos() {

        comboCargo.setSelectedIndex(0);
        jtfAdm.setText("");
        jtfNasc.setText("");
        jtfCPF.setText("");

        jtfEmail.setText("");
        jtaEndereco.setText("");
        jtfNome.setText("");
        jtfRG.setText("");
        jtfSalario.setText("");
        jpSenha.setText("");
        jtfTelefone.setText("");
        jtfUniforme.setText("");
        jtfNumCatao.setText("");

    }

    private void preencheCampos(FuncionarioBEAN f) throws ParseException {
        //selvet de listar um funcionario
        DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
        Date date = formatUS.parse(f.getDataAdmicao());
        Date date2 = formatUS.parse(f.getDataNacimento());

//Depois formata data
        DateFormat formatBR = new SimpleDateFormat("dd-mm-yyyy");
        String dateFormated = formatBR.format(date);
        String dateFormated2 = formatBR.format(date2);
        System.out.println(f.getCargo());
        listarALL();
        for (CargoBEAN dado : pegaCargo) {
            if (dado.getCodigo() == f.getCodigo()) {
                comboCargo.setSelectedItem(dado.getNome());
            }
        }
        jtfAdm.setText(dateFormated);
        jtfNasc.setText(dateFormated2);
        jtfCPF.setText(f.getCPF());
        jtfEmail.setText(f.getEmail());
        jtaEndereco.setText(f.getEndereco());
        jtfNome.setText(f.getNome());
        jtfRG.setText(f.getRG());
        jtfSalario.setText(f.getSalario() + "");
        jpSenha.setText(f.getSenha());
        jtfTelefone.setText(f.getTelefone());
        jtfUniforme.setText(f.getUniforme() + "");
        jtfNumCatao.setText(f.getCartao() + "");
        painelFuncionario.setSelectedIndex(1);

    }

    private void buscarFuncionarios() {

        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        System.out.println(sh.getFunEmail() + "/" + sh.getFunSenha());
        final Call<ArrayList<FuncionarioBEAN>> call = api.listarFuncionarios(sh.getFunEmail(), sh.getFunSenha());
        call.enqueue(new Callback<ArrayList<FuncionarioBEAN>>() {
            @Override
            public void onResponse(Call<ArrayList<FuncionarioBEAN>> call, Response<ArrayList<FuncionarioBEAN>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        ArrayList<FuncionarioBEAN> u = response.body();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                dados = u;
                                preencheTabela(dados);
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
            public void onFailure(Call<ArrayList<FuncionarioBEAN>> call, Throwable t) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }

    private void cadastrar(FuncionarioBEAN f) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        System.out.println(sh.getFunEmail() + "/" + sh.getFunSenha());
        final Call<Void> call = api.insereFuncionario(new Gson().toJson(f), sh.getFunEmail(), sh.getFunSenha());
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

    public void localizar(int codigo) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        final Call<FuncionarioBEAN> call = api.listarFuncionario(sh.getFunEmail(), sh.getFunSenha(), codigo + "");
        call.enqueue(new Callback<FuncionarioBEAN>() {
            @Override
            public void onResponse(Call<FuncionarioBEAN> call, Response<FuncionarioBEAN> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        System.out.println("Login correto");
                        FuncionarioBEAN u = response.body();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                a.setVisible(false);
                                try {
                                    // dados = u;
                                    preencheCampos(u);
                                    System.out.println("passou");
                                } catch (ParseException ex) {
                                    Logger.getLogger(FRMFuncionario.class.getName()).log(Level.SEVERE, null, ex);
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
            public void onFailure(Call<FuncionarioBEAN> call, Throwable thrwbl) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        a.setVisible(false);
                    }
                });
            }
        });
    }
}
