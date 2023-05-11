package de.rhojin.server;

import de.rhojin.grpc.HelloGrpc;
import de.rhojin.grpc.HelloRequest;
import de.rhojin.grpc.HelloResponse;
import io.grpc.stub.StreamObserver;

public class HelloService extends HelloGrpc.HelloImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("sayHello()");

        HelloResponse response = HelloResponse.newBuilder()
                .setMessage("Hello " + request.getName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
