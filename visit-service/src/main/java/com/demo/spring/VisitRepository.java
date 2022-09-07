package com.demo.spring;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.spring.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
	
	@Query("select v from Visit v where petId in ?1")
	public List<Visit> findAllByPetId(Collection<Integer> petIds);
	
	public List<Visit> findByPetId(int petId);
}
