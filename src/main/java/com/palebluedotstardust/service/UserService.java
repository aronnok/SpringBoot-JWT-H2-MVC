package com.palebluedotstardust.service;

import com.palebluedotstardust.model.User;

public interface UserService {
	User save(User user);

	User findByEmail(String email);

}
