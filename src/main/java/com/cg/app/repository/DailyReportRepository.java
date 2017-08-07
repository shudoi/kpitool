package com.cg.app.repository;

import com.cg.app.entity.DailyReport;
import com.cg.app.entity.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Integer> {
    
    @Query("SELECT dr FROM DailyReport dr where dr.person = :person and dr.reportDate = :reportDate")
    List<DailyReport> findByPersonAndReportDate(@Param("person") Person person,@Param("reportDate") String reportDate);
}
