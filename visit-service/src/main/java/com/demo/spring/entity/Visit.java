package com.demo.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VISITS")
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int visitId;
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "VISITDATE")
	private String visitDate;

	@Column(name = "PETID")
	private int petId;
	
	@Column(name = "SPECIALITYID")
	private int specId;
	

	public int getVisitId() {
		return visitId;
	}

	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public Visit() {

	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public Visit(int visitId, String description, String visitDate, int specId) {
		this.visitId = visitId;
		this.description = description;
		this.visitDate = visitDate;
		this.specId = specId;
	}

	public Visit(int visitId, String description, String visitDate, int petId, int specId) {
		this.visitId = visitId;
		this.description = description;
		this.visitDate = visitDate;
		this.petId = petId;
		this.specId = specId;
	}

	
	
	

}
