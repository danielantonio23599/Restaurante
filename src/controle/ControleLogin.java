/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modelo.FuncionarioDAO;

/**
 *
 * @author Daniel
 */
public class ControleLogin {

    private FuncionarioDAO f = new FuncionarioDAO();

    public int login(String email, String senha) {

        return f.Login(email, senha);
    }
}
