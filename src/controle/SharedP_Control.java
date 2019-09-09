/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modelo.FuncionarioBEAN;
import modelo.FuncionarioDAO;
import modelo.SharedPreferencesBEAN;
import modelo.SharedPreferencesDAO;

/**
 *
 * @author Daniel
 */
public class SharedP_Control {

    private static FuncionarioDAO f = new FuncionarioDAO();
    SharedPreferencesDAO sha = new SharedPreferencesDAO();

    public void inserir(int funcionario) {
        logOFF();
        SharedPreferencesBEAN s = new SharedPreferencesBEAN();
        FuncionarioBEAN fun = f.localizar(funcionario);
        s.setFunCodigo(funcionario);
        s.setFunNome(fun.getNome());
        s.setFunEmail(fun.getEmail());
        s.setFunCargo(fun.getCargo());
        sha.inserir(s);
    }

    public SharedPreferencesBEAN listar() {
        return sha.listarALl();
    }

    public void logOFF() {
        sha.excluir();
    }
}
