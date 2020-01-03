package sync;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




/**
 * Created by itzda on 07/06/2018.
 */

public class SyncDefault {

    /* public static final Retrofit RETROFIT_DIGA = new Retrofit.Builder().
             baseUrl("http://diga-servidor-diga-servidor.7e14.starter-us-west-2.openshiftapps.com").
             addConverterFactory(GsonConverterFactory.create()).
             build();
 */
    
    public static final Retrofit RETROFIT_RESTAURANTE = new Retrofit.Builder().
            baseUrl("http://localhost:8080/RestauranteServer/").
            addConverterFactory(GsonConverterFactory.create()).
            build();
}
