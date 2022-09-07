package com.demo.spring;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.entity.Vet;

public interface VetRepository extends JpaRepository<Vet, Integer>{

}
