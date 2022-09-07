package com.demo.spring.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PETS")
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int petId;
	@Column(name = "PETNAME")
	private String petName;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "LIFESPAN")
	private int lifeSpan;
	
	
	@ManyToOne
	@JoinColumn(name = "ownerId")
	@JsonIgnore
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

	public Pet(int petId, String petName, String type, int lifeSpan) {
		this.petId = petId;
		this.petName = petName;
		this.type = type;
		this.lifeSpan = lifeSpan;
	}

	public Pet() {

	}

	public Pet(int petId, String petName, String type, int lifeSpan, Owner owners) {
		this.petId = petId;
		this.petName = petName;
		this.type = type;
		this.lifeSpan = lifeSpan;
		this.owners = owners;
	}

	@Override
	public int hashCode() {
		return Objects.hash(petId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pet other = (Pet) obj;
		return petId == other.petId;
	}

	
	

	
}
