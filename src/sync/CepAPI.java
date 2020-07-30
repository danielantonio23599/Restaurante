package sync;
import modelo.Endereco;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface CepAPI {
   @GET("{CEP}/json/")
   Call<Endereco> getEnderecoByCEP(@Path("CEP") String CEP);

}
