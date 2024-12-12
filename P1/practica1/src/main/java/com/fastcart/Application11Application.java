package com.fastcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.fastcart.model.Role;
import com.fastcart.model.User;
import com.fastcart.service.interf.UserService;

@SpringBootApplication
public class Application11Application {
	public static void main(String[] args) {
		SpringApplication.run(Application11Application.class, args);
	}

	@Bean
	CommandLineRunner jpaSample(UserService userService) {
		return (args) -> {
			if (!userService.doesThisUserExist("admin")) {
				User admin = new User();
				admin.setRole(Role.ROLE_ADMIN);
				admin.setUsername("admin");
				admin.setPassword("admin123");
				userService.register(admin);
			}
			if (!userService.doesThisUserExist("user")) {
				User user = new User();
				user.setRole(Role.ROLE_USER);
				user.setUsername("user");
				user.setPassword("user123");
				userService.register(user);
			}
		};
	}

}