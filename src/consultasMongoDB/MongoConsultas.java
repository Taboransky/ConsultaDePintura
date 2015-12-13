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

    public static void obtemCruzamentoDadosPorParametros(String primeiroParamentroDeBusca, String segundoParametroDeBusca, String terceiroParametroDeBusca) {
        // db.pt.aggregate([ {$group:{_id:{"modulo":"$modulo","setor":"$setor"},total:{$sum:"$area"}}},  {$sort: { modulo: -1 }} ])
    
        String nomeParametroDeProcura = primeiroParamentroDeBusca; 
        String nomeParametroDeProcura2 = segundoParametroDeBusca;
        String nomeParametroDeProcura3 = terceiroParametroDeBusca;
        List<Object> listO = new ArrayList<>();
         
        if( nomeParametroDeProcura3 == ""   ){
            listO = retornaResultadoQueryComDoisParametros(nomeParametroDeProcura,nomeParametroDeProcura2);
            CalculosMetricas.CalculoMetricasDeDoisParametrosDeBusca(listO, nomeParametroDeProcura, nomeParametroDeProcura2 );
        } else {
            listO =  retornaResultadoQueryComTresParametros(nomeParametroDeProcura,nomeParametroDeProcura2,nomeParametroDeProcura3);
            CalculosMetricas.CalculoMetricasParaTresParametrosDeBusca(listO, nomeParametroDeProcura, nomeParametroDeProcura2,nomeParametroDeProcura3 );
        }
    }
    
    private static List<Object> retornaResultadoQueryComDoisParametros( String nomePrimeiroParametroDeProcura, String nomePrimeiroParametroDeProcura2){
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexNome = "^.*$";
        
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
       
        return listO; 
    }
    
    private static List<Object> retornaResultadoQueryComTresParametros( String nomePrimeiroParametroDeProcura, String nomePrimeiroParametroDeProcura2,String nomePrimeiroParametroDeProcura3 ){
        MongoCollection<Document> ptCollection = initiateMongoCollection();
        String regexNome = "^.*$";
        AggregateIterable<Document> agg = ptCollection.aggregate(asList(
            new Document("$match",new Document("modulo",java.util.regex.Pattern.compile(regexNome))),
            new Document("$group",new Document("_id",new Document(nomePrimeiroParametroDeProcura,"$"+nomePrimeiroParametroDeProcura)
                                                            .append(nomePrimeiroParametroDeProcura2,"$"+nomePrimeiroParametroDeProcura2)
                                                            .append(nomePrimeiroParametroDeProcura3,"$"+nomePrimeiroParametroDeProcura3))
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
                        String setAux2 = aux.getString(nomePrimeiroParametroDeProcura3);

                        listO.add(modAux);
                        listO.add(setAux);
                        listO.add(setAux2);
                        control = 2;                        
                    } else {
                        listO.add(document.getDouble("Total"));
                        control = 1;
                    }
                }
            }
        });
         
        return listO; 
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

