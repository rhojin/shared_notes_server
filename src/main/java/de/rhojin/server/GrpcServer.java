package de.rhojin.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.rhojin.config.Config;
import de.rhojin.config.ConfigReader;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Config config = ConfigReader.create().read();
        System.out.println(config);


        MongoClient mongoClient = MongoClients.create("mongodb://" + config.mongoDb.ip + ":" + config.mongoDb.port);
        MongoManager mongoManager = new MongoManager(mongoClient, config.mongoDb.databaseName, config.mongoDb.collectionName);

        System.out.println("start grpc server ...");
        Server server = ServerBuilder
                .forPort(config.port)
                .addService(new NoteService(mongoManager)).build();

        server.start();
        server.awaitTermination();
    }
}