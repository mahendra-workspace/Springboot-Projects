package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;

@RestController
@RequestMapping("/api/user")
public class UsersController {

	@Autowired
	private UsersService usersService;
	
	
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
    
    
    
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            usersService.deleteUser(username);
            return new ResponseEntity<>("User and related data deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
}
