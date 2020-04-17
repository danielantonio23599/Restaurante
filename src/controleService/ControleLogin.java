/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleService;

import controle.SharedPEmpresa_Control;
import controle.SharedP_Control;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import modelo.local.SharedPreferencesBEAN;
import modelo.local.SharedPreferencesEmpresaBEAN;


/**
 *
 * @author Daniel
 */
public class ControleLogin {

    public void logIN(SharedPreferencesBEAN f) {
        SharedP_Control.inserir(f);
    }

    public void logEmpresa(SharedPreferencesEmpresaBEAN e) {
        SharedPEmpresa_Control.inserir(e);
    }

    public SharedPreferencesEmpresaBEAN listarEmpresa() {
        return SharedPEmpresa_Control.listar();
    }

    public DefaultComboBoxModel buscar(String email) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        ArrayList<String> pe = SharedP_Control.buscar(email);
        for (String p : pe) {
            modelo.addElement(p);
        }
        return modelo;

    }
}
