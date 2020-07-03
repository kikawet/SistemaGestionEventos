package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
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

	@GetMapping("/{id}")
	UsuarioDTO getUsuario(@PathVariable long id, @RequestParam(value = "id") long idUsuarioPeticion) {
		// Comprovamos que el usuario está logeado
		gestorEventos.getUsuario(idUsuarioPeticion);

		UsuarioDTO u = gestorEventos.getUsuario(id);

		if (u.getNumEventosInscritos() > 0)
			u.add(linkTo(methodOn(RESTUsuario.class).listarInscritos(id, 0, 10, idUsuarioPeticion))
					.withRel("eventosInscritos"));

		if (u.getNumEventosCreados() > 0)
			u.add(linkTo(methodOn(RESTUsuario.class).listarCreados(id, 0, 10, idUsuarioPeticion))
					.withRel("eventosCreados"));

		return u;
	}

	@GetMapping("/{id}/inscritos")
	CollectionModel<EventoDTO> listarInscritos(@PathVariable long id,
			@RequestParam(required = false, defaultValue = "0") int pagina,
			@RequestParam(required = false, defaultValue = "10") int cantidad,
			@RequestParam(value = "id") long idUsuarioPeticion) {
		gestorEventos.getUsuario(idUsuarioPeticion);
		UsuarioDTO u = gestorEventos.getUsuario(id);

		List<EventoDTO> eventos = gestorEventos.listarEventosInscritosDeUnUsuario(u, pagina, cantidad);

		eventos = RESTEvento.addLinks(eventos);

		Link selfLink = linkTo(RESTUsuario.class).withSelfRel();

		CollectionModel<EventoDTO> resultado = new CollectionModel<>(eventos);

		resultado.add(selfLink);

		return resultado;
	}

	@GetMapping("/{id}/creados")
	CollectionModel<EventoDTO> listarCreados(@PathVariable long id,
			@RequestParam(required = false, defaultValue = "0") int pagina,
			@RequestParam(required = false, defaultValue = "10") int cantidad,
			@RequestParam(value = "id") long idUsuarioPeticion) {
		gestorEventos.getUsuario(idUsuarioPeticion);
		UsuarioDTO u = gestorEventos.getUsuario(id);

		List<EventoDTO> eventos = gestorEventos.listarEventosCreadosPorUnUsuario(u, pagina, cantidad);

		eventos = RESTEvento.addLinks(eventos);

		Link selfLink = linkTo(RESTUsuario.class).withSelfRel();

		CollectionModel<EventoDTO> resultado = new CollectionModel<>(eventos);

		resultado.add(selfLink);

		return resultado;
	}

}
