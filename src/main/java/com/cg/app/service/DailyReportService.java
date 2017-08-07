package com.cg.app.service;

import com.cg.app.entity.DailyReport;
import com.cg.app.entity.Entry;
import com.cg.app.entity.Person;
import com.cg.app.entity.Task;
import com.cg.app.repository.DailyReportRepository;
import com.cg.app.repository.EntryRepository;
import com.cg.myflow.consumer.RouteId;
import static com.cg.myflow.core.Components.direct;
import com.cg.myflow.core.Exchange;
import static com.cg.myflow.core.Expression.*;
import com.cg.myflow.core.MyFlowUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DailyReportService {

    @Autowired
    DailyReportRepository repository;
    @Autowired
    TaskService taskService;
    @Autowired
    EntryRepository entryRepository;

    public DailyReportService() {
        new RouteId("dailyReport/get")
                .to(direct("required_auth"))
                .to(direct("person/get"))
                .to(getOrCreateDailyReport(rawData(Person.class), orElse(header("reportDate"), today())));

        new RouteId("dailyReport/update")
                .to(direct("dailyReport/get"))
                .to(updateEntryFromMapList(header("data")));

    }

    private Function<Exchange, Exchange> getOrCreateDailyReport(Object personExpression, Object reportDateExpression) {
        return (exchange) -> {
            Person person = evaluate(personExpression, exchange, Person.class);
            String reportDate = evaluate(reportDateExpression, exchange, String.class);
            List<DailyReport> reports = repository.findByPersonAndReportDate(person, reportDate);
            if (reports.isEmpty()) {
                DailyReport dailyReport = new DailyReport(person, reportDate);
                repository.save(dailyReport);
                System.out.println("create report");
                exchange.setRawData(DailyReport.class, dailyReport);
            } else {
                System.out.println("load report");
                exchange.setRawData(DailyReport.class, reports.get(0));
            }
            return exchange;
        };
    }

    private Function<Exchange, Exchange> updateEntryFromMapList(Object mapListExpression) {
        return (exchange) -> {
            List<Map> mapList = evaluate(mapListExpression, exchange, List.class);
            DailyReport report = exchange.getRawData(DailyReport.class);
            getDeletedEntries(report, mapList).forEach((entry) -> {
                entryRepository.delete(entry);
                taskService.updateTotalProgress(entry.getTask());
            });
            Person person = exchange.getRawData(Person.class);
            updateEntriesAndFilterNewEntryMapList(report, mapList)
                    .forEach((entryMap) -> {
                        List<Task> tasks = taskService.findById(MyFlowUtil.mapValueAsInt(entryMap, "task.id", -1));
                        if (!tasks.isEmpty()) {
                            Entry entry = new Entry(person, tasks.get(0), report, Integer.parseInt(entryMap.get("today_progress").toString()), Integer.parseInt(entryMap.get("actual_time").toString()), entryMap.get("comment").toString());
                            report.getEntries().add(entry);
                            entryRepository.save(entry);
                            taskService.updateTotalProgress(entry.getTask());
                        }
                    });
            exchange.setHeader("data", report);
            return exchange;
        };
    }

    private List<Entry> getDeletedEntries(DailyReport report, List<Map> mapList) {
        List<Entry> result = new ArrayList<>();
        for (Entry entry : report.getEntries()) {
            if (mapList.stream().noneMatch((map) -> {
                return entry.isEqualTo(map);
            })) {
                result.add(entry);
            }
        }
        return result;
    }

    private List<Map> updateEntriesAndFilterNewEntryMapList(DailyReport report, List<Map> mapList) {
        List<Map> newEntryMapList = new ArrayList<>();
        for (Map entryMap : mapList) {
            if (report.getEntries().stream().noneMatch((entry) -> {
                if (entry.isEqualTo(entryMap)) {
                    boolean updateTodayProgress = entry.updateFromMap(entryMap);
                    if (updateTodayProgress) {
                        taskService.updateTotalProgress(entry.getTask());
                    }
                    return true;
                } else {
                    return false;
                }
            })) {
                newEntryMapList.add(entryMap);
            }
        }
        return newEntryMapList;
    }

}
