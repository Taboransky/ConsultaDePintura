/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testesExcel;

import domain.Registro;
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
    

    public static void writeZonaHH(  List<Registro> listaRegistros ) {
        Label labelToAdd;
        Number numToAdd;
        int linha=1, coluna=0;
        
        try {
            File exlFile = new File("src/output/ZonaHH.xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,"Zona"));
            writableSheet.addCell(new Label(1,0,"Area Total"));
            writableSheet.addCell(new Label(2,0,"Wj2"));
            writableSheet.addCell(new Label(3,0,"Wj3"));
            writableSheet.addCell(new Label(4,0,"Preco tratamento de superficie"));
            writableSheet.addCell(new Label(5,0,"Preco aplicacao de tinta de alto desempenho"));
            writableSheet.addCell(new Label(6,0,"Preco equipamento"));
            writableSheet.addCell(new Label(7,0,"Preco total"));
            writableSheet.addCell(new Label(8,0,"Homem M2"));
            
            for( Registro registro : listaRegistros   ){
                labelToAdd = new Label(0, linha, registro.getnomeParametro1());//Obtem o nome
                writableSheet.addCell(labelToAdd);
                
                numToAdd =  new Number(1, linha, registro.getArea());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(2, linha, registro.getWj2());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(3, linha, registro.getWj3());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(4, linha, registro.getTratamentoSuperficiePreco());//Obtem tratamento de superficie
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(5, linha, registro.getAplicacaoDeTintaAltoDesempenhoPreco());//Obtem aplicacao de tinta de alto desempenho
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(6, linha, registro.getEquipamentoPreco());//Obtem preco de equipamento
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(7, linha, registro.getPrecoTotal());//Obtem preco total
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(8, linha, registro.getHomemM2());//Obtem homem M2
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
    
    public static void writeCSVParaDoisParametros(  List<Registro> listaRegistros, String parametro1, String parametro2 ) {
        Label labelToAdd;
        Number numToAdd;
        int linha=1, coluna=0;
        String fileName = "Arquivo"+parametro1+parametro2;
        
        try {
            File exlFile = new File("src/output/"+fileName+".xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,parametro1));
            writableSheet.addCell(new Label(1,0,parametro2));
            writableSheet.addCell(new Label(2,0,"Area Total"));
            writableSheet.addCell(new Label(3,0,"Wj2"));
            writableSheet.addCell(new Label(4,0,"Wj3"));
            writableSheet.addCell(new Label(5,0,"Preco tratamento de superficie"));
            writableSheet.addCell(new Label(6,0,"Preco aplicacao de tinta de alto desempenho"));
            writableSheet.addCell(new Label(7,0,"Preco equipamento"));
            writableSheet.addCell(new Label(8,0,"Preco total"));
            writableSheet.addCell(new Label(9,0,"Homem M2"));
            
            for( Registro registro : listaRegistros   ){
                labelToAdd = new Label(0, linha, registro.getnomeParametro1());//Obtem o nome
                writableSheet.addCell(labelToAdd);
                
                labelToAdd = new Label(1, linha, registro.getnomeParametro2());//Obtem o nome
                writableSheet.addCell(labelToAdd);
                
                numToAdd =  new Number(2, linha, registro.getArea());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(3, linha, registro.getWj2());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(4, linha, registro.getWj3());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(5, linha, registro.getTratamentoSuperficiePreco());//Obtem tratamento de superficie
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(6, linha, registro.getAplicacaoDeTintaAltoDesempenhoPreco());//Obtem aplicacao de tinta de alto desempenho
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(7, linha, registro.getEquipamentoPreco());//Obtem preco de equipamento
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(8, linha, registro.getPrecoTotal());//Obtem preco total
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(9, linha, registro.getHomemM2());//Obtem homem M2
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
