package com.demo.spring.entity;

import java.util.HashSet;
import java.util.Set;


public class SpecialityDto {

	private int specialityId;
	
	private String specialityName;


	private Set<Vet> vets = new HashSet<>();

	
	public Set<Vet> getVets() {
		return vets;
	}

	public void setVets(Set<Vet> vets) {
		this.vets = vets;
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

	public SpecialityDto(int specialityId, String specialityName) {
		this.specialityId = specialityId;
		this.specialityName = specialityName;
	}

	
}
