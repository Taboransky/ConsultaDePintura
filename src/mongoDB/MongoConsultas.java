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
        //obtemAreaPorZona();
        obtemAreaPorSetor();
        //obtemTotalArea(); 
        //obtemDiametroFlanges();
    }
    
    //calcular o custo por zona
    public static void obtemAreaPorZona(){
    
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexNome = "^.*$";
        
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
            new Document("$match",new Document("#nome",java.util.regex.Pattern.compile(regexNome))),
            new Document("$group",new Document("_id","$subgrupo-zona").append("Total", new Document("$sum","$area"))),
            new Document("$sort", new Document("_id",1))
        ));
    
        List<Object> listO = new ArrayList<>();
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                int control = 1;  //variável para controlar a leitura (estava gravando dobrado)
                for(Object o : document.values()) {
                    if(control==1){
                        listO.add(document.getString("_id"));
                        control = 2;
                    } else {
                        listO.add(document.getDouble("Total"));
                        control = 1;
                    }
                }
            }
        });
        
        CalculosMetricas.CalculoMetricas(listO);
    }
    
    public static void obtemAreaPorSetor(){
        
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexNome = "^.*$";
        
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
            new Document("$match",new Document("#nome",java.util.regex.Pattern.compile(regexNome))),
            new Document("$group",new Document("_id","$#grupo").append("Total", new Document("$sum","$area"))),
            new Document("$sort", new Document("_id",1))
        ));
        
        
        List<Object> listO = new ArrayList<>();
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                int control=1;
                for(Object o : document.values()) {
                    if(control==1){
                        listO.add(document.getString("_id"));
                        control = 2;
                    } else {
                        listO.add(document.getDouble("Total"));
                        control = 1;
                    }
                }
            }
        });
        
        CalculosMetricas.CalculoMetricas(listO);
    }
    
    
    public static void obtemDiametroFlanges() {
        
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexFLG = "^FLG.*$";
        List<String> listStringFromDocument = new ArrayList<>();
        
        List<Object> listO = new ArrayList<>();
        
        
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(Document document) {
                int column=0; // colum indica cada célula/key do elemento
                for(Object o : document.values()){
                    if (o.toString().equals(document.getString("*Comp no")) && column!=1){ // tive que botar column!=1 pois alguns #nome eram iguais a *Comp no (ou seja, duplicava)
                        listStringFromDocument.add(document.getString("*Comp no")); 
                        listStringFromDocument.add(document.getString("*NPD")); //salva primeiro *Comp no e *NPD em um array de String
                    }
                    column++;
                }
            }
        };
        ptCollection.find(eq("*Comp no",java.util.regex.Pattern.compile(regexFLG))).sort(new Document("*Comp no",1)).forEach(printBlock);
        
        
        System.out.println("Tamanho FINAL de listString: " + listStringFromDocument.size());
        
        for(int j=0;j<listStringFromDocument.size();j++){  //lista string tem formato: *Comp, *NPD, *Comp, *NPD, *Comp, *NPD, etc...
            if (j%2==0){
                listO.add(listStringFromDocument.get(j));
            } else {
                String[] parts = listStringFromDocument.get(j).replaceAll("\"", "").split(" "); // separa *NPD 1" 1"  em 2 elementos 1
                
                listO.add(inchesToCentimeters(parts[0]));
                listO.add(inchesToCentimeters(parts[1]));
            }
        }
        EscrituraXLS.writeDiameter(filterSameCompNo2(listO));

        // db.pt.find({"*Comp no":"FLG","*NPD":/.*/},{"*NPD":1,"*Comp no":1}).pretty()
    }
    
    
    
    public static void obtemTotalArea(){
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        
        System.out.println(ptCollection.count());
        
        String regexpN = "^.*$";
        String regexpSG =  "^.*Alta.*$";
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
                new Document("$match", new Document("#nome",java.util.regex.Pattern.compile(regexpN))),
                new Document("$match", new Document("subgrupo-zona",java.util.regex.Pattern.compile(regexpSG))),
                new Document("$group", new Document("_id", "$subgrupo-zona").append("Area", new Document("$sum","$area"))),
                new Document("$sort", new Document("Area",-1))
        ));
        
        
        List<String> sToPass = new ArrayList<>();
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                for(Object o : document.values()) {
                    sToPass.add(o.toString());
                }     
            }
        });
        EscrituraXLS.writeZoneArea(sToPass);
    }
    
    
    
      public static List<Object> filterSameCompNo2(List<Object> originalList) {
        List<Object> returnList = new ArrayList<>();
        String singularComp=originalList.get(0).toString();
        double d1=0, d2=0;
        
        System.out.println("originalList.size() : " + originalList.size());
        for(int i=0; i<originalList.size(); i++){
            if(i%3==0){ // 3 colunas
                if(originalList.get(i).toString().equals(singularComp)) { // se já existir essa Label, somar os diametros
                    d1 += Double.parseDouble( originalList.get(i+1).toString() );
                    d2 += Double.parseDouble( originalList.get(i+2).toString() );
                    i += 2;
                } else {
                    returnList.add(singularComp); //se não, gravar na nova Lista
                    returnList.add(d1);
                    returnList.add(d2);
                    
                    singularComp = originalList.get(i).toString(); // e pegar um novo Label e zerar os diametros
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
        
        if(inches.contains("1/2")) {
            if(inches.contains("-1/2"))
            {
                String[] parts = inches.split("-");
                aux = Double.parseDouble(parts[0]) + 0.5;
            } else {
                aux = 0.5;
            }
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
        }
        aux = aux * INCH_TO_CM;
        
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

