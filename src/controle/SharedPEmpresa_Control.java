/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import modelo.CargoBEAN;
import modelo.local.SharedPreferencesEmpresaDAO;
import modelo.local.SharedPreferencesEmpresaBEAN;
import util.Criptografia;

/**
 *
 * @author Daniel
 */
public class SharedPEmpresa_Control {

    public static void inserir(SharedPreferencesEmpresaBEAN sh) {
        SharedPreferencesEmpresaDAO sha = new SharedPreferencesEmpresaDAO();
        logOFF();
        sha.inserir(sh);
    }

    public static SharedPreferencesEmpresaBEAN listar() {
        SharedPreferencesEmpresaDAO sha = new SharedPreferencesEmpresaDAO();
        SharedPreferencesEmpresaBEAN sh = sha.listar();
        if (sh.getEmpCodigo() != 0) {
            String senha = Criptografia.criptografar(sh.getEmpSenha());
            sh.setEmpSenha(senha);
        }
        return sh;
    }

    public static SharedPreferencesEmpresaBEAN listarLogin() {
        SharedPreferencesEmpresaDAO sha = new SharedPreferencesEmpresaDAO();
        return sha.listar();
    }

    public static void logOFF() {
        SharedPreferencesEmpresaDAO sha = new SharedPreferencesEmpresaDAO();
        sha.excluir();
    }
}
