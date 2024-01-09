package de.rhojin.config;

import lombok.ToString;

@ToString
public class Config {
    public int grpcPort;
    public MongoDb mongoDb;
}
