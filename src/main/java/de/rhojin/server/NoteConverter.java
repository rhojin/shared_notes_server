package de.rhojin.server;

import com.google.protobuf.util.JsonFormat;
import de.rhojin.grpc.Note;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bson.Document;

@NoArgsConstructor(staticName = "create")
public class NoteConverter {

    //TODO: check @SneakyThrows annotation when a proper logger was implemented to see how the log looks like
    @SneakyThrows
    public Note document2note(Document document) {
        Note.Builder builder = Note.newBuilder();
        document.remove("_id"); //remove mongodb specific attribute, this is necessary to construct a Note object
        JsonFormat.parser().merge(document.toJson(), builder);
        return builder.build();
    }

    @SneakyThrows
    public Document note2document(Note note) {
        String json = JsonFormat.printer().print(note);
        Document document = Document.parse(json);
        document.put("_id", note.getId()); //add mongodb specific attribute, this attribute is required by mongodb
        return document;
    }


}
