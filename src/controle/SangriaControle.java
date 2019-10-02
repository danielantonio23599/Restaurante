/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modelo.SangriaBEAN;
import modelo.SangriaDAO;

/**
 *
 * @author Daniel
 */
public class SangriaControle {

    private SangriaDAO s = new SangriaDAO();

    public void cadastrar(SangriaBEAN sangria) {
        s.adicionar(sangria);
    }

}
