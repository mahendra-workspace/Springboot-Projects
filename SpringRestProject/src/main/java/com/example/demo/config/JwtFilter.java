package com.example.demo.config;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.services.JWTService;
import com.example.demo.services.MyUserDetailService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	ApplicationContext context;
	
    @Autowired
    private MyUserDetailService myUserDetailService;
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		String authHeader = request.getHeader("Authorization");
//		String token= null;
//		String  username = null;
//		
//		if(authHeader!= null && authHeader.startsWith("Bearer")) {
//			token = authHeader.substring(7);
//			username = jwtService.extractUsername(token);
//		}
//		
//		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
//			
//			UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(username);
//			
//			
//			if(jwtService.validateToken(token,userDetails)) {
//				UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authToken);
//			}
//		}
//		
//		filterChain.doFilter(request, response);
//	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // Extract token

            try {
            	Claims claims = jwtService.extractAllClaims(token);

            	boolean isAccessToken = !claims.containsKey("type") || !"refresh".equals(claims.get("type"));
                if (!isAccessToken) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
                    return;
                }
            	
                String credential = claims.getSubject();
            	
                if (credential != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = myUserDetailService.loadUserByUsername(credential);

                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Unauthorized if token is invalid
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
