package de.rhojin.server.topic;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.rhojin.grpc.Topic;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

//TODO: ReH NoteManager and TopicManager are very similar => extract generic manager class
@RequiredArgsConstructor
public class TopicManager {
    private final MongoClient client;
    private final String databaseName;
    private final String collectionName;

    public Set<Topic> load() {
        MongoCollection<Document> collection = getCollection();
        FindIterable<Document> documents = collection.find();
        Set<Topic> result = new HashSet<>();
        for (Document document : documents) {
            Topic topic = TopicConverter.create().document2topic(document);
            result.add(topic);
        }
        return result;
    }

    public void create(Topic topic) {
        Document document = TopicConverter.create().topic2document(topic);
        getCollection().insertOne(document);
    }

    public void update(Topic topic) {
        Document document = TopicConverter.create().topic2document(topic);
        getCollection().replaceOne(Filters.eq("_id", topic.getId()), document);
    }

    public void delete(Topic topic) {
        Document document = TopicConverter.create().topic2document(topic);
        getCollection().deleteOne(document);
    }

    private MongoCollection<Document> getCollection() {
        return client.getDatabase(databaseName).getCollection(collectionName);
    }
}
