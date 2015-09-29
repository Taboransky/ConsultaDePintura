/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testeLeituraXLS;


import com.sun.rowset.internal.Row;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import jxl.read.biff.BiffException;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.json.*;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.util.Arrays.asList;
/**
 *
 * @author bapho
 */
public class LeituraXLS {
    
   public static void main(String[] args) 
   {
       MongoClient mongoClient = new MongoClient(); //conect ao MongoDB
       MongoDatabase db = mongoClient.getDatabase("plataforma");//Conecta ao banco de dados
       
       
       
       System.out.println("tamo na Main");
       try{ 
           testaLer(db);
       } catch(Exception e)
       {System.out.println(e.getMessage());};
      
   }
   
   public static void testaLer( MongoDatabase db ) throws IOException, BiffException{
        System.out.println("Entramo na funcao");
       
        File folder = new File("/plataformFiles");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> listOfFileName = new ArrayList<String>();
        
        
        for (File file : listOfFiles) {
            if (file.isFile()) {
                listOfFileName.add(file.getName());
            }
        }
 
        WorkbookSettings ws = new WorkbookSettings();// resolver o problema de encoding.
        ws.setEncoding("Cp1252");//enconding com utf-8
       
        Workbook workbook = Workbook.getWorkbook(new File("/plataformFiles/P-56_M49_S01.xls"),ws);
        System.out.println("li o arquivo");
        Sheet sheet = workbook.getSheet(0);
      
      
        JSONObject json = new JSONObject();
      
        JSONArray rows = new JSONArray();
      
      
        for(int i=1;i<sheet.getRows();i++){
            JSONObject jRow = new JSONObject();
            JSONArray cells = new JSONArray();
            Document doc = new Document(); //Mudei para o Tipo Document, pq o mongo pede esse tipo ao invez do json, ele faz a convercao dele de document para json
            
            for(int j=0;j<sheet.getColumns();j++){
                  Cell cell=sheet.getCell(j,i);
                  if(  cell.getContents() != "" ){
                    jRow.put( sheet.getCell(j,0).getContents(),  cell.getContents() );
                    doc.append(sheet.getCell(j,0).getContents(),  cell.getContents());
                  }
            }

               //rows.put( jRow );
               db.getCollection("pt").insertOne( doc );//salvar os documentos na collection pt. 
               //System.out.println(jRow);
             
        }
      

    // Create the JSON.
    //json.put( "rows", rows );

    // Get the JSON text.
    //System.out.println(json.toString());

      
    workbook.close();
    
   }

}
