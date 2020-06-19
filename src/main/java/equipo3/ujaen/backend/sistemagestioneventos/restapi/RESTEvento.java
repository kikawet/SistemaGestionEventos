package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping
	List<EventoDTO> listarEventos(@RequestParam(required = false) CategoriaEvento categoria,
			@RequestParam(required = false, defaultValue = "") String descripcion,
//			@RequestParam(required = false, defaultValue = "0") int pagina,
			@RequestParam(required = false, defaultValue = "10") int cantidad) {

		return gestorEventos.listarEventos(categoria, descripcion, cantidad);
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

	@DeleteMapping("/{id}")
	EstadoUsuarioEvento inscribirUsuario(@PathVariable(value = "id") long idEvento,
			@RequestParam(value = "id") long uId) {
		UsuarioDTO u = gestorEventos.getUsuario(uId);

		return gestorEventos.inscribirUsuario(u, idEvento);
	}

	@DeleteMapping("/{id}")
	void cancelarInscripcion(@PathVariable(value = "id") long idEvento, @RequestParam(value = "id") long uId) {
		UsuarioDTO u = gestorEventos.getUsuario(uId);

		gestorEventos.cancelarInscripcionUsuario(u, idEvento);
	}

}
