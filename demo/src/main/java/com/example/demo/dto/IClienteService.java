package com.example.demo.dto;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IClienteService {
	public List<Clientes> findAll();
	
	public Page<Clientes> findAll(Pageable pageable);
	
	public Clientes findById(Long id);


}
