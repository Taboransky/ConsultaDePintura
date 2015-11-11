/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testesExcel;


import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import jxl.read.biff.BiffException;



import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;


import jxl.CellType;
import jxl.NumberCell;
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
           //testaLer(db);
           lerTudo(db);
       } catch(Exception e)
       {System.out.println(e.getMessage());};
      
   }
   
   public static void lerTudo(MongoDatabase db) throws IOException, BiffException {
       //File folder = new File("/plataformFiles");
       File folder = new File("src/documentos_geiza");
       File[] listOfFiles = folder.listFiles();
       //ArrayList<String> listOfFileName = new ArrayList<String>();
       
       
       for (File file : listOfFiles) {
            if (file.isFile()) {
                //listOfFileName.add(file.getName());
                testaLer(db, file.getName());
            }
        }
       
   }
   
   public static void testaLer( MongoDatabase db, String fileName) throws IOException, BiffException{
        //System.out.println("Entramo na funcao");
        
        String filePath = "/plataformFiles/" + fileName;
 
        WorkbookSettings ws = new WorkbookSettings();// resolver o problema de encoding.
        ws.setEncoding("Cp1252");//enconding com utf-8
       
        Workbook workbook = Workbook.getWorkbook(new File(filePath),ws);
        System.out.println("li o arquivo");
        Sheet sheet = workbook.getSheet(0);
      
      
        //JSONObject json = new JSONObject();
        //JSONArray rows = new JSONArray();
      
        for(int i=1;i<sheet.getRows();i++){
            //JSONObject jRow = new JSONObject();
            //JSONArray cells = new JSONArray();
            Document doc = new Document(); //Mudei para o Tipo Document, pq o mongo pede esse tipo ao invez do json, ele faz a convercao dele de document para json
            
            for(int j=0;j<sheet.getColumns();j++){
                  Cell cell=sheet.getCell(j,i);
                  if(  !cell.getContents().isEmpty() ){
                   // jRow.put( sheet.getCell(j,0).getContents(),  cell.getContents() );
                    
                    if( sheet.getCell(j,0).getContents().equals( "&Área (m2)" )){ //precisamos converter de string para numeric para o mongo poder calcular
                        /*
                        if (A.getType() == CellType.NUMBER) {
                            NumberCell nc = (NumberCell) A;
                            double doubleA = nc.getValue();
                            // this is a double containing the exact numeric value that was stored 
                            // in the spreadsheet
                        }
                        */
                        double value;
                                
                        if(cell.getType() != CellType.NUMBER){
                            String tempCell = "" + cell.getContents();
                                // sout pra testar
                                //System.out.println("String nao numero: " + tempCell);
                            tempCell = tempCell.replace(",", ".");
                                // sout pra testar
                                //System.out.println("String após replace: " + tempCell);
                            value = Double.parseDouble(tempCell);
                        }
                        else {
                            NumberCell nc = (NumberCell) cell;
                            value = nc.getValue();
                        }
                                
                        //System.out.println("value: " + value + " vem do excel: " + cell.getContents());
                        
                        doc.append("area",  value);
                    } else if( j==2 ) {
                        String cellZone = cell.getContents();
                        
                        if (cellZone.matches("(\\w+) (Alta) (\\w+)")){
                            //System.out.println("Achei um! " + cellZone);
                            cellZone = cellZone.replaceAll("(\\w+) (Alta) (\\w+)", "$1 $3 $2");
                            //System.out.println("Novo valor: " + cellZone);
                        }
                        doc.append("subgrupo-zona", cellZone);
                          
                    } else {
                        doc.append(sheet.getCell(j,0).getContents(),  cell.getContents());
                    }
                      
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
    
    //comandos importantes:
    // Deletar toda a collection db.pt.remove({})
    // db.pt.findOne({"#nome":"FLG-04A 420"})
    // db.pt.aggregate([{ $group : { _id:"$#subgrupo",total: {$sum:"$area"} } }])
    // db.pt.count({ "#subgrupo" : "zona A"})
    // db.pt.count({"supgrupo-zona":/.*Alta.*/})
	// db.pt.find().limit(-1).skip(x).next()   // pegar registro aleatório
    
    
    // Item 1): db.pt.aggregate([ {$match:{"#nome":/.*/} }, {$match:{"subgrupo-zona":/.*Alta.*/} }, {$group:{_id:"$subgrupo-zona",total:{$sum:"$area"}}}, {$sort: { total: -1 }} ])
    // Item 2): ( db.pt.stats() * 100 / db.pt.count({"supgrupo-zona":/.*Alta.*/}) )
                 // db.pt.count({"subgrupo-zona":"Zona C Alta","#nome":/.*/})
    
    // Item 3): db.pt.find({"#nome":/.*/,"*NPD":/.*/},{"*NPD":1,"_id":0,"#nome":1}).pretty()
   } 

}
