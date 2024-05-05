package com.dressmeup.DressMeUpAPI;

import com.dressmeup.DressMeUpAPI.entities.Role;
import com.dressmeup.DressMeUpAPI.services.RoleService;
import com.dressmeup.DressMeUpAPI.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DressMeUpApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DressMeUpApiApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {
			Role roleUser = roleService.save(new Role(Role.ROLE_USER));
		};
	}

}
