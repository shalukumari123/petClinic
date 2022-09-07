package com.demo.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.demo.spring.entity.Pet;
import com.demo.spring.rest.PetRestController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class PetRestControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	PetRepository repo;
	
	@MockBean 
	OwnerRepository orepo;

	@InjectMocks
	PetRestController rc;

	@Test
	 void testFindById() throws Exception {

		Mockito.when(repo.findById(1)).thenReturn(Optional.of(new Pet(1, "Coco", "Dog", 10)));

		mvc.perform(get("/owners/pets/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.petName").value("Coco"));
	}
	
	@Test
	 void testFindByIdNegative() throws Exception {

		Mockito.when(repo.findById(1)).thenReturn(Optional.of(new Pet(1, "Coco", "Dog", 10)));

		mvc.perform(get("/owners/pets/2").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Pet not found"));
	}

	@Test
	 void testFindAllPet() throws Exception {
		List<Pet> petList = new ArrayList<>();
		petList.add(new Pet(1, "Coco", "Dog", 10));
		Mockito.when(repo.findAll()).thenReturn(petList);

		mvc.perform(get("/owners/pets/get").accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.[0].petName").value("Coco"));
	}
}
