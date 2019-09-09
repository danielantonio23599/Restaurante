/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import modelo.FuncionarioBEAN;
import modelo.FuncionarioDAO;
import modelo.Produtos;
import modelo.SharedPreferencesBEAN;
import modelo.SharedPreferencesDAO;

/**
 *
 * @author Daniel
 */
public class ControleLogin {

    private FuncionarioDAO f = new FuncionarioDAO();

    public int login(String email, String senha) {
        int funcionario = f.Login(email, senha);
        return funcionario;
    }

    public DefaultComboBoxModel buscar(String produto) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        ArrayList<String> pe = f.buscar(produto);
        for (String p : pe) {
            modelo.addElement(p);

        }

        return modelo;

    }

}
