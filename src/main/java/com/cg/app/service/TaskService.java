package com.cg.app.service;

import com.cg.app.entity.Entry;
import com.cg.app.entity.Person;
import com.cg.app.entity.Task;
import com.cg.app.repository.EntryRepository;
import com.cg.app.repository.TaskRepository;
import com.cg.myflow.consumer.RouteId;
import com.cg.myflow.consumer.To;
import static com.cg.myflow.core.Components.direct;
import static com.cg.myflow.core.Components.log;
import com.cg.myflow.core.Exchange;
import java.util.List;
import java.util.function.Function;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.cg.myflow.core.Expression.*;
import com.cg.myflow.core.MyFlowUtil;

@Service
@Transactional
public class TaskService {
    
    @Autowired
    TaskRepository repository;
    @Autowired
    EntryRepository entryRepository;
    
    public TaskService() {
        new RouteId("task/info")
                .to(direct("required_auth"))
                .to(setTaskInfo(map(header("task"), "id")))
                .when(new Object[]{header("show_log"), new To(direct("entry/byTask"))});

        new RouteId("task/list")
                .to(direct("required_auth"))
                .to((exchange) -> {
                    List<Task> findAll = repository.findAll();
                    exchange.getModel().put("tasks", findAll);
                    return exchange;
                });
    }
    
    private Function<Exchange, Exchange> setTaskInfo(Object taskNameExpression) {
        return (exchange) -> {
            List<Task> tasks = repository.findById(MyFlowUtil.tryParseInt(evaluate(taskNameExpression, exchange), -1));
            if (!tasks.isEmpty()) {
                exchange.setHeader("task", tasks.get(0));
                exchange.setRawData(Task.class, tasks.get(0));
            }
            return exchange;
        };
    }

    public List<Task> findByTaskName(String taskName) {
        return repository.findByTaskName(taskName);
    }
    
    public void updateTotalProgress(Task task) {
        Integer sum = entryRepository.getSum(task);
        task.setTotalProgress(sum);
    }
    
    public List<Task> findById(Integer taskId) {
        return repository.findById(taskId);
    }
}
