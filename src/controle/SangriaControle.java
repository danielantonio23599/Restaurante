/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.util.ArrayList;
import modelo.SangriaBEAN;
import modelo.SangriaDAO;

/**
 *
 * @author Daniel
 */
public class SangriaControle {

    private SangriaDAO s = new SangriaDAO();
    private CaixaControle c = new CaixaControle();

    public void cadastrar(SangriaBEAN sangria) {
        s.adicionar(sangria);
    }

    public ArrayList<SangriaBEAN> listarSangriasCaixa() {
        return s.buscar(c.getCaixa());
    }
    public float getTotalSangriasCaixa() {
        return s.getTotalSangriasCaixa(c.getCaixa());
    }

}
