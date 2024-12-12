package com.fastcart.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fastcart.model.Cart;

import com.fastcart.model.User;
import com.fastcart.repository.CartRepo;
import com.fastcart.repository.UserRepo;
import com.fastcart.service.interf.UserService;

@Service
public class UserServiceImpl implements UserService {

	// Repositorios necesarios y atributos
	private final UserRepo userRepo;
	private final CartRepo cartRepo;

	PasswordEncoder passwordEncode;

	// Constructor
	public UserServiceImpl(UserRepo userRepo, CartRepo cartRepo) {
		this.userRepo = userRepo;
		this.cartRepo = cartRepo;
		this.passwordEncode = new BCryptPasswordEncoder();
	}

	// Métodos
	public String register(User user) {

		// Verificar si el nombre de usuario ya existe
		if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return "El nombre de usuario ya está en uso."; 
		}

		// Creamos el usuario
		User new_user = new User();
		new_user.setUsername(user.getUsername());
		new_user.setPassword(passwordEncode.encode(user.getPassword())); // Encriptar la contraseña
		new_user.setRole(user.getRole());

		// Crear y asociar un nuevo carrito
		Cart cart = new Cart();
		new_user.setCart(cart);

		// Guardar usuario y actualizar id en carrito para referenciación mutua.
		User usuario = userRepo.save(new_user);
		cart.setUser(usuario);
		cartRepo.save(cart);

        return null; // Si no hay errores, devuelvo null
	}
	
	public boolean doesThisUserExist(String userName) {
		Optional<User> user = userRepo.findByUsername(userName);
		
		boolean existe = false;
		if(user.isPresent()) {
			existe = true;
		}
		
		return existe;
		
	}

}
