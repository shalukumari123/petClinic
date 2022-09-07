package com.demo.spring.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.OwnerRepository;
import com.demo.spring.PetDto;
import com.demo.spring.PetRepository;
import com.demo.spring.entity.Owner;
import com.demo.spring.entity.Pet;

import io.micrometer.core.annotation.Timed;

@RestController
@Timed("petclinic.pets")
public class PetRestController {

	@Autowired
	PetRepository petRepo;

	@Autowired
	OwnerRepository ownerRepo;

	static Logger logger = LoggerFactory.getLogger(PetRestController.class);
	
	@GetMapping(path = "owners/pets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity findPetById(@PathVariable("id") int id) {
		Optional<Pet> petOp = petRepo.findById(id);
		if (petOp.isPresent()) {
			PetDto dto = new PetDto();
			dto.setPetId(petOp.get().getPetId());
			dto.setPetName(petOp.get().getPetName());
			dto.setType(petOp.get().getType());
			dto.setLifeSpan(petOp.get().getLifeSpan());
			dto.setOwners(petOp.get().getOwners());
			logger.info("{Pet Details retrived}");
			return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Pet not found\"}");
		}
	}

	// Remove the pet from the owner
	@DeleteMapping(value = "owners/pets/delete/{ownerid}/{petid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deletePet(@PathVariable("ownerid") int id1,@PathVariable("petid") int id2) {
		Optional<Owner> ownerOp = ownerRepo.findById(id1);
		if (ownerOp.isPresent()) {
			Set<Pet> pets = ownerOp.get().getPets();
			for (Pet p : pets) {
				if(p.getPetId() == id2) {
					petRepo.deleteById(p.getPetId());
					ownerOp.get().getPets().remove(p);
					break;
				}
				else {
					logger.info("{pet not found}");
				}
			}
			ownerRepo.save(ownerOp.get());
			return ResponseEntity.ok("{\"status\":\"Pet Deleted\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"owner with id not found\"}");
		}
	}

	//list of all pets
	@GetMapping(path = "owners/pets/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Pet>> getList() {
		return ResponseEntity.ok(petRepo.findAll());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> getException(Exception ex) {
		return ResponseEntity.ok(ex.getMessage());
	}

}
