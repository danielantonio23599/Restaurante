/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.local.ConnectionF;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Daniel
 */
public class GeradorRelatorio {

    private Connection conexao;

    public GeradorRelatorio(Connection conexao) {
        this.conexao = conexao;
    }

    public void geraPdf(String jrxml, Map<String, Object> parametros) {

        try {
            File tmp = new File("relatorios/relatorio.jasper");

            if (!tmp.exists()) { // Arquivo não existe ainda. Relatório não foi compilado.

                JasperCompileManager.compileReportToFile(jrxml); // Compilando antes.
            }
            File arquivo = new File("relatorios/relatorio.pdf");
            String path = arquivo.getAbsolutePath();
            OutputStream saida = new FileOutputStream(path);
            // compila jrxml em memoria
            tmp = new File("relatorios/relatorio.jasper");
            String jasper = tmp.getAbsolutePath();
            // preenche relatorio
            JasperPrint print = JasperFillManager.fillReport(jasper, parametros, conexao);

            // exporta para pdf
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(saida));

            exporter.exportReport();
            //Aqui vc chama o relatório
            JasperViewer viewer = new JasperViewer(print);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//Coloca em maximizado
            viewer.setTitle("Relatorio");//Coloca um título no relatório
            viewer.setVisible(true);
            System.out.println("sucesso");
        } catch (Exception e) {
            System.out.println("erro : " + e.getMessage());
        }
    }

    public static void exportaPDF(JasperPrint print) {
        try {
            File arquivo = new File("relatorios/relatorio.pdf");
            String path = arquivo.getAbsolutePath();
            OutputStream saida = new FileOutputStream(path);
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(saida));

            exporter.exportReport();

            //Aqui vc chama o relatório
            JasperViewer viewer = new JasperViewer(print);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//Coloca em maximizado
            viewer.setTitle("Relatorio");//Coloca um título no relatório
            viewer.setVisible(true);
            System.out.println("sucesso");
        } catch (Exception e) {
            System.out.println("erro : " + e.getMessage());
        }
    }

}
