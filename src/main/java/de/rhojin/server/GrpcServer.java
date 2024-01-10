package de.rhojin.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.rhojin.config.Config;
import de.rhojin.config.ConfigReader;
import de.rhojin.server.note.NoteManager;
import de.rhojin.server.note.NoteService;
import de.rhojin.server.topic.TopicManager;
import de.rhojin.server.topic.TopicService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Config config = ConfigReader.create().read();
        System.out.println(config);


        MongoClient mongoClient = MongoClients.create("mongodb://" + config.mongoDb.ip + ":" + config.mongoDb.port);
        NoteManager noteManager = new NoteManager(mongoClient, config.mongoDb.databaseName, config.mongoDb.notesCollectionName);
        TopicManager topicManager = new TopicManager(mongoClient, config.mongoDb.databaseName, config.mongoDb.topicsCollectionName);

        System.out.println("start grpc server ...");
        Server server = ServerBuilder
                .forPort(config.grpcPort)
                .addService(new NoteService(noteManager))
                .addService(new TopicService(topicManager, noteManager))
                .build();

        server.start();
        server.awaitTermination();
    }
}