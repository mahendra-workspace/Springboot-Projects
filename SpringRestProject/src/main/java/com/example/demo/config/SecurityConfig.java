package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.services.MyUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
//		//disable csrf
//		httpSecurity.csrf(Customizer->Customizer.disable());  
//		//Enable Authentication
//		httpSecurity.authorizeHttpRequests(request->request.anyRequest().authenticated());
//		//This configures form-based authentication, where the user is presented with a login form to enter their credentials.
//		httpSecurity.formLogin(Customizer.withDefaults());  
//		// This configures HTTP Basic Authentication, a simple authentication mechanism where the client sends username and password credentials in the request header.
//		httpSecurity.httpBasic(Customizer.withDefaults());  
//		//it will create new session_id every time. so no need for csrf
//		httpSecurity.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); 
//		return httpSecurity.build();
		
		//or we can write in short 
		
		
		return httpSecurity
					.csrf(Customizer->Customizer.disable())
					.authorizeHttpRequests(request->request
							.requestMatchers("/api/user/register","/api/user/login","/api/user/refresh-token")
							.permitAll()
							.anyRequest().authenticated())
					.httpBasic(Customizer.withDefaults())
					.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
					.build(); 
		
		
	}
	
	
	//for Custom Spring Security
	@Bean
	public AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(myUserDetailService); 
	    provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
	    return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	//for jWT token
	@Bean
	public AuthenticationManager authenticationMangager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
		
	}


}
