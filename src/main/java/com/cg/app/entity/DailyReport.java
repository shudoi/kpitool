package com.cg.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "daily_report", indexes = {
    @Index(columnList = "person_id")})
public class DailyReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public DailyReport() {
    }

    public DailyReport(Person person, String reportDate) {
        this.person = person;
        this.reportDate = reportDate;
        this.entries = new ArrayList<>();
    }
    @OneToMany(mappedBy = "dailyReport")
    @OrderBy("id ASC")
    private List<Entry> entries;
    @ManyToOne
    private Person person;
    String reportDate;
}
