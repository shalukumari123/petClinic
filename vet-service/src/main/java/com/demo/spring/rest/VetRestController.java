package com.demo.spring.rest;

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

import com.demo.spring.VetRepository;
import com.demo.spring.entity.Vet;
import com.demo.spring.entity.VetDto;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/vets")
@Timed("petclinic.vets")
public class VetRestController {

	@Autowired
	VetRepository vetRepo;
	
	static Logger logger = LoggerFactory.getLogger(VetRestController.class);

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity findVetById(@PathVariable("id") int id) {
		Optional<Vet> vetOp = vetRepo.findById(id);
		if (vetOp.isPresent()) {
			logger.info("{Vet Details retrived}");
			return ResponseEntity.ok(vetOp.get());
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Vet does not exists\"}");
		}
	}

	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addVet(@RequestBody VetDto v) {
		if (vetRepo.existsById(v.getVetId())) {
			return ResponseEntity.ok("{\"status\":\"Vet already exists\"}");
		} else {
			Vet vet = new Vet();
			vet.setVetId(v.getVetId());
			vet.setVetFirstName(v.getVetFirstName());
			vet.setVetLastName(v.getVetLastName());
			vetRepo.save(vet);
			logger.info("{Vet saved}");
			return ResponseEntity.ok("{\"status\":\"Vet saved\"}");
		}
	}

	// list all the vets
	@GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Vet>> getList() {
		return ResponseEntity.ok(vetRepo.findAll());
	}

	@PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateVet(@RequestBody VetDto v, @PathVariable("id") int id) {
		Optional<Vet> vetOp = vetRepo.findById(id);
		if (vetRepo.existsById(id)) {
			Vet vet = new Vet();
			vet.setVetId(id);
			vet.setVetFirstName(v.getVetFirstName());
			vet.setVetLastName(v.getVetLastName());
			vet.setSpecialities(vetOp.get().getSpecialities());
			vetRepo.save(vet);
			return ResponseEntity.ok("{\"status\":\"Vet updated\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Vet with id not found\"}");
		}
	}

	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteVet(@PathVariable("id") int id) {
		if (vetRepo.existsById(id)) {
			vetRepo.deleteById(id);
			logger.info("{Vet deleted}");
			return ResponseEntity.ok("{\"status\":\"Vet deleted\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Vet not found\"}");
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> getException(Exception ex) {
		return ResponseEntity.ok(ex.getMessage());
	}
}
