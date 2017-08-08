package com.cg.app.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cg.myflow.core.MyFlowUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "entry")
public class Entry implements Serializable {

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public DailyReport getDailyReport() {
		return dailyReport;
	}

	public void setDailyReport(DailyReport dailyReport) {
		this.dailyReport = dailyReport;
	}

	public Integer getTodayProgress() {
		return todayProgress;
	}

	public void setTodayProgress(Integer todayProgress) {
		this.todayProgress = todayProgress;
	}

	public Integer getActualTime() {
		return actualTime;
	}

	public void setActualTime(Integer actualTime) {
		this.actualTime = actualTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Person person;
    @ManyToOne
    private Task task;
    @JsonIgnore
    @ManyToOne
    private DailyReport dailyReport;
    @JsonProperty("today_progress")
    private Integer todayProgress;
    @JsonProperty("actual_time")
    private Integer actualTime;
    @JsonProperty("comment")
    private String comment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("report_date")
    @Transient
    private String reportDate;

    public boolean updateFromMap(Map map) {
        Integer oldTodayProgress = this.todayProgress;
        Integer newTodayProgress = Integer.parseInt(map.get("today_progress").toString());
        this.todayProgress = newTodayProgress;
        this.setActualTime(Integer.parseInt(map.get("actual_time").toString()));
        this.setComment((String) map.get("comment"));
        return !Objects.equals(oldTodayProgress, newTodayProgress);
    }

    public boolean isEqualTo(Map entryMap) { //TBD
        if (MyFlowUtil.mapValueAsInt(entryMap, "task.id", -1).equals(this.task.getId())) {
            return true;
        } else {
            return false;
        }
    }

    public Entry() {
    }

    public Entry(Person person, Task task, DailyReport report, Integer todayProgress, Integer actualTime, String comment) {
        this.person = person;
        this.task = task;
        this.dailyReport = report;
        this.todayProgress = todayProgress;
        this.actualTime = actualTime;
        this.comment = comment;
    }
}
