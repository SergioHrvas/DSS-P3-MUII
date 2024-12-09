package com.fastcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootUriController {
	@GetMapping("/")
	public String index() {
		return "index"; // Thymeleaf buscará el archivo en templates/index.html
	}

	@GetMapping("/index.html")
	public String redirectToIndex() {
		return "index"; // Thymeleaf buscará el archivo en templates/index.html
	}

	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
}
