package com.mvc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 光电院承担规划项目表
 * 
 * @author wangrui
 * @date 2016-11-15
 */
@Entity
@Table(name = "contract")
public class PlanProjectForm implements Serializable {

	private static final long serialVersionUID = 5700346765916208959L;

	private Integer plpr_id;
	private String cont_project;
	private String manager;
	
}
