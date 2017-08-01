package com.cg.app.util;

import java.util.List;
import org.bson.Document;

public class CollectionUtil {

    public static boolean mergeTasksTotalProgressToReport(List<Document> tableData, List<Document> tasks) {
        for (Document row : tableData) {
            for (Document task : tasks) {
                if (row.get("name", String.class).equals(task.get("name", String.class))) {
                    row.put("total_progress", task.getOrDefault("total_progress", "0"));
                }
            }
        }
        return true;
    }
}
