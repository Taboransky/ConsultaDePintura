/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consultasMongoDB;

import com.mongodb.BasicDBObject;
import consultasEntradaSaidaArquivo.EscrituraXLS;

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

import consultasCalculos.CalculosMetricas;

/**
 *
 * @author Drope
 */
public class MongoConsultas {

    public static void obtemCruzamentoDeDadosPorDoisParametros(String primeiroParamentroDeBusca, String segundoParametroDeBusca) {
        // db.pt.aggregate([ {$group:{_id:{"modulo":"$modulo","setor":"$setor"},total:{$sum:"$area"}}},  {$sort: { modulo: -1 }} ])
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexNome = "^.*$";
        
        String nomePrimeiroParametroDeProcura = primeiroParamentroDeBusca; 
        String nomePrimeiroParametroDeProcura2 = segundoParametroDeBusca;
        
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
            new Document("$match",new Document("modulo",java.util.regex.Pattern.compile(regexNome))),
            new Document("$group",new Document("_id",new Document(nomePrimeiroParametroDeProcura,"$"+nomePrimeiroParametroDeProcura)
                                                            .append(nomePrimeiroParametroDeProcura2,"$"+nomePrimeiroParametroDeProcura2))
                                                            .append("Total", new Document("$sum","$area"))),
            new Document("$sort", new Document("Total",1))
        ));
        
        List<Object> listO = new ArrayList<>();
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                
                int control = 1;  //variável para controlar a leitura (estava gravando dobrado)
                for(Object o : document.values()) {
                    if(control==1){
                        Document aux = (Document) document.get("_id");
                        String modAux = aux.getString(nomePrimeiroParametroDeProcura);
                        String setAux = aux.getString(nomePrimeiroParametroDeProcura2);
                        
                        listO.add(modAux);
                        listO.add(setAux);
                        control = 2;                        
                    } else {
                        listO.add(document.getDouble("Total"));
                        control = 1;
                    }
                }
            }
        });
        
        CalculosMetricas.CalculoMetricasDeDoisParametrosDeBusca(listO, nomePrimeiroParametroDeProcura, nomePrimeiroParametroDeProcura2 );
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

