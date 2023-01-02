package com.vidya.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vidya.dao.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}