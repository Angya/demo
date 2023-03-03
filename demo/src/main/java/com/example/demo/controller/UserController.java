package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//import com.example.demo.dto.IUsuarioDao;
import com.example.demo.dto.Clientes;
import com.example.demo.dto.IClienteService;
import com.example.demo.encrypt.StringEncrypt;

@CrossOrigin(origins = { "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private StringEncrypt scr;
	// private final Logger log =
	// LoggerFactory.getLogger(ClienteRestController.class);

	@GetMapping("/clientes")
	public List<Clientes> index() {
		return clienteService.findAll();
	}

	@GetMapping("/clientes/page/{page}")
	public Page<Clientes> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Clientes cliente = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Clientes>(cliente, HttpStatus.OK);
	}
	
		@GetMapping("/clientes/pokemon/{nombre}")
		public Object pokemon(@PathVariable String nombre) {

			RestTemplate restemplate = new RestTemplate();
			String uri = "https://pokeapi.co/api/v2/pokemon/";
			Object objeto = restemplate.getForObject(uri + nombre, Object.class);
			return objeto;
		}

		@GetMapping("/clientes/encriptar/{palabra}")
		public Object encriptar(@PathVariable String palabra) throws Exception {

			String key = "92AE31A79FEEB2A3"; // llave
			String iv = "0123456789ABCDEF"; // vector de inicialización
			String cleartext = palabra;
			//System.out.println("Texto encriptado: " + scr.encrypt(key, iv, cleartext));

			return scr.encrypt(key, iv, cleartext);
		}

		@GetMapping("/clientes/desencriptar/{palabra}/{encriptado}")
		public Object desencriptar(@PathVariable String palabra, @PathVariable String encriptado) throws Exception {

			String key = "92AE31A79FEEB2A3"; // llave
			String iv = "0123456789ABCDEF"; // vector de inicialización

			//System.out.println("Texto desencriptado: " + scr.decrypt(key, iv, scr.encrypt(key, iv, palabra)));

			return scr.decrypt(key, iv, scr.encrypt(key, iv, palabra));
		}
	



}
