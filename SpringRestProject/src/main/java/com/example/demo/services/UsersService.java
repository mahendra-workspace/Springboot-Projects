package com.example.demo.services;

import com.example.demo.entities.Users;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.UsersRepository;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository; 
	
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    AuthenticationManager authManager;
    
    @Autowired
    private JWTService jwtService;
	
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
	
	@Transactional
    public void deleteUser(String username) {
		
		
        Optional<Users> userOpt = usersRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        Users user = userOpt.get();

        // Delete related employee
        employeeRepository.deleteByUser(user);



        // Delete the user itself
        usersRepository.delete(user);
    }

	public Map<String, String> verifyUser(Users user) {
		
	    // Determine if the user provided a username, email, or phone number
	    String credential = user.getUsername(); // Default to username field

	    // Check for phone number or email
	    if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
	        credential = user.getPhoneNumber();
	    } else if (user.getGmail() != null && !user.getGmail().isEmpty()) {
	        credential = user.getGmail();
	    }
	    
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(credential, user.getPassword()));
	    if (authentication.isAuthenticated()) {
	        Users fullUser = usersRepository.findByUsernameOrGmailOrPhoneNumber(credential, credential, credential)
                    .orElseThrow(() -> new RuntimeException("User not found"));

	        // Generate access and refresh tokens
	        String accessToken = jwtService.generateAccessToken(fullUser);
	        String refreshToken = jwtService.generateRefreshToken(fullUser);

	        // Return tokens in a response map
	        Map<String, String> tokens = new HashMap<>();
	        tokens.put("accessToken", accessToken);
	        tokens.put("refreshToken", refreshToken);
	        return tokens;
	    }
		
	    throw new RuntimeException("Login Failed");
	}
}
