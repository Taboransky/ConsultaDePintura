/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testesExcel;

import java.io.File;
import java.util.List;
 
import jxl.*;
import jxl.write.*;
import jxl.write.Number;

public class EscrituraXLS {
    
    public static void main(String[] args) {
        
    }
 
    
    
    public static void writeDiameter(List<Object> list){
        Label labelToAdd;
        Number numToAdd;
        int coluna=0, linha=1;
        
        System.out.println(".size() da lista = " + list.size());
        try {
            File exlFile = new File("src/output/write_test2.xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,"Flange"));
            writableSheet.addCell(new Label(1,0,"Diametro 1"));
            writableSheet.addCell(new Label(2,0,"Diametro 2"));
            
                for(Object o : list) {
                    if(o.getClass() == String.class) {
                        String s = o.toString();
                        labelToAdd = new Label(coluna, linha, s);
                        writableSheet.addCell(labelToAdd);

                    } else
                        if (o.getClass() == Double.class) {
                            Double d = Double.parseDouble(o.toString());
                            numToAdd = new Number(coluna, linha, d);
                            writableSheet.addCell(numToAdd);
                        }
                    coluna++;
                    if(coluna>2) {
                        linha++;
                        coluna=0;
                    }
                }
            
            writableWorkbook.write();
            writableWorkbook.close();
            
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    // função feita pra query de aggregate de zona (2 elementos: 1 string, 1 num)
    public static void writeZoneArea(List<String> list) {
        Label labelToAdd;
        Number numToAdd;
        int linha=1, coluna=0;
        
        try {
            File exlFile = new File("src/output/write_test2.xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,"Zona"));
            writableSheet.addCell(new Label(1,0,"Area Total"));
            
            for(Object o : list) {
                    if(o.getClass() == String.class) {
                        String s = o.toString();
                        labelToAdd = new Label(coluna, linha, s);
                        writableSheet.addCell(labelToAdd);

                    } else
                        if (o.getClass() == Double.class) {
                            Double d = Double.parseDouble(o.toString());
                            numToAdd = new Number(coluna, linha, d);
                            writableSheet.addCell(numToAdd);
                        }
                    coluna++;
                    if(coluna>1) {
                        linha++;
                        coluna=0;
                    }
                }
 
            //Write and close the workbook
            writableWorkbook.write();
            writableWorkbook.close();
            System.out.println("Arquivo salvo: " + exlFile.getName());
 
        } catch(Exception e)
       {System.out.println(e.getMessage());};
    }
    

    public static void writeZonaHH( List<String> listNames, List<Double> listArea, List<Double> listPrice, List<Double> listHomenM2, List<Double> listaTrataMentoSuperficiePreco,List<Double> listaApliocacaoDeTintaAltoDesempenhoProco,List<Double> listaEquipamentoPreco ) {
        Label labelToAdd;
        Number numToAdd;
        int linha=1, coluna=0;
        
        try {
            File exlFile = new File("src/output/ZonaHH.xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,"Zona"));
            writableSheet.addCell(new Label(1,0,"Area Total"));
            writableSheet.addCell(new Label(2,0,"Preco tratamento de superficie"));
            writableSheet.addCell(new Label(3,0,"Preco aplicacao de tinta de alto desempenho"));
            writableSheet.addCell(new Label(4,0,"Preco equipamento"));
            writableSheet.addCell(new Label(5,0,"Preco total"));
            writableSheet.addCell(new Label(6,0,"Homem M2"));
            
            //Nomes da zonas
            for( String nome : listNames){
                labelToAdd = new Label(0, linha, nome);
                writableSheet.addCell(labelToAdd);
                linha++;
            }
            
            linha=1;
            //Area total
            for( Double area : listArea){
                numToAdd = new Number(1, linha, area);
                writableSheet.addCell(numToAdd);
                linha++;
            }
            
            linha=1;
            //Preco do tratamento de superficie
            for( Double area : listaTrataMentoSuperficiePreco){
                numToAdd = new Number(2, linha, area);
                writableSheet.addCell(numToAdd);
                linha++;
            }
            
            linha=1;
            //preco tinta de alto desempenho
            for( Double price : listaApliocacaoDeTintaAltoDesempenhoProco){
                numToAdd = new Number(3, linha, price);
                writableSheet.addCell(numToAdd);
                linha++;
            }
            
            linha=1;
            //preco do equipamento
            for( Double price : listaEquipamentoPreco){
                numToAdd = new Number(4, linha, price);
                writableSheet.addCell(numToAdd);
                linha++;
            }
            
            linha=1;
            //preco total
            for( Double price : listPrice){
                numToAdd = new Number(5, linha, price);
                writableSheet.addCell(numToAdd);
                linha++;
            }
            
            linha=1;
            //Homem por metro quadrado
            for( Double hh : listHomenM2){
                numToAdd = new Number(6, linha, hh);
                writableSheet.addCell(numToAdd);
                linha++;
            }
            

            //Write and close the workbook
            writableWorkbook.write();
            writableWorkbook.close();
            System.out.println("Arquivo salvo: " + exlFile.getName());
 
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
