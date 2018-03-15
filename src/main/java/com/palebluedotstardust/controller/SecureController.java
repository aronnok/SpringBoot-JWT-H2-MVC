package com.palebluedotstardust.controller;

import com.palebluedotstardust.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.palebluedotstardust.service.UserService;

@RestController
@RequestMapping("/secure")
public class SecureController {

	@Autowired
	private UserService userService;

	@RequestMapping("/user/users")
	public String loginSuccess() {
		return "Login Successful!";
	}

	@RequestMapping(value = "/user/email/{email:.+}", method = RequestMethod.POST)
	public User findByEmail(@PathVariable("email") String email) {

		System.out.println(email);

		return userService.findByEmail(email);
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public User updateUser(@RequestBody User user) {
		return userService.save(user);
	}
}
