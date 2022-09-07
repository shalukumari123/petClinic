package com.demo.spring;

import java.util.HashSet;
import java.util.Set;

import com.demo.spring.entity.Pet;


public class OwnerDto {

	private int ownerId;
	
	private String ownerName;

	private String address;
	
	private long telephone;

	
	
	private Set<Pet> pets = new HashSet<>();
	
	public Set<Pet> getPets() {
		return pets;
	}

	public void setPets(Set<Pet> pets) {
		this.pets = pets;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getTelephone() {
		return telephone;
	}

	public void setTelephone(long telephone) {
		this.telephone = telephone;
	}
	
}
