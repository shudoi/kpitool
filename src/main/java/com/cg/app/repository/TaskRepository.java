package com.cg.app.repository;

import com.cg.app.entity.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findById(Integer id);

    @Query("SELECT t FROM Task t WHERE t.name = :taskName")
    List<Task> findByTaskName(@Param("taskName") String taskName);

    @Query("SELECT t FROM Task t WHERE t.id = :id")
    List<Task> reget(@Param("id") Integer id);
}
