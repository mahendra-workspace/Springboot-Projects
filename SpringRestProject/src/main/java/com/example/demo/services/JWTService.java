package com.example.demo.services;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
    private final String secret = "YourSuperSecretKeyForJWTValidationMustBeLongEnough";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	
	public JWTService() {

	}

	public String generateAccessToken(Users user) {
		Map<String, Object> claims = new HashMap<>();
		
	    claims.put("username", user.getUsername());
	    claims.put("gmail", user.getGmail());
	    claims.put("phoneNumber", user.getPhoneNumber());
	    claims.put("roles", user.getRole());
		
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+60*60*1000))
				.and()
				.signWith(secretKey)
				.compact();
	}
	
	public String generateRefreshToken(Users user) {
		
		Map<String, Object> claims = new HashMap<>();
		

	    claims.put("username", user.getUsername());
	    claims.put("type", "refresh");
		
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+60*60*60*1000))
				.and()
				.signWith(secretKey)
				.compact();
	}
		


//	private SecretKey getKey() {
//		try {
//			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//			SecretKey sk = keyGen.generateKey();
//			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//			byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//			return Keys.hmacShaKeyFor(keyBytes);
//		} catch (NoSuchAlgorithmException e) {
//			throw new RuntimeException();
//		}
//
//
//	}
	

	public String extractUsername(String token) {
		
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		Claims claims = extractAllClaims(token);
		
		boolean isAccessToken = !claims.containsKey("type") || !"refresh".equals(claims.get("type"));
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}

	public boolean validateRefressToken(String refreshToken) {
	    try {

	    	Claims claims = extractAllClaims(refreshToken);

	    	boolean isRefreshToken = "refresh".equals(claims.get("type"));

	    	return !claims.getExpiration().before(new Date()) && isRefreshToken;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}




}
