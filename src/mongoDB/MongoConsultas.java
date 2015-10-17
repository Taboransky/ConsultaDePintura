/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongoDB;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;

import org.bson.Document;

import static java.util.Arrays.asList;

/**
 *
 * @author Drope
 */
public class MongoConsultas {
    
    public static void obtemTotalArea(){
        
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("plataforma");
        
        MongoCollection<Document> ptCollection = database.getCollection("pt");
        
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
        AggregateIterable<Document> agg = database.getCollection("pt").aggregate(asList(
                new Document("$match", new Document("#nome",java.util.regex.Pattern.compile(regexpN))),
                new Document("$match", new Document("subgrupo-zona",java.util.regex.Pattern.compile(regexpSG))),
                new Document("$group", new Document("_id", "$subgrupo-zona").append("Area", new Document("$sum","$area"))),
                new Document("$sort", new Document("Area",-1))
                //new Document("$sort", new Document("_id",1))
        ));
        
        
        agg.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        });
        
    }
    
    public static void main(String[] args) {
        obtemTotalArea();
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

