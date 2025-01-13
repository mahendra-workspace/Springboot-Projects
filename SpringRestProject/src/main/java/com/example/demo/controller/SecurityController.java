package com.example.demo.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

	@GetMapping("/csrf_token")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
}
