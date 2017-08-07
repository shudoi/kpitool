package com.cg.app.service;

import com.cg.app.entity.Entry;
import com.cg.app.entity.Task;
import com.cg.app.repository.EntryRepository;
import com.cg.myflow.consumer.RouteId;
import com.cg.myflow.core.Exchange;
import static com.cg.myflow.core.Expression.evaluate;
import static com.cg.myflow.core.Expression.rawData;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EntryService {
    
    @Autowired
    EntryRepository repository;
    
    public EntryService() {
        new RouteId("entry/byTask")
                .to(getEntriesByTaskWithReportDate(rawData(Task.class)));
    }
    
    public final Function<Exchange, Exchange> getEntriesByTaskWithReportDate(Object taskExpression) {
        return (exchange) -> {
            Task task = evaluate(taskExpression, exchange, Task.class);
            exchange.setHeader("entries", repository.findAllByTaskWithReportDate(task));
            List<Entry> entries = repository.findAllByTaskWithReportDate(task).stream().map((row) -> {
                Entry entry = (Entry) row[1];
                entry.setReportDate(row[0].toString());
                return entry;
            }).collect(Collectors.toList());
            exchange.setHeader("entries", entries);
            return exchange;
        };
    }
}
