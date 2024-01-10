package de.rhojin.server.note;

import de.rhojin.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoteService extends NoteServiceGrpc.NoteServiceImplBase {


    private final NoteManager noteManager;
    private final Map<Integer, Note> idToNote;

    public NoteService(NoteManager noteManager) {
        this.noteManager = noteManager;
        idToNote = noteManager.load()
                .stream()
                .collect(Collectors.toMap(Note::getId, Function.identity()));
    }

    @Override
    public void getAllNotes(Empty request, StreamObserver<Notes> responseObserver) {
        System.out.println("getNotes()");

        Notes response = Notes.newBuilder().addAllNote(idToNote.values()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getNotesByTopic(Topic request, StreamObserver<Notes> responseObserver) {
        System.out.println("getNotesByTopic() " + request.getText());
        List<Note> notesByTopic = idToNote.values()
                .stream()
                .filter(note -> Objects.equals(note.getTopic(), request))
                .toList();
        Notes response = Notes.newBuilder().addAllNote(notesByTopic).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getNote(NoteId request, StreamObserver<Note> responseObserver) {
        System.out.println("getNote()");
        responseObserver.onNext(idToNote.get(request.getId()));
        responseObserver.onCompleted();
    }

    @Override
    public void createNote(Note request, StreamObserver<Empty> responseObserver) {
        System.out.println("createNote()");
        int id = idToNote.isEmpty() ? 0 : Collections.max(idToNote.keySet()) + 1;
        Note note = Note.newBuilder().setId(id).setIndex(id).setText(request.getText()).setTopic(request.getTopic()).build();
        idToNote.put(note.getId(), note);
        noteManager.create(note);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateNote(Note request, StreamObserver<Empty> responseObserver) {
        System.out.println("updateNote()");
        idToNote.put(request.getId(), request);
        noteManager.update(request);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteNote(NoteId request, StreamObserver<Note> responseObserver) {
        System.out.println("deleteNote()");
        Note note = idToNote.remove(request.getId());
        noteManager.delete(note);
        responseObserver.onNext(note);
        responseObserver.onCompleted();
    }

}
