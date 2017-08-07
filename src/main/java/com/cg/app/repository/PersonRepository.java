/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cg.app.repository;

import com.cg.app.entity.Person;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p FROM Person p WHERE p.username = :username and p.password = :password")
    List<Person> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("SELECT p FROM Person p WHERE p.username = :username")
    List<Person> findByUsername(@Param("username") String username);
}
