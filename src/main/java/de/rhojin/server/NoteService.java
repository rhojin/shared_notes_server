package de.rhojin.server;

import de.rhojin.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NoteService extends NoteServiceGrpc.NoteServiceImplBase {


    private final MongoManager mongoManager;
    private final Map<Integer, Note> idToNote;

    public NoteService(MongoManager mongoManager) {
        this.mongoManager = mongoManager;
        idToNote = mongoManager.load()
                .stream()
                .collect(Collectors.toMap(Note::getId, Function.identity()));
    }

    @Override
    public void getNotes(Empty request, StreamObserver<NoteResponse> responseObserver) {
        System.out.println("getNotes()");
        NoteResponse response = NoteResponse.newBuilder().addAllNote(idToNote.values()).build();
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
        Note note = Note.newBuilder().setId(id).setIndex(id).setText(request.getText()).build(); //TODO: is this necessary or can 'request' just be used?
        idToNote.put(note.getId(), note);
        mongoManager.create(note);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateNote(Note request, StreamObserver<Empty> responseObserver) {
        System.out.println("updateNote()");
        idToNote.put(request.getId(), request);
        mongoManager.update(request);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteNote(NoteId request, StreamObserver<Note> responseObserver) {
        System.out.println("deleteNote()");
        Note note = idToNote.remove(request.getId());
        mongoManager.delete(note);
        responseObserver.onNext(note);
        responseObserver.onCompleted();
    }

}
