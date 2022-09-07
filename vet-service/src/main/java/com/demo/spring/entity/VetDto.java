package com.demo.spring.entity;

import java.util.HashSet;
import java.util.Set;


public class VetDto {

	
	private int vetId;
	
	private String vetFirstName;
	
	private String vetLastName;

	
	private Set<Speciality> specialities = new HashSet<>();

	public Set<Speciality> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(Set<Speciality> specialities) {
		this.specialities = specialities;
	}

	public int getVetId() {
		return vetId;
	}

	public void setVetId(int vetId) {
		this.vetId = vetId;
	}

	public String getVetFirstName() {
		return vetFirstName;
	}

	public void setVetFirstName(String vetFirstName) {
		this.vetFirstName = vetFirstName;
	}

	public String getVetLastName() {
		return vetLastName;
	}

	public void setVetLastName(String vetLastName) {
		this.vetLastName = vetLastName;
	}

	public VetDto(int vetId, String vetFirstName, String vetLastName) {
		this.vetId = vetId;
		this.vetFirstName = vetFirstName;
		this.vetLastName = vetLastName;
	}

	
}
