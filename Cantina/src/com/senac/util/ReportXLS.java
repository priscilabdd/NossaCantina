/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.senac.util;

import com.senac.dao.VendaDao;
import com.senac.model.Venda;
import com.senac.model.VendaReport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * 
 */
public class ReportXLS {

    private final HSSFCell COLUNA[] = new HSSFCell[3];
    private final String CABECALHOUSUARIO[] = {"Código da Venda", "Data", "Total"};
    private final HSSFCell COLUNAFUNCIONARIO[] = new HSSFCell[4];
    private final String CABECALHOFUNCIONARIO[] = {"Código da Venda", "Data", "Total", "Funcionário"};
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet s = wb.createSheet();
    HSSFRow r = null;

    public void reportUsuario(int matricula) throws SQLException {
        int i = 1;
        r = s.createRow(0);
        for (int j = 0; j < CABECALHOUSUARIO.length; j++) {
            COLUNA[j] = r.createCell(j);
            COLUNA[j].setCellValue(CABECALHOUSUARIO[j]);
        }
        VendaDao vd = new VendaDao();
        for (Venda v : vd.reportUsuario(matricula)) {
            r = s.createRow(i);
            COLUNA[0] = r.createCell(0);
            COLUNA[0].setCellValue(v.getCod_venda());
            COLUNA[1] = r.createCell(1);
            COLUNA[1].setCellValue(v.getDataVenda());
            COLUNA[2] = r.createCell(2);
            COLUNA[2].setCellValue(v.getTotalVenda());
            i++;
        }
        try {
            FileOutputStream out = new FileOutputStream(salvarReport());
            wb.write(out);
            out.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void reportPorFuncionario() throws SQLException {
        int i = 1;
        r = s.createRow(0);
        for (int j = 0; j < CABECALHOFUNCIONARIO.length; j++) {
            COLUNAFUNCIONARIO[j] = r.createCell(j);
            COLUNAFUNCIONARIO[j].setCellValue(CABECALHOFUNCIONARIO[j]);
        }
        VendaDao vd = new VendaDao();
        for (VendaReport vr : vd.reportFuncionario()) {
            r = s.createRow(i);
            COLUNAFUNCIONARIO[0] = r.createCell(0);
            COLUNAFUNCIONARIO[0].setCellValue(vr.getCodVenda());
            COLUNAFUNCIONARIO[1] = r.createCell(1);
            COLUNAFUNCIONARIO[1].setCellValue(vr.getDataVenda());
            COLUNAFUNCIONARIO[2] = r.createCell(2);
            COLUNAFUNCIONARIO[2].setCellValue(vr.getTotalVenda());
            COLUNAFUNCIONARIO[3] = r.createCell(3);
            COLUNAFUNCIONARIO[3].setCellValue(vr.getNome());
            i++;
        }
        try {
            FileOutputStream out = new FileOutputStream(salvarReport());
            wb.write(out);
            out.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public String salvarReport() {
        JFileChooser fc = new JFileChooser();
        String arquivo = null;
        try {
            int result = fc.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                arquivo = fc.getSelectedFile().toString().concat(".xls");
                File file = new File(arquivo);
                if (file.exists()) {
                    Object[] options = {"Sim", "Não"};
                    int opcao = JOptionPane.showOptionDialog(null, "Já existe um arquivo com este nome.\nDeseja substituir?", "Atenção!!!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (opcao == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Report gerado com sucesso.", "", 1);
                    } else {
                        salvarReport();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Report gerado com sucesso.", "", 1);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Erro ao gerar Report.", 2);
        }
        return arquivo;
    }
}
