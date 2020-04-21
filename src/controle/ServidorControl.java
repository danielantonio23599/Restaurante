/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modelo.local.ServidorBEAN;
import modelo.local.ServidorDAO;

/**
 *
 * @author Daniel
 */
public class ServidorControl {

    public static void inserir(ServidorBEAN s) {
        logOFF();
        ServidorDAO d = new ServidorDAO();
        d.inserir(s);
    }

    public static ServidorBEAN listar() {
        ServidorDAO d = new ServidorDAO();
        return d.listar();
    }

    public static void logOFF() {
        ServidorDAO d = new ServidorDAO();
        d.excluir();
    }

}
