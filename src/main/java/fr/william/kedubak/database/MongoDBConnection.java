package fr.william.kedubak.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String DATABASE_NAME = System.getenv("MONGO_DATABASE");
    private static final String CONNECTION_STRING = System.getenv("MONGO_CONNECTION_STRING");

    private static MongoClient mongoClient;

    public static void connect() {
        try {
            ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            mongoClient = MongoClients.create(settings);
            System.out.println("Connexion à la base de données réussie.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            connect();
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connexion à la base de données fermée.");
        }
    }
}
