package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@RestController
@RequestMapping("/usuario")
public class RESTUsuario {

	@Autowired
	private InterfaceSistemaGestionEventos gestorEventos;

	@GetMapping("/ping")
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

	@GetMapping("/{login}")
	UsuarioDTO getUsuario(@PathVariable String login, @RequestParam long id) {
		UsuarioDTO usuario = gestorEventos.getUsuario(id);

		if (usuario.getLogin().equals(login))
			throw new UsuarioNoRegistrado();

		return usuario;
	}

	@GetMapping("/{login}/inscritos")
	List<EventoDTO> listarInscritos(@PathVariable String login, @RequestParam long id) {
		UsuarioDTO u = new UsuarioDTO();

		u.setLogin(login);
		u.setUId(id);

		return gestorEventos.listarEventosInscritosDeUnUsuario(u);
	}

	@GetMapping("/{login}/creados")
	List<EventoDTO> listarCreados(@PathVariable String login, @RequestParam long id) {
		UsuarioDTO u = new UsuarioDTO();

		u.setLogin(login);
		u.setUId(id);

		return gestorEventos.listarEventosCreadosPorUnUsuario(u);
	}

}
