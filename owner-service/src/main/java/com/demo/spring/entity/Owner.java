package com.demo.spring.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "OWNERS")
public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ownerId;
	@Column(name = "OWNERNAME")
	private String ownerName;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "PHONE")
	private long telephone;

	
	@OneToMany(mappedBy = "owners",cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
	private Set<Pet> pets = new HashSet<>();
	
	public void addPet(Pet pet) {
        pets.add(pet);
        pet.setOwners(this);
    }
	
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

	public Owner(int ownerId,  String ownerName, String address,  long telephone) {
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.address = address;
		this.telephone = telephone;
	}

	public Owner() {

	}
}
