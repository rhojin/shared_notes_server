package de.rhojin.server;

import de.rhojin.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class NoteService extends NoteServiceGrpc.NoteServiceImplBase {

    private final Map<Integer, Note> idToNote = new HashMap<>();

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
        idToNote.put(request.getId(), request);
        responseObserver.onCompleted();
    }

    @Override
    public void updateNote(Note request, StreamObserver<Empty> responseObserver) {
        System.out.println("updateNote()");
        idToNote.put(request.getId(), request);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteNote(NoteId request, StreamObserver<Note> responseObserver) {
        System.out.println("deleteNote()");
        Note removed = idToNote.remove(request.getId());
        responseObserver.onNext(removed);
        responseObserver.onCompleted();
    }
}
