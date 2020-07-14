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
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.ParametrosInvalidos;
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
	public String nuevoUsuario(@ModelAttribute("usuario") @Valid UsuarioDTO usuarioDTO, BindingResult result) {
		log.info("post - registro");
		String view = "redirect:/";

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

}
