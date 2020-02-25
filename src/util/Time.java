/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(data);
    }

    public static String adicionarMeses(int quantidadeMeses) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        // c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 10);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + quantidadeMeses);
        //c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);

        String a = new  SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return a;
    }

}
