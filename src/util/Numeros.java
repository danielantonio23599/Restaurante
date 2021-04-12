/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DecimalFormat;

/**
 *
 * @author Daniel
 */
public class Numeros {

    public static String formatarFloat(float numero) {
        String retorno = "";
        DecimalFormat formatter = new DecimalFormat("#.00");
        try {
            retorno = formatter.format(numero).replace(",", ".");
        } catch (Exception ex) {
            System.err.println("Erro ao formatar numero: " + ex);
        }
        return retorno;
    }
}
