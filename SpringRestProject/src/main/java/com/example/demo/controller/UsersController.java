package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Users;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.JWTService;
import com.example.demo.services.LocalS3Service;
import com.example.demo.services.UsersService;

@RestController
@RequestMapping("/api/user")
public class UsersController {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UsersRepository userRepository;
	
    @Autowired
    private LocalS3Service localS3Service;
    
    @Autowired
    private JWTService jwtService;
    
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	
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
	
	@PostMapping("/register")
	public Users createUser(@RequestBody Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return usersService.saveUsers(user);
	}
	
	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Users user) {
		return usersService.verifyUser(user);
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<Map<String, String>> refreshToken(@RequestBody String refreshToken) {
	   

	    if (refreshToken == null || !jwtService.validateRefressToken(refreshToken)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Invalid refresh token"));
	    }

	    String username = jwtService.extractUsername(refreshToken);


	    Users user = userRepository.findByUsername(username)
	    		.orElseThrow(() -> new RuntimeException("User not found"));


	    String newAccessToken = jwtService.generateAccessToken(user);


	    Map<String, String> response = new HashMap<>();
	    response.put("accessToken", newAccessToken);

	    return ResponseEntity.ok(response);
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
    
    @PostMapping("/upload-profile-picture/{username}")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String username, @RequestParam("file") MultipartFile file) {
    	try {
    		
    		String uploadDir = "C:\\Users\\mahendra\\Desktop\\Infobytes\\S3Service";
    		
    		// Ensure the directory exists
    		File directory = new File(uploadDir);
    		if(!directory.exists()) {
    			directory.mkdirs();
    		}
    		
    		// Save the file locally
    		String fileName = username+"_"+file.getOriginalFilename();
    		Path filePath = Paths.get(uploadDir, fileName);
    		Files.write(filePath, file.getBytes());
    		
    		// Generate file URL
    		String fileUrl = "file:///"+filePath.toString().replace("\\", "/");
    		
    		// Update the user's profile picture URL in the database
    		Users user = usersService.getByUsername(username)
    				.orElseThrow(()-> new IllegalArgumentException("User not found"));
    		user.setProfilePictureUrl(fileUrl);
    		usersService.saveUsers(user);
    		
    		return new ResponseEntity<>("Profile Picture uploaded Successfully", HttpStatus.OK);
    		
    	}catch(IOException e) {
    		return new ResponseEntity<>("Error uploading profile picture ", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

	
}
