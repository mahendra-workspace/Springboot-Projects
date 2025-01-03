package com.example.demo.services;

import com.example.demo.entities.Users;
import com.example.demo.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

	private final UsersRepository usersRepository; 
	
	@Autowired
	public UsersService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}
	
	
	public Optional<List<Users>> getAllUsers(){
		return Optional.ofNullable(usersRepository.findAll());
	}
	
	public Optional<Users> getByUsername(String username){
		 return usersRepository.findByUsername(username);
	}
	
	public Optional<Users> getByGmail(String gmail){
		return usersRepository.findByGmail(gmail);
	}
	
	public Users saveUsers(Users user) {
		return usersRepository.save(user);
	}
	
	public Users updateUser(String username, Users updateUser) {
		Optional<Users> existingUser = usersRepository.findByUsername(username);
		
		if(existingUser.isPresent()) {
			Users presentUser = existingUser.get();
			presentUser.setFullname(updateUser.getFullname());
			presentUser.setGmail(updateUser.getGmail());
			presentUser.setBirthDate(updateUser.getBirthDate());
			presentUser.setPhoneNumber(updateUser.getPhoneNumber());
			
			return usersRepository.save(presentUser);
		} else {
			throw new RuntimeException("Use with username "+username+" not found");
		}	
	}
}
