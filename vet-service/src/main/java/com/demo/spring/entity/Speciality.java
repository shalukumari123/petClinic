package com.demo.spring.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SPECIALITIES")
public class Speciality {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int specialityId;
	@Column(name = "specialityName")
	private String specialityName;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "VETS_SPECIALITIES", joinColumns = { @JoinColumn(name = "SPECIALITYID") }, 
	inverseJoinColumns = { @JoinColumn(name = "VETID") })
	private Set<Vet> vets = new HashSet<>();

	
	public Set<Vet> getVets() {
		return vets;
	}

	public void setVets(Set<Vet> vets) {
		this.vets = vets;
	}

	public Speciality() {

	}

	public int getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Speciality(int specialityId, String specialityName) {
		this.specialityId = specialityId;
		this.specialityName = specialityName;
	}

	
}
