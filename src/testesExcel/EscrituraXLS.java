/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testesExcel;

import java.io.File;
import java.util.Date;
import java.util.List;
 
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.Boolean;

public class EscrituraXLS {
    
    public static void main(String[] args) {
        
    }
 
    // função feita pra query de aggregate de zona (2 elementos: 1 string, 1 num)
    public static void writeFromList(List<String> list) {
        Label labelToAdd;
        Number numToAdd;
        int linha=0 , coluna=0;
        
        try {
            File exlFile = new File("src/output/write_test.xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            for(String s : list){
                //System.out.println("Coluna: "+coluna+"\nLinha: "+linha);
                
                if(coluna == 0){
                    labelToAdd = new Label(coluna, linha, s);
                    writableSheet.addCell(labelToAdd);
                } else {
                    numToAdd = new Number(coluna,linha,Double.parseDouble(s));
                    writableSheet.addCell(numToAdd);
                }
                coluna++;
                if(coluna>1) {
                    linha++;
                    coluna=0;
                }
                
            }
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
 
        } catch(Exception e)
       {System.out.println(e.getMessage());};
        }
    }
