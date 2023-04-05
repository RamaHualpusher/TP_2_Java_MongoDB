package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ConsultasMongoDB {
    public static void main(String[] args) {
        MongoClient mongoClient = null;
        try {
            mongoClient = conectarMongoDB();
            MongoCollection<Document> collection = getCollection(mongoClient);
            consultaRegionAmericasPoblacionMayorA100Millones(collection);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }finally {
            System.out.println("Fin del programa");
            mongoClient.close();
        }


    }


    public static MongoClient conectarMongoDB() {
        return MongoClients.create("mongodb://localhost:27017");
    }
    public static MongoCollection<Document> getCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase("paises_db").getCollection("paises");
    }
    public static void consultaRegionAmericasPoblacionMayorA100Millones(MongoCollection<Document> collection) {
        // Crea un filtro compuesto para buscar documentos donde la región sea "Americas" y la población sea mayor a 100 millones
        Bson filter = Filters.and(
                Filters.eq("region", "Americas"),
                Filters.gt("poblacion", 100000000)
        );

        // Realiza la consulta y recorre los resultados para imprimirlos por pantalla
        FindIterable<Document> results = collection.find(filter);
        for (Document doc : results) {
            //System.out.println(doc.toJson());
            imprimirDocument(doc);
        }
    }

    public static void imprimirDocument(Document doc) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonFormateado = null;
        try {
            jsonFormateado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jsonFormateado);
    }
}
