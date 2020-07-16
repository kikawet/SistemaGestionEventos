package equipo3.ujaen.backend.sistemagestioneventos.webccontroller;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTODetails;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoPrescrito;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
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
	public String crearEvento(@ModelAttribute("evento") @Valid EventoDTO eventoDTO, BindingResult result,
			@AuthenticationPrincipal UsuarioDTODetails principal) {

		log.info("post - registro");
		String view = "redirect:/";

		// log.info(eventoDTO.getFecha().toString());
		if (!result.hasErrors()) {
			try {
				ige.crearEventoPorUsuario(principal.getUsuario(), eventoDTO, false);
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

	@PostMapping("/inscribir")
	public String inscribirEvento(@RequestParam(required = false) boolean cancelar, @RequestParam long idEvento,
			@AuthenticationPrincipal UsuarioDTODetails principal, RedirectAttributes redirect) {
		String view = "redirect:/";
		String flashmsg = "";
		try {
			
			if (cancelar) {
				ige.cancelarInscripcionUsuario(principal.getUsuario(), idEvento);
				flashmsg = "Cancelado correctamente";
			} else {
				ige.inscribirUsuario(principal.getUsuario(), idEvento);
				flashmsg = "Incsrito correctamente";
			}
			
			
		} catch (EventoNoRegistrado enr) {
			log.info("Evento no registrado");
			flashmsg = "Error: Evento no registrado";
		} catch (UsuarioNoRegistrado unr) {
			log.info("Usuario no registrado");
			flashmsg = "Error: Usuario no registrado";
		} catch (AccesoDenegado ad) {
			log.info("Acceso denegado");
			flashmsg = "Error: Acceso denegado";
		} catch (EventoPrescrito ep) {
			log.info("Evento preescrito");
			flashmsg = "Error: Evento preescrito";
		}
		
		redirect.addFlashAttribute("estado", flashmsg);
		
		return view;
	}
}
