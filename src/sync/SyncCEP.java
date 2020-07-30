package sync;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncCEP {
    public static final Retrofit RETROFIT_CEP() {
        return new Retrofit.Builder().
                baseUrl("https://viacep.com.br/ws/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
    }
}
