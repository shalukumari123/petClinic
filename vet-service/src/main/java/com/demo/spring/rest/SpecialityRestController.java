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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.SpecialityRepository;
import com.demo.spring.VetRepository;
import com.demo.spring.entity.Speciality;
import com.demo.spring.entity.SpecialityDto;
import com.demo.spring.entity.Vet;
import com.demo.spring.entity.VetDto;

import io.micrometer.core.annotation.Timed;

@RestController
@Timed("petclinic.specialities")
public class SpecialityRestController {

	@Autowired
	SpecialityRepository repo;

	@Autowired
	VetRepository vetRepo;

	static Logger logger = LoggerFactory.getLogger(SpecialityRestController.class);

	@GetMapping(path = "/vets/speciality/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity findSpecialityById(@PathVariable("id") int id) {
		Optional<Speciality> specOp = repo.findById(id);
		if (specOp.isPresent()) {
			logger.info("{Speciality Details retrived}");
			return ResponseEntity.ok(specOp.get());
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"speciality does not exists\"}");
		}
	}

	@PostMapping(path = "/vets/speciality/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addVet(@RequestBody SpecialityDto s) {
		if (repo.existsById(s.getSpecialityId())) {
			return ResponseEntity.ok("{\"status\":\"speciality already exists\"}");
		} else {
			Speciality spec = new Speciality();
			spec.setSpecialityId(s.getSpecialityId());
			spec.setSpecialityName(s.getSpecialityName());
			repo.save(spec);
			logger.info("{Speciality Saved}");
			return ResponseEntity.ok("{\"status\":\"speciality saved\"}");
		}
	}

	@DeleteMapping(value = "/vets/speciality/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteSpeciality(@PathVariable("id") int id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
			logger.info("{Speciality Deleted}");
			return ResponseEntity.ok("{\"status\":\"Speciality deleted\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Speciality with id not found\"}");
		}
	}

	@GetMapping(path = "vets/speciality/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Speciality>> getList() {
		return ResponseEntity.ok(repo.findAll());
	}

	// Search a vet based on speciality
	@GetMapping(path = "vets/speciality/get/{specid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Vet>> listSpecialityOfVet(@PathVariable("specid") int id) {
		Optional<Speciality> specOp = repo.findById(id);
		List<Vet> vetList = new ArrayList<>(specOp.get().getVets());
		return ResponseEntity.ok(vetList);
	}

	// add a vet to the speciality
	@PostMapping(value = "vets/speciality/{specid}/vet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addSpeciality(@PathVariable("specid") int id, @RequestBody VetDto v) {
		Optional<Speciality> specOp = repo.findById(id);
		if (specOp.isPresent()) {
			Vet vet = new Vet();
			vet.setVetId(v.getVetId());
			vet.setVetFirstName(v.getVetFirstName());
			vet.setVetLastName(v.getVetLastName());
			Speciality spec = specOp.get();
			spec.getVets().add(vet);
			repo.save(spec);
			logger.info("{Vet Details Saved}");
			return ResponseEntity.ok("{\"status\":\"Vet saved\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Speciality not found\"}");
		}
	}

}
