/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Daniel
 */
public class Util {

    private static String[] quebraString(String qrCode) {
        return qrCode.split(";");
    }

    private static String geraStringQRCodeVenda(int venda, int mesa) {
        return venda + ";" + mesa;
    }

}
