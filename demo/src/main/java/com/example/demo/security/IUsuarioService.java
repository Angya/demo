package com.example.demo.security;

import com.example.demo.dto.User;

public interface IUsuarioService {
	public User findByUsername(String username);
}
