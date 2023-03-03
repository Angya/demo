package com.example.demo.dto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface IUsuarioDao extends CrudRepository<User, Long> {
	
	public User findByUsername(String username);
	
	@Query("select u from Usuario u where u.username=?1")
	public User findByUsername2(String username);

}
