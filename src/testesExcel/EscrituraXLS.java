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
                    //System.out.println("Objeto original: " + o.toString());
                    if(o.getClass() == String.class) {
                        String s = o.toString();
                        //System.out.println("Achei uma String: " + s);
                        labelToAdd = new Label(coluna, linha, s);
                        writableSheet.addCell(labelToAdd);

                    } else
                        if (o.getClass() == Double.class) {
                            Double d = Double.parseDouble(o.toString());
                            //System.out.println("Achei um Double: "+ d);
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
                    //System.out.println("Objeto original: " + o.toString());
                    if(o.getClass() == String.class) {
                        String s = o.toString();
                        //System.out.println("Achei uma String: " + s);
                        labelToAdd = new Label(coluna, linha, s);
                        writableSheet.addCell(labelToAdd);

                    } else
                        if (o.getClass() == Double.class) {
                            Double d = Double.parseDouble(o.toString());
                            //System.out.println("Achei um Double: "+ d);
                            numToAdd = new Number(coluna, linha, d);
                            writableSheet.addCell(numToAdd);
                        }
                    coluna++;
                    if(coluna>1) {
                        linha++;
                        coluna=0;
                    }
                }
            
            /*
            for(String s : list){
                
                labelToAdd = new Label(coluna, linha, s);
                writableSheet.addCell(labelToAdd);
                
                coluna++;
                if(coluna>(1)) {
                    linha++;
                    coluna=0;
                }
            }
            */
            
            /*
            Label label = new Label(0, 0, "Label (String)");
            DateTime date = new DateTime(1, 0, new Date());
            Boolean bool = new Boolean(2, 0, true);
            Number num = new Number(3, 0, 9.99);
            
            //Add the created Cells to the sheet
            //writableSheet.addCell(label);
            */
 
            //Write and close the workbook
            writableWorkbook.write();
            writableWorkbook.close();
            System.out.println("Arquivo salvo: " + exlFile.getName());
 
        } catch(Exception e)
       {System.out.println(e.getMessage());};
        }
    }
