package com.demo.spring;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.demo.spring.entity.Speciality;
import com.demo.spring.entity.Vet;
import com.demo.spring.rest.SpecialityRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class VetControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	VetRepository vetRepo;
	
	@MockBean
	SpecialityRepository specRepo;

	@InjectMocks
	VetControllerTest vc;
	
	@InjectMocks
	SpecialityRestController sc;

	@Test
	 void testFindVetById() throws Exception {

		Mockito.when(vetRepo.findById(1)).thenReturn(Optional.of(new Vet(1, "Ramya", "Varma")));

		mvc.perform(get("/vets/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.vetFirstName").value("Ramya"));
	}
	
	@Test
	 void testFindVetByIdNegative() throws Exception {

		Mockito.when(vetRepo.findById(1)).thenReturn(Optional.of(new Vet(1, "Ramya", "Varma")));

		mvc.perform(get("/vets/2").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Vet does not exists"));
	}
	
	
	@Test
	 void testFindAllVet() throws Exception{
		List<Vet> vetList = new ArrayList<>();
		vetList.add(new Vet(1, "Ramya", "Varma"));
		Mockito.when(vetRepo.findAll()).thenReturn(vetList);
		
		mvc.perform(get("/vets/get").accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].vetFirstName").value("Ramya"));
	}

	@Test
	 void shouldSaveVet() throws Exception {
		Vet vet = new Vet(1, "Ramya", "Varma");
		Mockito.when(vetRepo.save(vet)).thenReturn(vet);
		
		ObjectMapper om = new ObjectMapper();
		String vetJson = om.writeValueAsString(vet);
		
		mvc.perform(post("/vets/save").contentType(MediaType.APPLICATION_JSON)
				.content(vetJson))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	 void shouldUpdateVet() throws Exception {
		Vet vet = new Vet(1, "Ramya", "Varma");
		
		Mockito.when(vetRepo.findById(1)).thenReturn(Optional.of(vet));
		Mockito.when(vetRepo.save(vet)).thenReturn(vet);
		when(vetRepo.existsById(1)).thenReturn(true);
		
		ObjectMapper om = new ObjectMapper();
		String vetJson = om.writeValueAsString(vet);
		
		mvc.perform(put("/vets/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(vetJson))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Vet updated"));
	}
	
	@Test
	 void shouldUpdateVetNegative() throws Exception {
		Vet vet = new Vet(1, "Ramya", "Varma");
		Mockito.when(vetRepo.save(vet)).thenReturn(vet);
		when(vetRepo.existsById(1)).thenReturn(true);
		
		ObjectMapper om = new ObjectMapper();
		String vetJson = om.writeValueAsString(vet);
		
		mvc.perform(put("/vets/update/2").contentType(MediaType.APPLICATION_JSON)
				.content(vetJson))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	 void shouldDeleteVetById() throws Exception {
		int id = 1;
		when(vetRepo.existsById(id)).thenReturn(true);
		
		mvc.perform(delete("/vets/delete/1"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value("Vet deleted"));
	}
	
	@Test
	 void shouldDeleteVetByIdNegative() throws Exception {
		int id = 1;
		when(vetRepo.existsById(id)).thenReturn(true);
		
		mvc.perform(delete("/vets/delete/2"))
		.andDo(print())
		.andExpect(jsonPath("$.status").value("Vet not found"));
	}
	
	//speciality
	@Test
	 void testFindSpecById() throws Exception {

		Mockito.when(specRepo.findById(1)).thenReturn(Optional.of(new Speciality(1,"Radiology")));

		mvc.perform(get("/vets/speciality/1").accept(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialityName").value("Radiology"));
	}
	
	@Test
	 void testFindSpecByIdNegative() throws Exception {

		Mockito.when(specRepo.findById(1)).thenReturn(Optional.of(new Speciality(1,"Radiology")));

		mvc.perform(get("/vets/speciality/2").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("speciality does not exists"));
	}
	
	@Test
	 void testFindAllSpec() throws Exception{
		List<Speciality> specList = new ArrayList<>();
		specList.add(new Speciality(1,"Radiology"));
		Mockito.when(specRepo.findAll()).thenReturn(specList);
		
		mvc.perform(get("/vets/speciality/get").accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].specialityName").value("Radiology"));
	}

	@Test
	 void shouldSaveSpec() throws Exception {
		Speciality spec = new Speciality(1,"Radiology");
		Mockito.when(specRepo.save(spec)).thenReturn(spec);
		
		ObjectMapper om = new ObjectMapper();
		String specJson = om.writeValueAsString(spec);
		
		mvc.perform(post("/vets/speciality/save").contentType(MediaType.APPLICATION_JSON)
				.content(specJson))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	 void shouldDeleteSpecById() throws Exception {
		int id = 1;
		when(specRepo.existsById(id)).thenReturn(true);
		
		mvc.perform(delete("/vets/speciality/delete/1"))
		.andDo(print())
		.andExpect(jsonPath("$.status").value("Speciality deleted"));
	}
	
	@Test
	 void shouldDeleteSpecByIdNegative() throws Exception {
		int id = 1;
		when(specRepo.existsById(id)).thenReturn(true);
		
		mvc.perform(delete("/vets/speciality/delete/2"))
		.andDo(print())
		.andExpect(jsonPath("$.status").value("Speciality with id not found"));
	}
	
	@Test
	 void shouldSaveVetToSpeciality() throws Exception{
		
		Speciality spec = new Speciality(1,"Radiology");
		
		Vet vet = new Vet(1, "Ramya", "Varma");
		spec.getVets().add(vet);
		
		Mockito.when(specRepo.findById(1)).thenReturn(Optional.of(spec));
		Mockito.when(vetRepo.save(vet)).thenReturn(vet);
		
		ObjectMapper om = new ObjectMapper();
		String vetJson = om.writeValueAsString(vet);
		
		mvc.perform(post("/vets/speciality/1/vet").contentType(MediaType.APPLICATION_JSON)
				.content(vetJson))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	 void shouldSaveVetToSpecialityNegative() throws Exception{
		
		Speciality spec = new Speciality(1,"Radiology");
		
		Vet vet = new Vet(1, "Ramya", "Varma");
		spec.getVets().add(vet);
		
		Mockito.when(specRepo.findById(1)).thenReturn(Optional.of(spec));
		
		ObjectMapper om = new ObjectMapper();
		String vetJson = om.writeValueAsString(vet);
		
		mvc.perform(post("/vets/speciality/2/vet").contentType(MediaType.APPLICATION_JSON)
				.content(vetJson))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value("Speciality not found"));
	}
	
	@Test
	 void testFindVetBySpeciality() throws Exception{
		int id = 1;
		Speciality spec = new Speciality(1,"Radiology");
		spec.getVets().add(new Vet(1, "Ramya", "Varma"));
		spec.getVets().add( new Vet(2, "Ramya", "Varma"));
		Mockito.when(specRepo.findById(id)).thenReturn(Optional.of(spec));
		
		mvc.perform(get("/vets/speciality/get/1").accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].vetFirstName").value("Ramya"));
	}
	
}
