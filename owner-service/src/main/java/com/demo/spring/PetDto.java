package com.demo.spring;

import com.demo.spring.entity.Owner;

public class PetDto {

	private int petId;

	private String petName;

	private String type;

	private int lifeSpan;
	private Owner owners;

	public Owner getOwners() {
		return owners;
	}

	public void setOwners(Owner owners) {
		this.owners = owners;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void setLifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	@Override
	public String toString() {
		return "PetDto [petId=" + petId + ", petName=" + petName + ", type=" + type + ", lifeSpan=" + lifeSpan
				+ ", owners=" + owners.getOwnerName() ;
	}

	
}
