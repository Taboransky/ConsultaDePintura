/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongoDB;

import com.mongodb.BasicDBObject;
import testesExcel.EscrituraXLS;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import java.util.ArrayList;

import org.bson.Document;

import static java.util.Arrays.asList;
import java.util.List;

import consultadepintura.CalculosMetricas;

/**
 *
 * @author Drope
 */
public class MongoConsultas {
    
    
    public static void main(String[] args) {
        obtemAreaPorSetor();
        //obtemTotalArea(); 
        //obtemDiametroFlanges();
    }
    
    
    public static void obtemAreaPorSetor(){
        // db.pt.aggregate([{$match:{"#nome":/.*/}},{$group:{_id:"$#grupo",total:{$sum:"$area"}}},{$sort:{_id:1}}])
        
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexNome = "^.*$";
        
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
            new Document("$match",new Document("#nome",java.util.regex.Pattern.compile(regexNome))),
            new Document("$group",new Document("_id","$#grupo").append("Total", new Document("$sum","$area"))),
            new Document("$sort", new Document("_id",1))
        ));
        
        
        List<String> stringListFromDocument = new ArrayList<>();
        List<Object> listO = new ArrayList<>();
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                //System.out.println(document.toJson());
                
                for(Object o : document.values()) {
                    //System.out.println(o.getClass().getName() +" : " + o.toString());
                    //stringListFromDocument.add(o.toString());
                    listO.add(document.getString("_id"));
                    listO.add(document.getDouble("Total"));
                }
            }
        });
        
        /*
        System.out.println("Final: ");
        for(int i=0; i<listO.size();i++){
            System.out.println(listO.get(i) + ": " + listO.get(i+1));
            i += 1;
        }
        */
        CalculosMetricas.CalculoMetricas(listO);
        
    }
    
    
    public static void obtemDiametroFlanges() {
        
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexFLG = "^FLG.*$";
        //String regexFLG = "^FLG-04A$";
        List<String> listStringFromDocument = new ArrayList<>();
        
        List<Object> listO = new ArrayList<>();
        
        
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(Document document) {
                //System.out.println(document.toJson());
                //System.out.println(document.getString("*Comp no") +" - "+ document.getString("#nome") + " - " + document.getString("*NPD"));
                int column=0; // colum indica cada célula/key do elemento
                //System.out.println("Tamanho de .values().size = "+document.values().size());
                for(Object o : document.values()){
                   // System.out.println("o to String retorna: " + o.toString());
                    if (o.toString().equals(document.getString("*Comp no")) && column!=1){ // tive que botar column!=1 pois alguns #nome eram iguais a *Comp no (ou seja, duplicava)
                        listStringFromDocument.add(document.getString("*Comp no")); 
                        listStringFromDocument.add(document.getString("*NPD")); //salva primeiro *Comp no e *NPD em um array de String
                        
                        //System.out.println(document.getString("*Comp no"));
                        //System.out.println(document.getString("*NPD"));
                    }
                    //System.out.println("Coluna: " + column);
                    column++;
                }
                //System.out.println("Tamanho de listString: " + listStringFromDocument.size());
            }
        };
        //ptCollection.find(eq("*Comp no",java.util.regex.Pattern.compile(regexFLG))).limit(2).forEach(printBlock);
        ptCollection.find(eq("*Comp no",java.util.regex.Pattern.compile(regexFLG))).sort(new Document("*Comp no",1)).forEach(printBlock);
        
        
        System.out.println("Tamanho FINAL de listString: " + listStringFromDocument.size());
        
        for(int j=0;j<listStringFromDocument.size();j++){  //lista string tem formato: *Comp, *NPD, *Comp, *NPD, *Comp, *NPD, etc...
            if (j%2==0){
                listO.add(listStringFromDocument.get(j));
            } else {
                //String[] parts = listFromDocument.get(j).split(" ");
                String[] parts = listStringFromDocument.get(j).replaceAll("\"", "").split(" "); // separa *NPD 1" 1"  em 2 elementos 1
                
                listO.add(inchesToCentimeters(parts[0]));
                listO.add(inchesToCentimeters(parts[1]));
            }
        }
        
        System.out.println("listO.size() : " + listO.size());
        EscrituraXLS.writeDiameter(filterSameCompNo2(listO));
        //EscrituraXLS.writeDiameter(listO); //listO = [String, double, double]

        // db.pt.find({"*Comp no":"FLG-04A","*NPD":/.*/},{"*NPD":1,"*Comp no":1}).pretty()
    }
    
    
    
    public static void obtemTotalArea(){
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        
        System.out.println(ptCollection.count());
        
        /*
        AggregateIterable<Document> iterable = database.getCollection("pt").aggregate(asList(
        new Document("$group", new Document("_id", "$#subgrupo").append("Area", new Document("$sum","$area")))));
        
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        });
        */
        
        String regexpN = "^.*$";
        String regexpSG =  "^.*Alta.*$";
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
                new Document("$match", new Document("#nome",java.util.regex.Pattern.compile(regexpN))),
                new Document("$match", new Document("subgrupo-zona",java.util.regex.Pattern.compile(regexpSG))),
                new Document("$group", new Document("_id", "$subgrupo-zona").append("Area", new Document("$sum","$area"))),
                new Document("$sort", new Document("Area",-1))
                //new Document("$sort", new Document("_id",1))
        ));
        
        
        List<String> sToPass = new ArrayList<>();
        //List<Object> listO = new ArrayList<>();
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                //System.out.println(document.toJson());
                for(Object o : document.values()) {
                    //System.out.println(o.getClass().getName() +" : " + o.toString());
                    sToPass.add(o.toString());
                    //listO.add(o);
                }     
            }
            
        });
        EscrituraXLS.writeZoneArea(sToPass);
        //EscrituraXLS.writeZoneArea(listO);
    }
    
    
    
      public static List<Object> filterSameCompNo2(List<Object> originalList) {
        List<Object> returnList = new ArrayList<>();
        String singularComp=originalList.get(0).toString();
        double d1=0, d2=0;
        
        System.out.println("originalList.size() : " + originalList.size());
        for(int i=0; i<originalList.size(); i++){
            if(i%3==0){
                if(originalList.get(i).toString().equals(singularComp)) {
                    d1 += Double.parseDouble( originalList.get(i+1).toString() );
                    d2 += Double.parseDouble( originalList.get(i+2).toString() );
                    i += 2;
                } else {
                    returnList.add(singularComp);
                    returnList.add(d1);
                    returnList.add(d2);
                    
                    
                    singularComp = originalList.get(i).toString();
                    d1 = Double.parseDouble( originalList.get(i+1).toString() );
                    d2 = Double.parseDouble( originalList.get(i+2).toString() );
                    i += 2;
                }
            }
        }
        System.out.println("returnList.size(): " + returnList.size());
        
        return returnList;
    }
    
    
    
    public static double inchesToCentimeters(String inches) {
        double aux;
        final double INCH_TO_CM = 2.54;
        
        //System.out.println("Entrou: " + inches);
        
        if(inches.contains("1/2")) {
            if(inches.contains("-1/2"))
            {
                String[] parts = inches.split("-");
                aux = Double.parseDouble(parts[0]) + 0.5;
            } else {
                aux = 0.5;
            }
            //System.out.println("Uma string com meios: Antes: "+parts[0]+" ; depois de convertido: " + aux);
        } else 
        if(inches.contains("3/4")) {
            if(inches.contains("-3/4"))
            {
                String[] parts = inches.split("-");
                aux = Double.parseDouble(parts[0]) + 0.75;
            } else {
                aux = 0.75;
            }
            
        } else  {
            aux = Double.parseDouble(inches);
            //System.out.println("Não alterou: " + aux);
        }
        aux = aux * INCH_TO_CM;
        //cm = String.valueOf(aux);
        
        //System.out.println("Valor final de aux: " + aux + "\n\n");
        
        return aux;
    }
    
    
    
    public static MongoCollection<Document> initiateMongoCollection(){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("plataforma");
        MongoCollection<Document> ptCollection = database.getCollection("pt");
        return ptCollection;
    }
    
    
    

    
    
    // db.pt.aggregate([
    //  { $group : { _id:"$#subgrupo",total: {$sum:"$area"} } }
    // ])
    
    // db.pt.aggregate([
    //  {$match:{"#nome":/.*/} },
    //  {$match:{"subgrupo-zona":/.*Alta.*/} },
    //  {$group:{_id:"$subgrupo-zona",total:{$sum:"$area"}}},
    //  {$sort: { total: -1 }}
    // ])
    
     
}

