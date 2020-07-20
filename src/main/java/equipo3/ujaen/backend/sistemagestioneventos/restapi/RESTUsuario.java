package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@RestController
@RequestMapping("/rest/usuario")
public class RESTUsuario {

	@Autowired
	private InterfaceSistemaGestionEventos gestorEventos;

	@GetMapping("/ping")
	void ping() {

	}

	@PostMapping("/registro")
	@ResponseStatus(HttpStatus.CREATED)
	void registrar(@RequestBody UsuarioDTO usuarioDTO) {
		gestorEventos.registroUsuarios(usuarioDTO);
	}

	@PostMapping("/login")
	UUID login(@RequestBody UsuarioDTO usuarioDTO) {
		return gestorEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());
	}

	@GetMapping("/{id}")
	UsuarioDTO getUsuario(@PathVariable UUID id, @RequestParam(value = "id") UUID idUsuarioPeticion) {
		// Comprovamos que el usuario está logeado
		UsuarioDTO usuario = gestorEventos.getUsuario(idUsuarioPeticion);

		UsuarioDTO u = gestorEventos.getUsuario(id);

		if (!u.equals(usuario)) {
			throw new AccesoDenegado("No tienes permitido leer los datos de este usuario");
		}

		if (u.getNumEventosInscritos() > 0)
			u.add(linkTo(methodOn(RESTUsuario.class).listarInscritos(id, 0, 10, null, idUsuarioPeticion))
					.withRel("eventosInscritos"));

		if (u.getNumEventosCreados() > 0)
			u.add(linkTo(methodOn(RESTUsuario.class).listarCreados(id, 0, 10, idUsuarioPeticion))
					.withRel("eventosCreados"));

		return u;
	}

	@GetMapping("/{id}/inscritos")
	CollectionModel<EventoDTO> listarInscritos(@PathVariable UUID id,
			@RequestParam(required = false, defaultValue = "0", value = "page") int pagina,
			@RequestParam(required = false, defaultValue = "10", value = "cant") int cantidad,
			@RequestParam(required = false, value = "estado") EstadoUsuarioEvento estado,
			@RequestParam(value = "id") UUID idUsuarioPeticion) {
		gestorEventos.getUsuario(idUsuarioPeticion);
		UsuarioDTO u = gestorEventos.getUsuario(id);

		List<EventoDTO> eventos = gestorEventos.listarEventosUsuario(id, estado, pagina, cantidad);

		eventos = RESTEvento.addLinks(eventos);

		Link selfLink = linkTo(methodOn(RESTUsuario.class).listarInscritos(id, pagina, cantidad, estado, idUsuarioPeticion)).withSelfRel();

		CollectionModel<EventoDTO> resultado = new CollectionModel<>(eventos);

		resultado.add(selfLink);

		if (pagina > 0)
			resultado.add(linkTo(
					methodOn(RESTUsuario.class).listarInscritos(id, pagina - 1, cantidad, estado, idUsuarioPeticion))
							.withRel("anterior"));

		// La siguiente página ya no tendrá resultados
		if (eventos.size() == cantidad)
			resultado.add(linkTo(
					methodOn(RESTUsuario.class).listarInscritos(id, pagina + 1, cantidad, estado, idUsuarioPeticion))
							.withRel("siguiente"));

		return resultado;
	}

	@GetMapping("/{id}/creados")
	CollectionModel<EventoDTO> listarCreados(@PathVariable UUID id,
			@RequestParam(required = false, defaultValue = "0") int pagina,
			@RequestParam(required = false, defaultValue = "10") int cantidad,
			@RequestParam(value = "id") UUID idUsuarioPeticion) {
		gestorEventos.getUsuario(idUsuarioPeticion);
		UsuarioDTO u = gestorEventos.getUsuario(id);

		List<EventoDTO> eventos = gestorEventos.listarEventosCreadosPorUnUsuario(u, pagina, cantidad);

		eventos = RESTEvento.addLinks(eventos);

		Link selfLink = linkTo(RESTUsuario.class).withSelfRel();

		CollectionModel<EventoDTO> resultado = new CollectionModel<>(eventos);

		resultado.add(selfLink);

		if (pagina > 0)
			resultado.add(linkTo(methodOn(RESTUsuario.class).listarCreados(id, pagina - 1, cantidad, idUsuarioPeticion))
					.withRel("anterior"));

		// La siguiente página ya no tendrá resultados
		if (eventos.size() < cantidad)
			resultado.add(linkTo(methodOn(RESTUsuario.class).listarCreados(id, pagina + 1, cantidad, idUsuarioPeticion))
					.withRel("siguiente"));

		return resultado;
	}

}
