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
    
   
   public LeituraXLS(){
   
   }
    
    
   public static void lerArquivos() 
   {
       MongoClient mongoClient = new MongoClient(); //conect ao MongoDB
       MongoDatabase db = mongoClient.getDatabase("plataforma");//Conecta ao banco de dados
       
       
       
       System.out.println("tamo na Main");
       try{ 
           lerTudo(db);
       } catch(Exception e)
       {System.out.println(e.getMessage());};
      
   }

   public static void lerTudo(MongoDatabase db) throws IOException, BiffException {
       //File folder = new File("/plataformFiles");
       File folder = new File("src/documentos_geiza");
       File[] listOfFiles = folder.listFiles();
       
       
       for (File file : listOfFiles) {
            if (file.isFile()) {
                testaLer(db, file.getName());
            }
        }
       
   }
   
   public static void testaLer( MongoDatabase db, String fileName) throws IOException, BiffException{
        //System.out.println("Entramo na funcao");
        
       String filePath = "src/documentos_geiza/" + fileName;
       
        WorkbookSettings ws = new WorkbookSettings();// resolver o problema de encoding.
        ws.setEncoding("Cp1252");//enconding com utf-8
       
        Workbook workbook = Workbook.getWorkbook(new File(filePath),ws);
        System.out.println("li o arquivo");
        Sheet sheet = workbook.getSheet(0);
      
      
        for(int i=1;i<sheet.getRows();i++){
            Document doc = new Document(); //Mudei para o Tipo Document, pq o mongo pede esse tipo ao invez do json, ele faz a convercao dele de document para json
            
            for(int j=0;j<sheet.getColumns();j++){
                  Cell cell=sheet.getCell(j,i);
                  if(  !cell.getContents().isEmpty() ){
                    
                    if( sheet.getCell(j,0).getContents().equals( "&Área (m2)" )){ //precisamos converter de string para numeric para o mongo poder calcular
                        double value;
                                
                        if(cell.getType() != CellType.NUMBER){
                            String tempCell = "" + cell.getContents();
                            tempCell = tempCell.replace(",", ".");
                            value = Double.parseDouble(tempCell);
                        }
                        else {
                            NumberCell nc = (NumberCell) cell;
                            value = nc.getValue();
                        }
                        doc.append("area",  value);
                        
                    } else if( j==2 ) {
                        String cellZone = cell.getContents();
                        
                        
                        // Padronizando a coluna de zonas, e tratando de typos (Zona Alta X; Zona X Alta; Zona alta X; Zona altaX; Zona baixa  X)
                        if (cellZone.matches("(\\w+) (Alta) (\\w+)") || cellZone.matches("(\\w+) (alta) (\\w+)")){
                            cellZone = cellZone.replaceAll("(\\w+) (Alta) (\\w+)", "$1 $3 $2");
                            cellZone = cellZone.replaceAll("(\\w+) (alta) (\\w+)", "$1 $3 Alta");
                        } else if(cellZone.matches("(\\w+) (baixa)(\\s+)(\\w+)")) {
                            cellZone = cellZone.replaceAll("(\\w+) (baixa) (\\w+)", "$1 $4");
                        } else if(cellZone.matches("(\\w+) (alta)(\\w+)")){
                            cellZone = cellZone.replaceAll("(\\w+) (alta)(\\w+)", "$1 $3 Alta");
                        }
                        
                        doc.append("subgrupo-zona", cellZone);
                          
                    } else {
                        doc.append(sheet.getCell(j,0).getContents(),  cell.getContents());
                    }
                      
                  }
            }
               db.getCollection("pt").insertOne( doc );//salvar os documentos na collection pt.
        }

        
    workbook.close();
    
    //comandos importantes:
    // Deletar toda a collection db.pt.remove({})
    // db.pt.findOne({"#nome":"FLG-04A 420"})
    // it
    
    // db.pt.count({ "#subgrupo" : "zona A"})
    // db.pt.count({"supgrupo-zona":/.*Alta.*/})
    // db.pt.find().limit(-1).skip(x).next()   // pegar registro aleatório
    
    
    // Item 1): db.pt.aggregate([ {$match:{"#nome":/.*/} }, {$match:{"subgrupo-zona":/.*Alta.*/} }, {$group:{_id:"$subgrupo-zona",total:{$sum:"$area"}}}, {$sort: { total: -1 }} ])
    // Item 2): ( db.pt.stats() * 100 / db.pt.count({"supgrupo-zona":/.*Alta.*/}) )
                 // db.pt.count({"subgrupo-zona":"Zona C Alta","#nome":/.*/})
    
    // Item 3): db.pt.find({"#nome":/.*/,"*NPD":/.*/},{"*NPD":1,"_id":0,"#nome":1}).pretty()
   } 

}
