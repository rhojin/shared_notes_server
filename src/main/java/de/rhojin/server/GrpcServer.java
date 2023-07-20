package de.rhojin.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoManager mongoManager = new MongoManager(mongoClient, "sharedNotesDb", "notes");

        System.out.println("start grpc server ...");
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new NoteService(mongoManager)).build();

        server.start();
        server.awaitTermination();
    }
}