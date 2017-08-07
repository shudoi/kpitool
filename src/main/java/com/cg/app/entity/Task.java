package com.cg.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "task")
public class Task implements Serializable {

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
