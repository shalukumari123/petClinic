package com.demo.spring;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.spring.entity.Visit;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class VisitControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	VisitRepository repo;

	@InjectMocks
	VisitControllerTest vc;
	

	@Test
	 void testFindVisitById() throws Exception {

		Mockito.when(repo.findById(1)).thenReturn(Optional.of(new Visit(1, "rabies shot", "2022-02-22",1,5)));

		mvc.perform(get("/visits/1").accept(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description").value("rabies shot"));
	}
	
	@Test
	 void testFindVisitByIdNegative() throws Exception {

		Mockito.when(repo.findById(1)).thenReturn(Optional.of(new Visit(1, "rabies shot", "2022-02-22",1,5)));

		mvc.perform(get("/visits/2").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Visit with id not found"));
	}
	
	@Test
	 void testFindAllVisit() throws Exception{
		List<Visit> vetList = new ArrayList<>();
		vetList.add(new Visit(1, "rabies shot", "2022-02-22",1,5));
		Mockito.when(repo.findAll()).thenReturn(vetList);
		
		mvc.perform(get("/visits/get").accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].description").value("rabies shot"));
	}

	@Test
	 void shouldSaveVisit() throws Exception {
		Visit visit = new Visit(1, "rabies shot", "2022-02-22",1,5);
		Mockito.when(repo.save(visit)).thenReturn(visit);
		
		ObjectMapper om = new ObjectMapper();
		String visitJson = om.writeValueAsString(visit);
		
		mvc.perform(post("/visits/save").contentType(MediaType.APPLICATION_JSON)
				.content(visitJson))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	 void shouldSaveVisitNegative() throws Exception {
		Visit visit = new Visit(1, "rabies shot", "2022-02-22",1,5);
		Mockito.when(repo.existsById(1)).thenReturn(true);
		
		ObjectMapper om = new ObjectMapper();
		String visitJson = om.writeValueAsString(visit);
		
		mvc.perform(post("/visits/save").contentType(MediaType.APPLICATION_JSON)
				.content(visitJson))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Visit already exists"));
	}
	
	@Test
	 void shouldDeleteVisitById() throws Exception {
		int id = 1;
		when(repo.existsById(id)).thenReturn(true);
		
		mvc.perform(delete("/visits/delete/1"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value("Visit deleted"));
	}
	
	@Test
	 void shouldDeleteVisitByIdNegative() throws Exception {
		int id = 1;
		when(repo.existsById(id)).thenReturn(false);
		
		mvc.perform(delete("/visits/delete/2"))
		.andDo(print())
		.andExpect(jsonPath("$.status").value("Visit with id not found"));
	}
	
	@Test
	 void testFindVisitByPet() throws Exception{
		
		List<Visit> visitList = new ArrayList<>();
		visitList.add(new Visit(1, "rabies shot", "2022-02-22",4,5));
		visitList.add(new Visit(2, "vaccine shot", "2022-02-22",4,5));
		Mockito.when(repo.findByPetId(4)).thenReturn(visitList);
		
		mvc.perform(get("/visits/get/4").accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[1].description").value("vaccine shot"));
	}
	
	@Test
	 void shouldSavePetToVisit() throws Exception{
		
		Visit visit = new Visit(1, "rabies shot", "2022-02-22",5);
		visit.setPetId(1);
		
		Mockito.when(repo.findById(4)).thenReturn(Optional.of(visit));
		Mockito.when(repo.save(visit)).thenReturn(visit);
		
		ObjectMapper om = new ObjectMapper();
		String visitJson = om.writeValueAsString(visit);
		
		mvc.perform(post("/visits/save/2").contentType(MediaType.APPLICATION_JSON)
				.content(visitJson))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	 void shouldSavePetToVisitNegative() throws Exception{
		
		Visit visit = new Visit(1, "rabies shot", "2022-02-22",5);
		visit.setPetId(1);
		
		Mockito.when(repo.existsById(1)).thenReturn(true);
		
		ObjectMapper om = new ObjectMapper();
		String visitJson = om.writeValueAsString(visit);
		
		mvc.perform(post("/visits/save/1").contentType(MediaType.APPLICATION_JSON)
				.content(visitJson))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Visit exists"));
	}
}
