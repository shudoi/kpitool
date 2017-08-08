package com.cg.app.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "task")
public class Task implements Serializable {

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScheduledStartDate() {
		return scheduledStartDate;
	}
	public void setScheduledStartDate(String scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}
	public String getScheduledEndDate() {
		return scheduledEndDate;
	}
	public void setScheduledEndDate(String scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}
	public String getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public String getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public Integer getTotalProgress() {
		return totalProgress;
	}
	public void setTotalProgress(Integer totalProgress) {
		this.totalProgress = totalProgress;
	}
	public Integer getBranch() {
		return branch;
	}
	public void setBranch(Integer branch) {
		this.branch = branch;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("scheduled_start_date")
    private String scheduledStartDate;
    @JsonProperty("scheduled_end_date")
    private String scheduledEndDate;
    @JsonProperty("actual_start_date")
    private String actualStartDate;
    @JsonProperty("actual_end_date")
    private String actualEndDate;
    @JsonProperty("total_progress")
    private Integer totalProgress;
    private Integer branch;
}
