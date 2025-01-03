package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Users;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.UsersService;

@RestController
public class UsersController {

	private final UsersService usersService;
	
	@Autowired
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}
	
	@GetMapping("/users")
	public Optional<List<Users>> getAllUsers(){
		return usersService.getAllUsers();
	}
	
	@GetMapping("/users/{username}")
	public Optional<Users> getUserByUsername(@PathVariable String username){
		return usersService.getByUsername(username);
	}
	
	@GetMapping("/user/{gmail}")
	public Optional<Users> getUserByGmail(@PathVariable String gmail){
		return usersService.getByGmail(gmail);
	}
	
	@PostMapping("/user")
	public Users createUser(@RequestBody Users user) {
		return usersService.saveUsers(user);
	}
	
    @PutMapping("/{userId}")
    public Users updateUser(@PathVariable String username, @RequestBody Users updatedUser) {
        return usersService.updateUser(username, updatedUser); 
    }
	
	
}
