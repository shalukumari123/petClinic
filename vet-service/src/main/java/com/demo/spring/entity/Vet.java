package com.demo.spring.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "VETS")
public class Vet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vetId;
	@Column(name = "FIRSTNAME")
	private String vetFirstName;
	@Column(name = "LASTNAME")
	private String vetLastName;

	@ManyToMany
	@JoinTable(name = "VETS_SPECIALITIES", joinColumns = { @JoinColumn(name = "VETID") }, inverseJoinColumns = {
			@JoinColumn(name = "SPECIALITYID") })
	@JsonIgnore
	private Set<Speciality> specialities = new HashSet<>();

	public Set<Speciality> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(Set<Speciality> specialities) {
		this.specialities = specialities;
	}

	public Vet() {

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

	public Vet(int vetId, String vetFirstName, String vetLastName) {
		this.vetId = vetId;
		this.vetFirstName = vetFirstName;
		this.vetLastName = vetLastName;
	}

	
}
