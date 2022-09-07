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

import com.demo.spring.entity.Owner;
import com.demo.spring.entity.Pet;
import com.demo.spring.rest.OwnerRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OwnerRestControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	OwnerRepository repo;

	@MockBean
	PetRepository prepo;

	@InjectMocks
	OwnerRestController rc;

	@Test
	void testFindById() throws Exception {

		Mockito.when(repo.findById(1)).thenReturn(Optional.of(new Owner(1, "Arjun Chowdhary", "RJy", 235363278)));

		mvc.perform(get("/owners/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.ownerName").value("Arjun Chowdhary"));
	}

	@Test
	void testFindByIdNegative() throws Exception {

		Mockito.when(repo.findById(1)).thenReturn(Optional.of(new Owner(1, "Arjun Chowdhary", "RJy", 235363278)));

		mvc.perform(get("/owners/2").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner not found"));
	}

	@Test
	void testFindAllOwner() throws Exception {
		List<Owner> ownerList = new ArrayList<>();
		ownerList.add(new Owner(1, "Arjun Chowdhary", "Rjy", 839823474));
		Mockito.when(repo.findAll()).thenReturn(ownerList);

		mvc.perform(get("/owners/get").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].ownerName").value("Arjun Chowdhary"));
	}

	@Test
	void shouldSaveOwner() throws Exception {
		Owner owners = new Owner(4, "Karthik", "HYD", 376726347);
		Mockito.when(repo.save(owners)).thenReturn(owners);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owners);

		mvc.perform(post("/owners/save").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void shouldSaveOwnerNegative() throws Exception {
		Owner owners = new Owner(4, "Karthik", "HYD", 376726347);
		Mockito.when(repo.existsById(4)).thenReturn(true);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owners);

		mvc.perform(post("/owners/save").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner already exists"));
	}

	@Test
	void shouldUpdateOwner() throws Exception {
		Owner owners = new Owner(4, "Karthik", "RJy", 376726347);
		
		Mockito.when(repo.findById(4)).thenReturn(Optional.of(owners));
		Mockito.when(repo.save(owners)).thenReturn(owners);
		when(repo.existsById(4)).thenReturn(true);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owners);

		mvc.perform(put("/owners/update/4").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner updated"));
	}

	@Test
	void shouldUpdateOwnerNegative() throws Exception {
		Owner owners = new Owner(4, "Karthik", "RJy", 376726347);
		Mockito.when(repo.save(owners)).thenReturn(owners);
		when(repo.existsById(4)).thenReturn(true);

		ObjectMapper om = new ObjectMapper();
		String ownerJson = om.writeValueAsString(owners);

		mvc.perform(put("/owners/update/5").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void shouldDeleteOwnerById() throws Exception {
		int id = 1;
		when(repo.existsById(id)).thenReturn(true);

		mvc.perform(delete("/owners/delete/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("Owner deleted"));
	}

	@Test
	void shouldDeleteOwnerByIdNegative() throws Exception {
		int id = 1;
		when(repo.existsById(id)).thenReturn(false);

		mvc.perform(delete("/owners/delete/2")).andDo(print())
				.andExpect(jsonPath("$.status").value("Owner does not exists"));
	}

	@Test
	void testFindPetByOwner() throws Exception {
		int id = 1;
		Owner owner = new Owner(4, "Karthik", "RJy", 376726347);
		owner.addPet(new Pet(2, "Coco", "Dog", 10));
		owner.addPet(new Pet(3, "Cola", "Dog", 10));
		Mockito.when(repo.findById(id)).thenReturn(Optional.of(owner));

		mvc.perform(get("/owners/get/4").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// .andExpect(jsonPath("$.[0].petName").value("Coco"));
	}

	@Test
	void shouldSavePetToOwner() throws Exception {

		Owner owners = new Owner(4, "Karthik", "HYD", 376726347);

		Pet pet = new Pet(2, "Coco", "Dog", 10);
		owners.addPet(pet);
		Mockito.when(repo.findById(4)).thenReturn(Optional.of(owners));
		Mockito.when(repo.save(owners)).thenReturn(owners);

		ObjectMapper om = new ObjectMapper();
		String petJson = om.writeValueAsString(pet);

		mvc.perform(post("/owners/4/pet").contentType(MediaType.APPLICATION_JSON).content(petJson))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void shouldSavePetToOwnerNegative() throws Exception {

		Owner owners = new Owner(4, "Karthik", "HYD", 376726347);

		Pet pet = new Pet(2, "Coco", "Dog", 10);
		owners.addPet(pet);
		Mockito.when(repo.findById(4)).thenReturn(Optional.of(owners));

		ObjectMapper om = new ObjectMapper();
		String petJson = om.writeValueAsString(pet);

		mvc.perform(post("/owners/5/pet").contentType(MediaType.APPLICATION_JSON).content(petJson))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("Owner is not found"));
	}

	@Test
	void testDeletePet() throws Exception {

		Owner owner = new Owner(1,"saru","delhi",83627223);
		List<Pet> pet = new ArrayList<>();
		pet.add( new Pet(1, "chiku", "dog",10,owner));
		pet.add( new Pet(2, "chiku", "dog",10, owner));
		when(repo.findById(1)).thenReturn(Optional.of(owner));
		when(prepo.existsById(1)).thenReturn(true);

		mvc.perform(delete("/owners/pets/delete/1/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value("Pet Deleted"));
	}
	
	@Test
	void testDeletePetNegative() throws Exception {

		Owner owner = new Owner(1,"saru","delhi",83627223);
		List<Pet> pet = new ArrayList<>();
		pet.add( new Pet(1, "chiku", "dog",10,owner));
		pet.add( new Pet(2, "chiku", "dog",10, owner));
		when(repo.findById(1)).thenReturn(Optional.of(owner));
		when(prepo.existsById(1)).thenReturn(false);

		mvc.perform(delete("/owners/pets/delete/2/2"))
		.andExpect(jsonPath("$.status").value("owner with id not found"));

	}
}
