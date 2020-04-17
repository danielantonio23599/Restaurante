/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import com.google.gson.Gson;
import controle.SharedPEmpresa_Control;
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
import modelo.local.SharedPreferencesEmpresaBEAN;
import org.apache.commons.mail.EmailException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sync.RestauranteAPI;
import sync.SyncDefault;
import util.EnviaEmail;
import util.Time;
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
        this.setLocationRelativeTo(null);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        preencheCombo();
    }

    private void preencheCombo() {
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        comboEmpresa.removeAll();
        comboEmpresa.addItem(sh.getEmpFantazia());
    }

    /* private void listarALL() {
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

    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jpSenha = new javax.swing.JPasswordField();
        jtfNasc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtfRG = new javax.swing.JTextField();
        jtfCPF = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jtfTelefone = new javax.swing.JTextField();
        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("(##)#####-####");    jtfTelefone = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }
        jLabel2 = new javax.swing.JLabel();
        jtfNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jtfCEP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtfComplemento = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtfCidade = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jtfNumero = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtfUF = new javax.swing.JTextField();
        jtfLogradouro = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jtfBairro = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jpSenhaR = new javax.swing.JPasswordField();
        jLabel24 = new javax.swing.JLabel();
        comboEmpresa = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        Botoes = new javax.swing.JPanel();
        btnAdicionar = new javax.swing.JButton();
        btnLocalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel7.setBackground(new java.awt.Color(0, 153, 102));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Cadastro de Funcionario");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Funcionario"));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("*Senha:");

        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("##-##-####");    jtfNasc = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("Data Nasc :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("*RG:");

        try{    javax.swing.text.MaskFormatter cpf= new javax.swing.text.MaskFormatter("###.###.###-##");    jtfCPF = new javax.swing.JFormattedTextField(cpf); }    catch (Exception e){ }
        jtfCPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfCPFActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("*CPF:");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("*Email:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("*Telefone:");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setText("*Nome:");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("ENDEREÇO"));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setText("*CEP:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("*Bairro:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("*Cidade:");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel21.setText("*N°");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("*UF");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel22.setText("*Logradouro:");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel23.setText("Complemento:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jtfCEP)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtfLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(0, 161, Short.MAX_VALUE))
                                    .addComponent(jtfNumero))))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jtfCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtfUF)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jtfBairro)
                        .addGap(18, 18, 18)
                        .addComponent(jtfComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23)
                        .addGap(113, 113, 113))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(170, 170, 170))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfUF, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel24.setText("*Repetir Senha:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel12)
                            .addComponent(jtfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addComponent(jLabel9)
                            .addComponent(jtfNome)
                            .addComponent(jpSenha)
                            .addComponent(jLabel24)
                            .addComponent(jpSenhaR))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jtfTelefone))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtfNasc, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jtfCPF)
                                    .addComponent(jtfRG)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel2))
                                        .addGap(0, 0, Short.MAX_VALUE))))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfRG, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jpSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jpSenhaR, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        comboEmpresa.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("*Empresa:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboEmpresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(comboEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        btnAdicionar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdicionar.setText("Cadastrar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarjButton6ActionPerformed(evt);
            }
        });

        btnLocalizar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLocalizar.setText("Cancelar");
        btnLocalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BotoesLayout = new javax.swing.GroupLayout(Botoes);
        Botoes.setLayout(BotoesLayout);
        BotoesLayout.setHorizontalGroup(
            BotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotoesLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                .addComponent(btnLocalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );
        BotoesLayout.setVerticalGroup(
            BotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLocalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Botoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Botoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLocalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocalizarActionPerformed
        dispose();
    }//GEN-LAST:event_btnLocalizarActionPerformed

    private void btnAdicionarjButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarjButton6ActionPerformed
        String v = verificaCampos();
        if (v.equals("")) {
            FuncionarioBEAN f = null;
            f = getDados();
            cadastrar(f);

        } else {
            JOptionPane.showMessageDialog(null, v);
        }
    }//GEN-LAST:event_btnAdicionarjButton6ActionPerformed

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

    /*   private Email pegaDadosEmailEX(String desti) {
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

    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Botoes;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnLocalizar;
    private javax.swing.JComboBox<String> comboEmpresa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPasswordField jpSenha;
    private javax.swing.JPasswordField jpSenhaR;
    private javax.swing.JTextField jtfBairro;
    private javax.swing.JTextField jtfCEP;
    private javax.swing.JTextField jtfCPF;
    private javax.swing.JTextField jtfCidade;
    private javax.swing.JTextField jtfComplemento;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfLogradouro;
    private javax.swing.JTextField jtfNasc;
    private javax.swing.JTextField jtfNome;
    private javax.swing.JTextField jtfNumero;
    private javax.swing.JTextField jtfRG;
    private javax.swing.JTextField jtfTelefone;
    private javax.swing.JTextField jtfUF;
    // End of variables declaration//GEN-END:variables

    private String verificaCampos() {
        String retorno = "";
        if (jtfNome.getText().equals("")) {
            retorno = "Campo Nome";
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
        if (jtfLogradouro.getText().equals("")) {
            retorno += ", Logradouro";
        }
        if (jtfNumero.getText().equals("")) {
            retorno += ", Número";
        }
        if (jtfBairro.getText().equals("")) {
            retorno += ", Bairro";
        }
        if (jtfCidade.getText().equals("")) {
            retorno += ", Cidade";
        }
        if (jtfCEP.getText().equals("")) {
            retorno += ", CEP";
        }
        if (jtfUF.getText().equals("")) {
            retorno += ", UF";
        }

        if (!retorno.equals("")) {
            retorno += " se encontra(ão) 'vazio(s)', preencha-o(s) por gentileza!!";
        }
        return retorno;
    }

    private FuncionarioBEAN getDados() {
        FuncionarioBEAN f = new FuncionarioBEAN();
        System.out.println(Time.formataDataUS(jtfNasc.getText()));
        f.setCPF(jtfCPF.getText() + "");
        f.setDataNacimento(Time.formataDataUS(jtfNasc.getText()));
        f.setEmail(jtfEmail.getText() + "");
        f.setLogradouro(jtfLogradouro.getText());
        f.setNumero(jtfNumero.getText());
        f.setBairro(jtfBairro.getText());
        f.setComplemento(jtfComplemento.getText());
        f.setCidade(jtfCidade.getText());
        f.setCep(jtfCEP.getText());
        f.setUf(jtfUF.getText());
        f.setNome(jtfNome.getText() + "");
        f.setRG(jtfRG.getText() + "");
        f.setSenha(jpSenha.getText() + "");
        f.setTelefone(jtfTelefone.getText() + "");

        return f;
    }

    private void limpaCampos() {

        jtfNasc.setText("");
        jtfCPF.setText("");
        jtfEmail.setText("");
        jtfNome.setText("");
        jtfRG.setText("");
        jpSenha.setText("");
        jtfTelefone.setText("");
        jtfLogradouro.setText("");
        jtfNumero.setText("");
        jtfBairro.setText("");
        jtfComplemento.setText("");
        jtfCidade.setText("");
        jtfCEP.setText("");
        jtfUF.setText("");

    }

    /*private void preencheCampos(FuncionarioBEAN f) throws ParseException {
        //selvet de listar um funcionario
        DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
        Date date2 = formatUS.parse(f.getDataNacimento());
//Depois formata data
        DateFormat formatBR = new SimpleDateFormat("dd-mm-yyyy");
        String dateFormated2 = formatBR.format(date2);
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

    }*/
    private void cadastrar(FuncionarioBEAN f) {
        Carregamento a = new Carregamento(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                a.setVisible(true);

            }
        });
        SharedPreferencesEmpresaBEAN sh = SharedPEmpresa_Control.listar();
        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        System.out.println("email = " + sh.getEmpEmail() + " , senha = " + sh.getEmpSenha());
        final Call<Void> call = api.insereFuncionario(new Gson().toJson(f), sh.getEmpEmail(), sh.getEmpSenha());
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
                                limpaCampos();

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

    /*  public void localizar(int codigo) {
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
    }*/
}
