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
        
        AggregateIterable<Document> iterable = database.getCollection("pt").aggregate(asList(
        new Document("$group", new Document("_id", "$#subgrupo").append("area", new Document("$sum","$area")))));
        
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        });
        
    }
    
}
