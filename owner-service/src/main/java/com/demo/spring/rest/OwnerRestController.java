package com.demo.spring.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.OwnerDto;
import com.demo.spring.OwnerRepository;
import com.demo.spring.PetDto;
import com.demo.spring.PetRepository;
import com.demo.spring.entity.Owner;
import com.demo.spring.entity.Pet;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/owners")
@Timed("petclinic.owners")
public class OwnerRestController {

	@Autowired
	OwnerRepository repo;

	@Autowired
	PetRepository petRepo;
	
	static Logger logger = LoggerFactory.getLogger(OwnerRestController.class);

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity findOwnerById(@PathVariable("id") int id) {
		Optional<Owner> ownerOp = repo.findById(id);
		if (ownerOp.isPresent()) {
			logger.info("{Owner Details retrived}");
			return ResponseEntity.ok(ownerOp.get());
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Owner not found\"}");
		}
	}

	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addOwner(@RequestBody OwnerDto o) {
		if (repo.existsById(o.getOwnerId())) {
			return ResponseEntity.ok("{\"status\":\"Owner already exists\"}");
		} else {
			Owner owner = new Owner();
			owner.setOwnerId(o.getOwnerId());
			owner.setOwnerName(o.getOwnerName());
			owner.setAddress(o.getAddress());
			owner.setTelephone(o.getTelephone());
			repo.save(owner);
			logger.info("{Owner Details saved}");
			return ResponseEntity.ok("{\"status\":\"Owner saved\"}");
		}
	}

	@PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateOwner(@RequestBody OwnerDto o, @PathVariable("id") int id) {
		Optional<Owner> ownerOp = repo.findById(id);
		if (repo.existsById(id)) {
			Owner owner = new Owner();
			owner.setOwnerId(id);
			owner.setOwnerName(o.getOwnerName());
			owner.setAddress(o.getAddress());
			owner.setTelephone(o.getTelephone());
			owner.setPets(ownerOp.get().getPets());
			repo.save(owner);
			return ResponseEntity.ok("{\"status\":\"Owner updated\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Owner with id not found\"}");
		}
	}

	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteOwner(@PathVariable("id") int id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
			logger.info("{Owner Details Deleted}");
			return ResponseEntity.ok("{\"status\":\"Owner deleted\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Owner does not exists\"}");
		}
	}

	// Add the pet to the owner
	@PostMapping(value = "/{ownerid}/pet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> setPetToOwner(@PathVariable("ownerid") int id, @RequestBody PetDto p) {
		Optional<Owner> ownerOp = repo.findById(id);
		if (ownerOp.isPresent()) {
			Pet pet = new Pet();
			pet.setPetId(p.getPetId());
			pet.setPetName(p.getPetName());
			pet.setType(p.getType());
			pet.setLifeSpan(p.getLifeSpan());
			Owner owner = ownerOp.get();
			owner.addPet(pet);
			repo.save(owner);
			logger.info("{Pet is saved to the owner}");
			return ResponseEntity.ok("{\"status\":\"Pet saved\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Owner is not found\"}");
		}
	}

	// get list of all the owners
	@GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Owner>> getList() {
		return ResponseEntity.ok(repo.findAll());
	}

	// list pets of owner
	@GetMapping(path = "/get/{ownerid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Pet>> listPetsOfOwner(@PathVariable("ownerid") int id) {
		Optional<Owner> ownerOp = repo.findById(id);
		List<Pet> petList = new ArrayList<>(ownerOp.get().getPets());
		return ResponseEntity.ok(petList);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> getException(Exception ex) {
		return ResponseEntity.ok(ex.getMessage());
	}
}
