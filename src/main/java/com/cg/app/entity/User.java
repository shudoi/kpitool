package com.cg.app.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, columnDefinition = "int(10)")
	private Integer id;
	@Column(name = "USERID", nullable = false, columnDefinition = "varchar(10)")
	private String userid;
	@Column(name = "PASSWORD", nullable = false, columnDefinition = "varchar(50)")
	private String password;
	@Column(name = "ACTIVE_USER", nullable = false, columnDefinition = "char(1)")
	private String activeUser = "1";
	@Column(name = "KPI_ROLE", nullable = false, columnDefinition = "char(1)")
	private String kpiRole = "1";
	@Column(name = "NAME_EN", nullable = false, columnDefinition = "varchar(50)")
	private String nameEn;
	@Column(name = "NAME_JP", nullable = true, columnDefinition = "varchar(20)")
	private String nameJp = null;
	@Column(name = "NAME_KANA", nullable = false, columnDefinition = "varchar(100)")
	private String nameKana;
	@Column(name = "GENDER", nullable = false, columnDefinition = "char(1)")
	private String gender = "1";
	@Column(name = "EMPLOYEE_ID", nullable = false, columnDefinition = "varchar(50)")
	private String employeeId;
	@Column(name = "DOB", nullable = true, columnDefinition = "date")
	@Temporal(TemporalType.DATE)
	private Date dob = null;
	@Column(name = "BASE_LOCATION", nullable = true, columnDefinition = "varchar(255)")
	private String baseLocation = null;
	@Column(name = "ASSIGNED_DATE_TO_MLJ", nullable = true, columnDefinition = "date")
	@Temporal(TemporalType.DATE)
	private Date assignedDateToMlj = null;
	@Column(name = "ROLE", nullable = true, columnDefinition = "char(2)")
	private String role = null;
	@Column(name = "CG_GRADE", nullable = true, columnDefinition = "char(2)")
	private String cgGrade = null;
	@Column(name = "CG_DESIGNATION", nullable = true, columnDefinition = "char(2)")
	private String cgDesignation = null;
	@Column(name = "SKILL_SET", nullable = true, columnDefinition = "varchar(255)")
	private String skillSet = null;
	@Column(name = "YEARS_OF_EXPERIENCE", nullable = false, columnDefinition = "int(2)")
	private Integer yearsOfExperience = 0;
	@Column(name = "Z_ID", nullable = false, columnDefinition = "char(7)")
	private String zId = null;
	@Column(name = "VPN_ACCESS_RIGHT", nullable = false, columnDefinition = "char(1)")
	private String vpnAccessRight = "0";
	@Column(name = "DELETE_FLG", nullable = false, columnDefinition = "char(1)")
	private String deleteFlg = "0";
	@Column(name = "UPDATE_USER_ID", nullable = false, columnDefinition = "varchar(10)")
	private String updateUserId;
	@Column(name = "UPDATE_DATETIME", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updateDatetime;
	@Column(name = "CREATE_USER_ID", nullable = false, columnDefinition = "varchar(10)")
	private String createUserId;
	@Column(name = "CREATE_DATETIME", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createDatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
