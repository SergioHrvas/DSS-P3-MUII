package com.fastcart.config;
import org.springframework.security.web.context.SecurityContextRepository;

import com.fastcart.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class LoginRequest{
	String username;
	String password;
	
    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
public class CustomApiLoginFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public CustomApiLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // No crear una nueva sesión, solo obtener la existente
        if (session != null) {
            SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
            SecurityContext securityContext = securityContextRepository.loadContext(new HttpRequestResponseHolder(request, response));

            Authentication authentication = securityContext.getAuthentication();
            System.out.println("Authentication: " + authentication);

            // Verifica si el usuario está autenticado
            if (authentication != null && authentication.isAuthenticated()) {
                SecurityContextHolder.setContext(securityContext);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Si no está autenticado, manejar la lógica de login
        if (request.getRequestURI().equals("/api/login") && request.getMethod().equalsIgnoreCase("POST")) {
            handleLogin(request, response, filterChain);
            return;
        }

        // Continuar con el filtro en caso de no necesitar autenticación especial
        filterChain.doFilter(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
    	 // Leer el cuerpo de la solicitud (request body) como una cadena
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Convertir el cuerpo de la solicitud a un String (JSON)
        String body = requestBody.toString();
        System.out.println("Request Body: " + body);

        // Usar ObjectMapper para deserializar el JSON en un objeto LoginRequest
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = objectMapper.readValue(body, LoginRequest.class);
        
        // Ahora puedes obtener los valores de username y password
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authResult = authenticationManager.authenticate(authRequest);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(true); // Crear una nueva sesión si no existe
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("role", authResult.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).findFirst()
                    .orElse("ROLE_USER"));
            sessionData.put("sessionId", session.getId());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            ObjectMapper objectMapper1 = new ObjectMapper();
            response.getWriter().write(objectMapper1.writeValueAsString(sessionData));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication failed\"}");
        }
    }


}
