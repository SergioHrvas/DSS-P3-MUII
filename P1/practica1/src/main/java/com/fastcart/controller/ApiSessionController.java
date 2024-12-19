package com.fastcart.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ApiSessionController {

    // Verificar si la sesión es válida
	@GetMapping("/verify-session")
	public ResponseEntity<Map<String, Object>> verifySession(HttpServletRequest request, HttpSession session) {
	    // Obtener el JSESSIONID de las cookies
	    String sessionIdFromCookies = null;
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals("JSESSIONID")) {
	                sessionIdFromCookies = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (sessionIdFromCookies == null) {
	        return ResponseEntity.badRequest().body(Map.of("message", "Session ID is missing"));
	    }

	    // Verificamos si la sesión aún está activa
	    String storedSessionId = session.getId();

	    // Comparamos el JSESSIONID del cliente con el del servidor
	    if (sessionIdFromCookies.equals(storedSessionId)) {
	        // Si las sesiones coinciden, significa que la sesión es válida
	        return ResponseEntity.ok(Map.of("valid", true));
	    } else {
	        // Si no coinciden, la sesión ha expirado o no es válida
	        return ResponseEntity.status(401).body(Map.of("valid", false));
	    }
	}

}
