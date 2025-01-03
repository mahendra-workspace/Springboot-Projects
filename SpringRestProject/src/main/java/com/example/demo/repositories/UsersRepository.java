package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

	
	Optional<Users> findByUsername(String username);
	
	Optional<Users> findByGmail(String gmail);
	
}
