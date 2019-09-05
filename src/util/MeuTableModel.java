/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel
 */
public class MeuTableModel extends AbstractTableModel {

    private String[] colunas;
    private Object[][] dados;

    public MeuTableModel(String[] colunas, Object[][] dados){
        this.colunas = colunas;
        this.dados = dados;
    }

    public int getRowCount() {
        return dados.length;
    }

    public int getColumnCount() {
        return colunas.length;
    }

    public Object getValueAt(int linhaIndex, int colunaIndex) {
        return dados[linhaIndex][colunaIndex];
    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    @Override
    public Class<?> getColumnClass(int colunaIndex) {
        return dados[0][colunaIndex].getClass();
    }

    @Override  
    public boolean isCellEditable(int linhaIndex, int colunaIndex) {
        Object ob = dados[0][colunaIndex]; 
        if(ob instanceof Boolean){
            return true;
        }        
        return false;
    }

    @Override    
    public void setValueAt(Object aValue, int linhaIndex, int colunaIndex) {    
        Object ob = dados[linhaIndex][colunaIndex];
        if(ob instanceof Boolean){  
            dados[linhaIndex][colunaIndex] = (Boolean) aValue;  
        }  
    }

}
