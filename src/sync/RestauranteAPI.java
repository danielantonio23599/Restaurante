package sync;

import java.util.ArrayList;
import java.util.List;
import modelo.CargoBEAN;
import modelo.FuncionarioBEAN;
import modelo.local.SharedPreferencesBEAN;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by itzda on 07/06/2018.
 */
public interface RestauranteAPI {

    //Servlets oficiais do OpenShift
//    @FormUrlEncoded
//    @POST("/diga_api/FazLogin")
//    Call<Usuario> fazLogin(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
//
//    @FormUrlEncoded
//    @POST("/diga_api/FazSignin")
//    Call<Void> fazSignin(@Field("usuario") String usuario);
//
//    @FormUrlEncoded
//    @POST("/diga_api/PesquisaOcorrencia")
//    Call<List<Ocorrencia>> pesquisaOcorrencia(@Field("query") String query, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
//
//    @FormUrlEncoded
//    @POST("/diga_api/InsereOcorrencia")
//    Call<Void> insereOcorrencia(@Field("ocorrencia") String ocorrencia, @Field("fotoOcorrencia") String foto, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
//
//    @FormUrlEncoded
//    @POST("/diga_api/InsereOcorrencias")
//    Call<Void> insereOcorrencias(@Field("ocorrencia") String ocorrencias, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("fotos") String fotos);
//
//    @FormUrlEncoded
//    @POST("/diga_api/CurteOcorrencia")
//    Call<Void> curteOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codigoOcorrencia") int codigoOcorrencia, @Field("latitude") double latitude, @Field("longitude") double longitude);
//
//    @FormUrlEncoded
//    @POST("/diga_api/DescurteOcorrencia")
//    Call<Void> descurteOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("usuario") String usuario, @Field("ocorrencia") String ocorrencia);
//
//    @FormUrlEncoded
//    @POST("/diga_api/ReportaOcorrencia")
//    Call<Void> reportaOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("usuarioReportaOcorrencia") String usuarioReportaOcorrencia);
//
//    @FormUrlEncoded
//    @POST("/diga_api/DesreportaOcorrencia")
//    Call<Void> desreportaOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("usuario") String usuario, @Field("ocorrencia") String ocorrencia);
//
//    //@FormUrlEncoded
//    @POST("/diga_api/PreenchimentoInicialBD")
//    Call<List<Object>> preenchimentoInicialBD();
//
//    @FormUrlEncoded
//    @POST("/diga_api/PaginacaoOcorrencia")
//    Call<List<Ocorrencia>> fazPaginacaoOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("pagina") String pagina, @Field("rowsPorPagina") String rowsPorPagina);
//
//    @FormUrlEncoded
//    @POST("/diga_api/PegaDadosPorCodigo")
//    Call<Ocorrencia> pegaDadosOcorrenciaPorCodigo(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codigoOcorrencia") int codigoOcorrencia);
    // Servlets para testes no servidor local
    @FormUrlEncoded
    @POST("restaurante_server/FazLogin")
    Call<SharedPreferencesBEAN> fazLogin(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarCargo")
    Call<ArrayList<CargoBEAN>> listarCargos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ExcluiCargo")
    Call<Void> excluiCargo(@Field("cargo") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarCargoFuncionario")
    Call<CargoBEAN> listarCargoFuncionario(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codFuncionario") int cod);

    @FormUrlEncoded
    @POST("restaurante_server/InsereCargo")
    Call<Void> insereCargo(@Field("cargo") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/AtualizaCargo")
    Call<Void> atualizaCargo(@Field("cargo") String cargo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    /*
    @FormUrlEncoded
    @POST("diga_api/FazSignin")
    Call<Void> fazSignin(@Field("usuario") String usuario);  //, @Field("fotoUsuario") String fotoUsuario

    @FormUrlEncoded
    @POST("diga_api/PesquisaOcorrencia")
    Call<List<Ocorrencia>> pesquisaOcorrencia(@Field("query") String query, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("diga_api/InsereOcorrencia")
    Call<Void> insereOcorrencia(@Field("ocorrencia") String ocorrencia, @Field("fotoOcorrencia") String foto, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("diga_api/InsereOcorrencias")
    Call<Void> insereOcorrencias(@Field("ocorrencia") String ocorrencias, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("diga_api/CurteOcorrencia")
    Call<Void> curteOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codigoOcorrencia") int codigoOcorrencia, @Field("latitude") double latitude, @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("diga_api/DescurteOcorrencia")
    Call<Void> descurteOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("usuario") int usuario, @Field("ocorrencia") int ocorrencia);

    @FormUrlEncoded
    @POST("diga_api/ReportaOcorrencia")
    Call<Void> reportaOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codigoOcorrencia") int codigoOcorrencia, @Field("latitude") double latitude, @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("diga_api/DesreportaOcorrencia")
    Call<Void> desreportaOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("usuario") int usuario, @Field("ocorrencia") int ocorrencia);

    //@FormUrlEncoded
    @POST("diga_api/PreenchimentoInicialBD")
    Call<List<Object>> preenchimentoInicialBD();

    @FormUrlEncoded
    @POST("diga_api/PaginacaoOcorrencia")
    Call<List<Ocorrencia>> fazPaginacaoOcorrencia(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("pagina") String pagina, @Field("rowsPorPagina") String rowsPorPagina);

    @FormUrlEncoded
    @POST("diga_api/PesquisaOcorrenciaPorTexto")
    Call<List<Ocorrencia>> pesquisaOcorrenciaPorTexto(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("textoPesquisar") String textoPesquisar);

    @FormUrlEncoded
    @POST("diga_api/PegaDadosPorCodigo")
    Call<Ocorrencia> pegaDadosOcorrenciaPorCodigo(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codigoOcorrencia") int codigoOcorrencia);

    @POST("diga_api/PegaDadosMapaPrimario")
    Call<List<Ocorrencia>> pegaDadosMapaPrimario();

    @FormUrlEncoded
    @POST("diga_api/PegaDadosMapaSecundario")
    Call<Ocorrencia> pegaDadosMapaSecundario(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("codOcorrencia") int codOcorrencia, @Field("codUsuario") String codUsuario);

    @FormUrlEncoded
    @POST("diga_api/AtualizaFeed")
    Call<List<Ocorrencia>> atualizaFeed(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("dataInicial") String dataInicial, @Field("usuCodigo") int usuCodigo);
     */
}
