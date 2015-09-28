/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testeLeituraXLS;


import com.sun.rowset.internal.Row;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import jxl.read.biff.BiffException;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.json.*;

/**
 *
 * @author bapho
 */
public class LeituraXLS {
    
   public static void main(String[] args) 
   {
       
       System.out.println("tamo na Main");
       try{ 
           testaLer();
       } catch(Exception e)
       {System.out.println(e.getMessage());};
      
   }
   
   public static void testaLer() throws IOException, BiffException{
       System.out.println("Entramo na funcao");
       
       Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\bapho\\Documents\\NetBeansProjects\\ConsultaDePintura\\src\\testeLeituraXLS\\Book2.xls"));
       
       System.out.println("li o arquivo");
      Sheet sheet = workbook.getSheet(0);
      
      
      JSONObject json = new JSONObject();
      
      JSONArray rows = new JSONArray();
      
      
       for(int i=0;i<sheet.getRows();i++)
       {
           JSONObject jRow = new JSONObject();
           JSONArray cells = new JSONArray();
           
         for(int j=0;j<sheet.getColumns();j++)
         {
               Cell cell=sheet.getCell(j,i);
          
                
                jRow.put( sheet.getCell(j,0).getContents(),  cell.getContents() );
         }
         
            rows.put( jRow );
         
       }
      

    // Create the JSON.
    json.put( "rows", rows );

    // Get the JSON text.
    System.out.println(json.toString());

      
      workbook.close();
   }

}
