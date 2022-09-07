package com.demo.spring;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.spring.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Integer>{

}
