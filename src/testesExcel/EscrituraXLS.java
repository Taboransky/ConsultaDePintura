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
    
    
    
    public static void writeZonaHH(List<String> list) {
        Label labelToAdd;
        Number numToAdd;
        int linha=1, coluna=0;
        
        try {
            File exlFile = new File("src/output/ZonaHH.xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,"Zona"));
            writableSheet.addCell(new Label(1,0,"Area Total"));
            writableSheet.addCell(new Label(1,0,"Prices"));
            writableSheet.addCell(new Label(1,0,"Homem M2"));
            
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
 
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
