package sync;

import controle.ServidorControl;
import modelo.local.ServidorBEAN;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by itzda on 07/06/2018.
 */
public class SyncDefault {

    private static String ip = "localhost";
    private String url;
    public static final Retrofit RETROFIT_RESTAURANTE = new Retrofit.Builder().
            baseUrl(getUrl()).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    public static String getUrl() {
        ServidorBEAN ser = ServidorControl.listar();
       ip = ser.getIp();
        if (!ip.equals("")) {
            System.out.println(ip);
            return "http://" + ip + ":8089/RestauranteServer/";
        } else {
            System.out.println("localhost");
            return "http://localhost:8089/RestauranteServer/";
        }
    }
}
