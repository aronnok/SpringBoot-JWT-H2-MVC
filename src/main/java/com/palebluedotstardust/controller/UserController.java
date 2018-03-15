package com.palebluedotstardust.controller;

import java.time.*;
import java.util.Date;

import javax.servlet.ServletException;

import com.palebluedotstardust.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.palebluedotstardust.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	public String login(@RequestBody User login) throws ServletException {

		String jwtToken = "";

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


		LocalDateTime ldt = LocalDateTime.now(Clock.systemUTC());
		Instant creatation = ldt.toInstant(ZoneOffset.UTC);
		Date jwtCreation = Date.from(creatation);

		ldt.plusSeconds(240);

		Instant instant = ldt.toInstant(ZoneOffset.UTC);
		Date expirationDate = Date.from(instant);


		System.out.println(jwtCreation);
		System.out.println(expirationDate);
		LocalDateTime currentTime = LocalDateTime.now();

		jwtToken = Jwts.builder().setSubject(email).claim("roles", "user")
				.setExpiration(Date.from(currentTime
						.plusMinutes(2)
						.atZone(ZoneId.systemDefault()).toInstant()))
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, "secretkey")
				.compact();

		return jwtToken;
	}
}
