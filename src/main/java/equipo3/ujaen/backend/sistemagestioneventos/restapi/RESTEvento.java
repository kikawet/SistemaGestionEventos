package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@RestController
@RequestMapping("/evento")
public class RESTEvento {

	@Autowired
	private InterfaceSistemaGestionEventos gestorEventos;

	@GetMapping("/ping")
	@ResponseStatus(code = HttpStatus.OK)
	void ping() {
	}

	static List<EventoDTO> addLinks(List<EventoDTO> eventos) {
		for (EventoDTO evento : eventos) {
			Link selfLink = linkTo(methodOn(RESTEvento.class).getEvento(evento.getIdEvento(), null)).withSelfRel();
			evento.add(selfLink);
		}

		return eventos;
	}

	@GetMapping
	CollectionModel<EventoDTO> listarEventos(@RequestParam(required = false) CategoriaEvento categoria,
			@RequestParam(required = false, defaultValue = "") String descripcion,
			@RequestParam(required = false, defaultValue = "10") int cantidad) {

		List<EventoDTO> eventos = gestorEventos.listarEventos(categoria, descripcion, cantidad);

		eventos = addLinks(eventos);

		Link selfLink = linkTo(RESTEvento.class).withSelfRel();

		CollectionModel<EventoDTO> resultado = new CollectionModel<>(eventos);

		resultado.add(selfLink);

		return resultado;
	}

	@GetMapping("/{id}")
	EventoDTO getEvento(@PathVariable(value = "id") long idEvento,
			@RequestParam(required = false, value = "id") Long uId) {
		EventoDTO evento = gestorEventos.getEvento(idEvento);

		if (uId != null && uId != 0) {
			gestorEventos.getUsuario(uId);
			evento.add(linkTo(methodOn(RESTUsuario.class).getUsuario(evento.getIdCreador(), uId)).withRel("creador"));

			EstadoUsuarioEvento estadoUsuarioEvento = gestorEventos.getEstadoUsuarioEvento(uId, idEvento);

			if (estadoUsuarioEvento == null)
				evento.add(linkTo(methodOn(RESTEvento.class).inscribirUsuario(idEvento, uId)).withRel("inscribir"));
//			else // No se puede usar linkTo si el método devuelve null
//				evento.add(linkTo(methodOn(RESTEvento.class).cancelarInscripcion(idEvento, uId)).withRel("cancelarInscribir"));
		}
		return evento;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	void crearEvento(@RequestBody EventoDTO evento, @RequestParam(value = "id") long uId,
			@RequestParam(required = false, defaultValue = "false") boolean inscribir) {
		UsuarioDTO creador = gestorEventos.getUsuario(uId);

		gestorEventos.crearEventoPorUsuario(creador, evento, inscribir);
	}

	@DeleteMapping("/{id}")
	void cancelarEvento(@PathVariable(value = "id") long idEvento, @RequestParam(value = "id") long uId) {
		UsuarioDTO u = gestorEventos.getUsuario(uId);

		gestorEventos.cancelarEventoPorUsuario(u, idEvento);
	}

	@PostMapping("/{id}/inscripcion")
	EstadoUsuarioEvento inscribirUsuario(@PathVariable(value = "id") long idEvento,
			@RequestParam(value = "id") long uId) {
		UsuarioDTO u = gestorEventos.getUsuario(uId);

		return gestorEventos.inscribirUsuario(u, idEvento);
	}

	@DeleteMapping("/{id}/inscripcion")
	void cancelarInscripcion(@PathVariable(value = "id") long idEvento, @RequestParam(value = "id") long uId) {
		UsuarioDTO u = gestorEventos.getUsuario(uId);

		gestorEventos.cancelarInscripcionUsuario(u, idEvento);
	}

}
