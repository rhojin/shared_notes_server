package de.rhojin.server.note;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.rhojin.grpc.Note;
import de.rhojin.grpc.Topic;
import de.rhojin.server.topic.TopicConverter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class NoteManager {

    private final MongoClient client;
    private final String databaseName;
    private final String collectionName;

    public Set<Note> load() {
        MongoCollection<Document> collection = getCollection();
        FindIterable<Document> documents = collection.find();
        Set<Note> result = new HashSet<>();
        for (Document document : documents) {
            Note note = NoteConverter.create().document2note(document);
            result.add(note);
        }
        return result;
    }

    public void create(Note note) {
        Document document = NoteConverter.create().note2document(note);
        getCollection().insertOne(document);
    }

    public void update(Note note) {
        Document document = NoteConverter.create().note2document(note);
        getCollection().replaceOne(Filters.eq("_id", note.getId()), document);
    }

    public void updateByTopic(Topic topic) {
        Bson filterQuery = Filters.eq("topic.id", topic.getId());
        Document topicDocument = TopicConverter.create().topic2document(topic);
        topicDocument.remove("_id");
        Bson updateQuery = Updates.set("topic", topicDocument);
        getCollection().updateMany(filterQuery, updateQuery);
    }

    public void delete(Note note) {
        Document document = NoteConverter.create().note2document(note);
        getCollection().deleteOne(document);
    }

    public void deleteByTopic(Topic topic) {
        Bson query = Filters.eq("topic.id", topic.getId());
        getCollection().deleteMany(query);
    }

    private MongoCollection<Document> getCollection() {
        return client.getDatabase(databaseName).getCollection(collectionName);
    }

}
