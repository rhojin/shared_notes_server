package de.rhojin.config;

import lombok.ToString;

@ToString
public class MongoDb {

    public String ip;
    public String port;
    public String databaseName;
    public String collectionName;
}
