package com.cg.app.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.Document;

public class MongoUtil {

    static final String DATABASE_NAME = "heroku_l2hknpxf";

    public static MongoCollection getCollection(MongoClient client, String collectionName) {
        return client.getDatabase(DATABASE_NAME).getCollection(collectionName);
    }

    public static int calcTotalProgress(MongoClient client, String taskName) {
        return StreamSupport.stream(client.getDatabase(DATABASE_NAME).getCollection("report")
                .find(new Document("data",
                        new Document("$elemMatch",
                                new Document("name", taskName))))
                .spliterator(), false)
                .flatMap((Document document) -> {
                    return ((List<Document>) (document.get("data", List.class))).stream();
                })
                .filter((Document document) -> {
                    return document.get("name", String.class).equals(taskName);
                })
                .mapToInt((Document document) -> {
                    return Integer.parseInt(document.get("today_progress", String.class));
                })
                .sum();
    }

    public static boolean setTotalProgress(MongoClient client, String taskName, int total) {
        MongoUtil.getCollection(client, "tasks")
                .updateOne(Filters.eq("name", taskName), new Document("$set", new Document("total_progress", total)));
        return true;
    }

    public static List<Document> getTaskHistory(MongoClient client, String taskName) {
        return StreamSupport.stream(client.getDatabase(DATABASE_NAME).getCollection("report")
                .find(new Document("data",
                        new Document("$elemMatch",
                                new Document("name", taskName))))
                .spliterator(), false)
                .flatMap((Document document) -> {
                    List<Document> data = (List<Document>) (document.get("data", List.class));
                    data.forEach((Document doc) -> {
                        doc.append("username", document.get("username")).append("date", document.get("date"));
                    });
                    return data.stream();
                })
                .filter((Document document) -> {
                    return document.get("name", String.class).equals(taskName);
                })
                .collect(Collectors.toList());
    }
}
