package de.rhojin.server.topic;

import com.google.protobuf.util.JsonFormat;
import de.rhojin.grpc.Topic;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bson.Document;

@NoArgsConstructor(staticName = "create")
public class TopicConverter {

    @SneakyThrows
    public Topic document2topic(Document document) {
        Topic.Builder builder = Topic.newBuilder();
        document.remove("_id"); //remove mongodb specific attribute
        JsonFormat.parser().merge(document.toJson(), builder);
        return builder.build();
    }

    @SneakyThrows
    public Document topic2document(Topic topic) {
        String json = JsonFormat.printer().print(topic);
        Document document = Document.parse(json);
        document.put("_id", topic.getId()); //add mongodb specific attribute
        return document;
    }
}
