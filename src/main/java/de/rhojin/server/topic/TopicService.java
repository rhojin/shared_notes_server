package de.rhojin.server.topic;

import de.rhojin.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TopicService extends TopicServiceGrpc.TopicServiceImplBase {
    private final TopicManager topicManager;
    private final Map<Integer, Topic> idToTopic;

    public TopicService(TopicManager topicManager) {
        this.topicManager = topicManager;
        idToTopic = topicManager.load()
                .stream()
                .collect(Collectors.toMap(Topic::getId, Function.identity()));
    }

    @Override
    public void getTopics(Empty request, StreamObserver<Topics> responseObserver) {
        System.out.println("getTopics()");
        Topics response = Topics.newBuilder().addAllTopic(idToTopic.values()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createTopic(Topic request, StreamObserver<Empty> responseObserver) {
        System.out.println("createTopic()");
        int id = idToTopic.isEmpty() ? 0 : Collections.max(idToTopic.keySet()) + 1;
        Topic topic = Topic.newBuilder().setId(id).setText(request.getText()).build();
        idToTopic.put(topic.getId(), topic);
        topicManager.create(topic);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateTopic(Topic request, StreamObserver<Empty> responseObserver) {
        System.out.println("updateTopic()");
        idToTopic.put(request.getId(), request);
        topicManager.update(request);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTopic(TopicId request, StreamObserver<Topic> responseObserver) {
        System.out.println("deleteTopic()");
        Topic topic = idToTopic.remove(request.getId());
        topicManager.delete(topic);
        responseObserver.onNext(topic);
        responseObserver.onCompleted();
    }
}
