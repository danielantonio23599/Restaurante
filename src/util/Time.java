/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Time {

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        return sdf.format(hora);
    }

    public static String getData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String formataDataBR(String data) {
        String dataF = "";
        if (!data.equals("")) {
            if (!data.equals("  -  -    ")) {
                try {
                    SimpleDateFormat formatoDataBanco = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataBanco = formatoDataBanco.parse(data);
                    SimpleDateFormat formatoRetorno = new SimpleDateFormat("dd-MM-yyyy");
                    dataF = formatoRetorno.format(dataBanco);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            return "";
        }
        return dataF;
    }

    public static String formataDataUS(String data) {
        String dataF = "";
        if (!data.equals("")) {
            if (!data.equals("  -  -    ")) {
                try {
                    SimpleDateFormat formatoRetorno = new SimpleDateFormat("dd-MM-yyyy");
                    Date dataBanco = formatoRetorno.parse(data);
                    SimpleDateFormat formatoDataBanco = new SimpleDateFormat("yyyy-MM-dd");

                    dataF = formatoDataBanco.format(dataBanco);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(dataF);
        return dataF;
    }

    public static String adicionarMeses(int quantidadeMeses) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        // c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 10);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + quantidadeMeses);
        //c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);

        String a = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return a;
    }

}
