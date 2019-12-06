/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleService;

import controle.SharedP_Control;
import modelo.CargoBEAN;
import modelo.FuncionarioBEAN;
import modelo.local.SharedPreferencesBEAN;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sync.RestauranteAPI;
import sync.SyncDefault;

/**
 *
 * @author Daniel
 */
public class ControleCargo {
    private CargoBEAN u = null;

    public CargoBEAN cargo_do_funcionario() {

        RestauranteAPI api = SyncDefault.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        SharedPreferencesBEAN sh = SharedP_Control.listar();
        final Call<CargoBEAN> call = api.listarCargoFuncionario(sh.getFunEmail(), sh.getFunSenha(), sh.getFunCodigo());

        call.enqueue(new Callback<CargoBEAN>() {
            @Override
            public void onResponse(Call<CargoBEAN> call, Response<CargoBEAN> response) {
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        CargoBEAN u = response.body();
                        SharedP_Control.inserCargo(u);
                    } else {
                        // senha ou usuario incorreto

                    }
                } else {
                    //servidor fora do ar
                }

            }

            @Override
            public void onFailure(Call<CargoBEAN> call, Throwable t) {
                //Servidor fora do ar

            }
        });

        return u;
    }

}
