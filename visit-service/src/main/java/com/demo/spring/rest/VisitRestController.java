package com.demo.spring.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.spring.VisitRepository;
import com.demo.spring.entity.Visit;
import com.demo.spring.entity.VisitDto;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/visits")
@Timed("petclinic.visits")
public class VisitRestController {

	@Autowired
	RestTemplate rt;
	
	@Autowired
	VisitRepository repo;
	
	static Logger logger = LoggerFactory.getLogger(VisitRestController.class);
	
	//not completed
	@GetMapping(path = "/{visitid}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity findVisitById(@PathVariable("visitid") int id) {
		Optional<Visit> visitOp = repo.findById(id);
		if (visitOp.isPresent()) {
			logger.info("{Visit Details retrived}");
			return ResponseEntity.ok(visitOp.get());
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Visit with id not found\"}");
		}
	}
	
	
	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addVisit(@RequestBody VisitDto v) {
		if (repo.existsById(v.getVisitId())) {
			return ResponseEntity.ok("{\"status\":\"Visit already exists\"}");
		} else {
			Visit visit = new Visit();
			visit.setVisitId(v.getVisitId());
			visit.setDescription(v.getDescription());
			visit.setVisitDate(v.getVisitDate());
			visit.setPetId(v.getPetId());
			visit.setSpecId(v.getSpecId());
			repo.save(visit);
			logger.info("{Visit Details Saved}");
			return ResponseEntity.ok("{\"status\":\"Visit saved\"}");
		}
	}
	
	@PostMapping(path = "/save/{petid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addVisit(@PathVariable("petid") int id,@RequestBody VisitDto v) {
		if (repo.existsById(v.getVisitId())) {
			return ResponseEntity.ok("{\"status\":\"Visit exists\"}");
		} else {
			Visit visit = new Visit();
			visit.setVisitId(v.getVisitId());
			visit.setDescription(v.getDescription());
			visit.setVisitDate(v.getVisitDate());
			visit.setSpecId(v.getSpecId());
			visit.setPetId(id);
			repo.save(visit);
			logger.info("{Visit Details Saved}");
			return ResponseEntity.ok("{\"status\":\"Visit saved\"}");
		}
	}
	
	//list all visits
	@GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Visit>> getList() {
		return ResponseEntity.ok(repo.findAll());
	}
	
	//find pets by multiple ids
	@GetMapping(path = "/get/pets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Visit>> getPetsByIds(@RequestParam("ids") String[] ids) {
		List<Integer> idList = Arrays.asList(ids).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
		return ResponseEntity.ok(repo.findAllByPetId(idList));
	}
	
	//list visit pet by id
	@GetMapping(path = "/get/{petId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Visit>> getVisitByPetId(@PathVariable("petId") int petId) {
		return ResponseEntity.ok(repo.findByPetId(petId));
	}
	
	@DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteVisit(@PathVariable("id") int id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
			logger.info("{Visit Deleted}");
			return ResponseEntity.ok("{\"status\":\"Visit deleted\"}");
		} else {
			return ResponseEntity.status(404).body("{\"status\":\"Visit with id not found\"}");
		}
	}
}
