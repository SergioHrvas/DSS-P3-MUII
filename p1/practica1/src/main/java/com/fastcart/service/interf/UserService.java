package com.fastcart.service.interf;

import com.fastcart.model.User;

public interface UserService {
	/**
	 * Registra un nuevo usuario en el sistema
	 * 
	 * @param user Los datos del usuario a crear
	 * @return Obtiene el usuario registrado.
	 */
	public String register(User user);
	
	/**
	 * Verifica si ya existe un usuario en el sistema
	 * 
	 * @param userName El nombre del usuario
	 * @return Si existe o no el usuario.
	 */
	public boolean doesThisUserExist(String userName);
}
