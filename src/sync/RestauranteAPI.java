package sync;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import modelo.CargoBEAN;
import modelo.FuncionarioBEAN;
import modelo.ProdutoBEAN;
import modelo.Produtos;
import modelo.local.SharedPreferencesBEAN;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by itzda on 07/06/2018.
 */
public interface RestauranteAPI {

    // Servlets para testes no servidor local
    @FormUrlEncoded
    @POST("restaurante_server/FazLogin")
    Call<SharedPreferencesBEAN> fazLogin(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarCargos")
    Call<ArrayList<CargoBEAN>> listarCargos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ExcluirCargo")
    Call<Void> excluiCargo(@Field("cargo") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarCargoFuncionario")
    Call<CargoBEAN> listarCargoFuncionario(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codFuncionario") String cod);

    @FormUrlEncoded
    @POST("restaurante_server/AdicionarCargo")
    Call<Void> insereCargo(@Field("cargo") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/EditarCargo")
    Call<Void> atualizaCargo(@Field("cargo") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarFuncionarios")
    Call<ArrayList<FuncionarioBEAN>> listarFuncionarios(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarFuncionario")
    Call<FuncionarioBEAN> listarFuncionario(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("funcionario") String cod);

    @FormUrlEncoded
    @POST("restaurante_server/GerarNumero")
    Call<Void> gerarNumFunPonto(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/AdicionarFuncionario")
    Call<Void> insereFuncionario(@Field("funcionario") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ExcluirFuncionario")
    Call<Void> excluiFuncionario(@Field("funcionario") String funcionario, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/AdicionarProduto")
    Call<Void> insereProduto(@Field("produto") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/AtualizaProduto")
    Call<Void> atualizaProduto(@Field("produto") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarProdutos")
    Call<ArrayList<ProdutoBEAN>> listarProdutos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ExcluirProduto")
    Call<Void> excluiProduto(@Field("produto") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarProduto")
    Call<ProdutoBEAN> listarProduto(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("produto") String cod);

    @FormUrlEncoded
    @POST("restaurante_server/BuscarUm")
    Call<Produtos> buscarUmProduto(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("produto") String cod);

    @FormUrlEncoded
    @POST("restaurante_server/PesquisaProduto")
    Call<DefaultComboBoxModel> pesquisaProdutos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("produto") String cod);

}
