package com.fastcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fastcart.model.Role;
import com.fastcart.model.User;
import com.fastcart.service.interf.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new User()); // Crea un nuevo objeto DTO
	    return "register"; // Nombre de la plantilla
	}
	
	@PostMapping("/user/register")
	public String register(@ModelAttribute("user") User user, Model model,  RedirectAttributes redirectAttributes) {
		user.setRole(Role.ROLE_USER);
		String error = userService.register(user);
		
		
	    if (error != null) {
	        model.addAttribute("error", error); // Agregar el mensaje de error al modelo
	        return "register"; // Regresar al formulario de registro
	    }
	    
		redirectAttributes.addFlashAttribute("registered", true);

	    
        return "redirect:/login"; // redirigir a una página de éxito

	}
	
}
