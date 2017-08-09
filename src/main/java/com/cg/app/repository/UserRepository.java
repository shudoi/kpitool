package com.cg.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
