
import java.text.SimpleDateFormat;
import java.util.Date;
import visao.FRMLogin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
public class Time {

    public static void main(String args[]) {
       try {
            SimpleDateFormat formatoRetorno = new SimpleDateFormat("dd-MM-yyyy");
            Date dataBanco = formatoRetorno.parse("11-11-1111");
            SimpleDateFormat formatoDataBanco = new SimpleDateFormat("yyyy-MM-dd");            
            

            System.out.println(dataBanco);
            System.out.println(formatoDataBanco.format(dataBanco));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
