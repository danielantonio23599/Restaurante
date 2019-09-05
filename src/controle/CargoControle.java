/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import modelo.CargoBEAN;
import modelo.CargoDAO;

/**
 *
 * @author Daniel
 */
public class CargoControle {

    private CargoDAO c = new CargoDAO();
    private FuncionarioControle f = new FuncionarioControle();

    public ArrayList<CargoBEAN> listarAll() {
        return c.listarALl();
    }

    public String cadastrar(CargoBEAN car) {
        CargoBEAN cargo = c.pegaCodigo(car.getNome());
        if (cargo.getCodigo() == 0) {
            c.adicionar(car);
            return "Cargo adicionado com SUCESSO!!";
        } else {
            return "Cargo do nome '" + car.getNome() + "' já está CADASTRADO!!";
        }
    }

    public CargoBEAN localizarNome(String string) {
        return c.localizarPorNome(string);
    }

    public String editar(CargoBEAN car) {
        CargoBEAN cargo = c.pegaCodigo(car.getNome());
        if (cargo.getCodigo() != 0) {
            c.editar(car);
            return "Cargo Atualizado com SUCESSO!!";
        } else {
            return "Cargo do nome '" + car.getNome() + "'ainda NÃO está CADASTRADO!!";
        }
    }

    public String excluir(String car) {
        CargoBEAN cargo = c.pegaCodigo(car);
        if (f.funCargo(cargo.getCodigo()) == 0) {
            if (cargo.getCodigo() != 0) {
                c.excluir(car);
                return "Cargo EXCLUIDO com SUCESSO!!";
            } else {

                return "Cargo do nome '" + car + "'ainda NÃO está CADASTRADO!!";
            }
        } else {
            return "Cargo esta sendo utilizado, IMPOSSIVEL de ser Excluido!!!";
        }
    }

    public int pegaCodigo(String string) {        
        return c.pegaCodigo(string).getCodigo();
    }

    
}
