package equipo3.ujaen.backend.sistemagestioneventos.webccontroller;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoPrescrito;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Controller
@RequestMapping("/evento")
public class EventoController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(InicioController.class);

	@Autowired
	InterfaceSistemaGestionEventos ige;

	@RequestMapping(value = "/crea", method = RequestMethod.GET)
	public String crearEvento(ModelMap model) {

		log.info("get - crearEvento");
		model.addAttribute("evento", new EventoDTO());
		return "crearEvento";
	}

	@PostMapping("/crea")
	public String crearEvento(@ModelAttribute("evento") @Valid EventoDTO eventoDTO, BindingResult result) {
		log.info("post - registro");
		String view = "redirect:/";
		// Hay que quitar esto luego
		UsuarioDTO patricio = new UsuarioDTO("Patricio Ruiz", "1234");

		//log.info(eventoDTO.getFecha().toString());
		if (!result.hasErrors()) {
			try {
				ige.crearEventoPorUsuario(patricio, eventoDTO, false);
			} catch (EventoYaRegistrado eyr) {
				result.rejectValue("titulo", "error.evento.crea", "Ya existe este evento");
				view = "crearEvento";
			} catch (EventoPrescrito ep) {
				result.rejectValue("fecha", "error.evento.crea2", "Evento preescrito");
				view = "crearEvento";
			}
		} else {
			view = "crearEvento";
		}
		
		
		return view;
	}
}
