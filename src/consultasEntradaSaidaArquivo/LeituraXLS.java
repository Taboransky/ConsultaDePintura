/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package consultasEntradaSaidaArquivo;


import com.mongodb.DBCollection;
import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import jxl.read.biff.BiffException;



import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
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

       try{ 
           leituraDeArquivos(db);
       } catch(Exception e)
       {System.out.println(e.getMessage());};
      
   }

   public static void leituraDeArquivos(MongoDatabase db) throws IOException, BiffException {
       File folder = new File("src/arquivosPlataformaP56");
       File[] listOfFiles = folder.listFiles();

       MongoCollection myCollection = db.getCollection("pt");
       myCollection.drop();
       
       for (File file : listOfFiles) {
            if (file.isFile()) {
                lerArquivo(db, file.getName());
            }
        }
   }
   
   public static void lerArquivo( MongoDatabase db, String fileName) throws IOException, BiffException{
        //System.out.println("Entramo na funcao");
        
       String filePath = "src/arquivosPlataformaP56/" + fileName;
       
        WorkbookSettings ws = new WorkbookSettings();// resolver o problema de encoding.
        ws.setEncoding("Cp1252");//enconding com utf-8
       
        Workbook workbook = Workbook.getWorkbook(new File(filePath),ws);
        Sheet sheet = workbook.getSheet(0);
        
        
        for(int linha=1;linha<sheet.getRows();linha++){
            Document doc = new Document(); //Mudei para o Tipo Document, pq o mongo pede esse tipo ao invez do json, ele faz a convercao dele de document para json
            
            for(int coluna=0;coluna<sheet.getColumns();coluna++){
                  Cell cell=sheet.getCell(coluna,linha);
                  
                  //if( !cell.getContents().isEmpty() ) {
                  if( (!cell.getContents().isEmpty()) && (!sheet.getCell(0, linha).getContents().isEmpty()) ) {  
                  
                      String fullName = fileName;
                      String[] noDot = fullName.split("\\.");
                      String[] noUnderline = noDot[0].split("_");
                      String modulo = noUnderline[1];
                      String setor = noUnderline[2];
                        
                      doc.append("#grupo", noDot[0]);
                      doc.append("modulo", modulo);
                      doc.append("setor", setor);
                      
                      
                    if( sheet.getCell(coluna,0).getContents().equals( "&Área (m2)" )){ //precisamos converter de string para numeric para o mongo poder calcular
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
                        
                    } else if( coluna==2 ) {
                        String cellZone = cell.getContents();
                        
                        
                        // Padronizando a coluna de zonas, e tratando de typos (Zona Alta X; Zona X Alta; Zona alta X; Zona altaX; Zona baixa  X; Zona L Alta ;)
                        if (cellZone.matches("(\\w+) (Alta) (\\w+)")) {
                            cellZone = cellZone.replaceAll("(\\w+) (Alta) (\\w+)", "$1 $3 $2");
                        } else if (cellZone.matches("(\\w+) (alta) (\\w+)")){
                            cellZone = cellZone.replaceAll("(\\w+) (alta) (\\w+)", "$1 $3 Alta");
                        } else if(cellZone.matches("(\\w+) (baixa)(\\s+)(\\w+)")) {
                            cellZone = cellZone.replaceAll("(\\w+) (baixa) (\\w+)", "$1 $3");
                        } else if(cellZone.matches("(\\w+) (alta)(\\w+)")){
                            cellZone = cellZone.replaceAll("(\\w+) (alta)(\\w+)", "$1 $3 Alta");
                        } else if(cellZone.matches("(Zona) (\\w+) (Alta)(\\s+)")) {
                            cellZone = cellZone.replaceAll("(Zona) (\\w+) (Alta)(\\s+)", "$1 $2 $3");
                        }
                        doc.append("subgrupo-zona", cellZone);
                          
                    } else {
                        doc.append(sheet.getCell(coluna,0).getContents(),  cell.getContents());
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
    
    
    // modulo & setor
    // db.pt.aggregate([ {$group:{_id:{"modulo":"$modulo","setor":"$setor"},total:{$sum:"$area"}}},  {$sort: { modulo: -1 }} ])
   } 

}
