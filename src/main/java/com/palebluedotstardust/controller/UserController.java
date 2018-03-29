package com.palebluedotstardust.controller;

import java.net.URI;
import java.time.*;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.xml.ws.Response;

import com.palebluedotstardust.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.palebluedotstardust.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public User registerUser(@RequestBody User user) {
		return userService.save(user);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity login(@RequestBody User login) throws ServletException {

		String jwtToken = "";
        HashMap<String,String> responseMap = new HashMap<>();

		if (login.getEmail() == null || login.getPassword() == null) {
			throw new ServletException("Please fill in username and password");
		}

		String email = login.getEmail();
		String password = login.getPassword();

		User user = userService.findByEmail(email);

		if (user == null) {
			throw new ServletException("User email not found.");
		}

		String pwd = user.getPassword();

		if (!password.equals(pwd)) {
			throw new ServletException("Invalid login. Please check your name and password.");
		}

		LocalDateTime currentTime = LocalDateTime.now();

		jwtToken = Jwts.builder().setSubject(email).claim("roles", "user")
				.setExpiration(Date.from(currentTime
						.plusMinutes(2)
						.atZone(ZoneId.systemDefault()).toInstant()))
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, "secretkey")
				.compact();


        responseMap.put("token",jwtToken);
        responseMap.put("expirationTime","120");
        ResponseEntity<HashMap> responseEntity = new ResponseEntity<>(responseMap,
                HttpStatus.OK);

        return responseEntity;
        
	}
}
