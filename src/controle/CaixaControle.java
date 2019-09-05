/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.IOException;
import modelo.CaixaBEAN;
import modelo.CaixaDAO;

/**
 *
 * @author Daniel
 */
public class CaixaControle {

    private CaixaDAO c = new CaixaDAO();

    public boolean isCaixaAberto() {
        CaixaBEAN caixa = c.listar();
        if (caixa.getCodigo() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getCaixa() {
        CaixaBEAN ca = this.listar();
        return ca.getCodigo();
    }

    public String abrirCaixa(CaixaBEAN c) {

        this.c.abrirCaixa(c);
        return "Abriu!!";

    }

    public CaixaBEAN listar() {
        return c.listar();
    }

}
