/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cg.app.repository;

import com.cg.app.entity.Entry;
import com.cg.app.entity.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

    @Query("SELECT sum(e.todayProgress) FROM Entry e where e.task = :task group by e.task.id")
    Integer getSum(@Param("task") Task task);

    @Query("SELECT dr.reportDate, e FROM DailyReport dr, Entry e where e.task = :task and e.dailyReport = dr order by dr.reportDate desc")
    List<Object[]> findAllByTaskWithReportDate(@Param("task") Task task);
}
