package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.xml.ws.Action;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTODetails;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.ParametrosInvalidos;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	InterfaceSistemaGestionEventos ige;

	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String resgitro(ModelMap model) {
		log.info("get - registro");
		model.addAttribute("usuario", new UsuarioDTO());
		return "registro";
	}

	@PostMapping("/registro")
	public String nuevoUsuario(@ModelAttribute("usuario") @Valid UsuarioDTO usuarioDTO, BindingResult result,
			@RequestParam(value = "terminos", required = false) boolean terminos, ModelMap model) {
		log.info("post - registro");
		String view = "redirect:/";

		if (!terminos) {
			model.addAttribute("errorTerminos", "Debes de aceptar los términos y condiciones");
		}
		model.addAttribute("terminos", terminos);
		log.info("post - registro - terminos: " + terminos);

		if (!result.hasErrors())
			try {
				ige.registroUsuarios(usuarioDTO);
			} catch (ParametrosInvalidos pi) {
				result.rejectValue("terminos", "error.usuario.registro", pi.getMessage());
				view = "registro";
			} catch (UsuarioYaRegistrado uyr) {
				result.rejectValue("login", "error.usuario.registro", "Ya existe un usuario con ese login");
				view = "registro";
			}
		else
			view = "registro";

		return view;
	}

	@GetMapping("/login")
	public String login(ModelMap model) {
		log.info("get - login");
		model.addAttribute("usuario", new UsuarioDTO());
		return "login";
	}

	@GetMapping("/perfil")
	public String verPerfil(ModelMap model, @AuthenticationPrincipal UsuarioDTODetails principal) {

		List<EventoDTO> creados = ige.listarEventosCreadosPorUnUsuario(principal.getUsuario(), 0, 10);
		List<EventoDTO> inscritos = ige.listarEventosInscritosDeUnUsuario(principal.getUsuario(), 0, 10);
		model.addAttribute("usuario", principal.getUsuario());
		model.addAttribute("creados", creados);
		model.addAttribute("inscritos", inscritos);
		return "perfilUsuario";
	}

}
