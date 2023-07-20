package de.rhojin.server;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.rhojin.grpc.Note;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class MongoManager {

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

    public void delete(Note note) {
        Document document = NoteConverter.create().note2document(note);
        getCollection().deleteOne(document);
    }

    private MongoCollection<Document> getCollection() {
        return client.getDatabase(databaseName).getCollection(collectionName);
    }

}
