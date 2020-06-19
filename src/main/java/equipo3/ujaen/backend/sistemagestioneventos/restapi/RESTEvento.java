package equipo3.ujaen.backend.sistemagestioneventos.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
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

}
