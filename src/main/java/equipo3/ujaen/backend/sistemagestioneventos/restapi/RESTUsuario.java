package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@RestController
@RequestMapping("/usuario")
public class RESTUsuario {

	@Autowired
	private InterfaceSistemaGestionEventos gestorEventos;

	@GetMapping("/ping")
	@ResponseStatus(code = HttpStatus.OK)
	void ping() {

	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	void registrar(@RequestBody UsuarioDTO usuarioDTO) {
		gestorEventos.registroUsuarios(usuarioDTO.getLogin(), usuarioDTO.getPassword());
	}

	@PutMapping
	UsuarioDTO login(@RequestBody UsuarioDTO usuarioDTO) {
		return gestorEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());
	}

}
