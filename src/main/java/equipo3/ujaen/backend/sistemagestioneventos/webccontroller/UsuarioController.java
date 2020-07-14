package equipo3.ujaen.backend.sistemagestioneventos.webccontroller;

import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.beans.Principal;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
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
	
	@Autowired
	Principal principal;
	
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
	@GetMapping("/login")
	public String login(ModelMap model) {
		log.info("CONTROLADOR LOGIN!!");
		model.addAttribute("usuario", new UsuarioDTO());
		return "login";
	}
	
	@PostMapping("/login")
	public String comprobar(@ModelAttribute("usuario") @Valid UsuarioDTO usuario,
			BindingResult result,  RedirectAttributes redAttr) {
		
		String view="login";
		
		log.info("GET OBJECT NAME - "+result.getObjectName());
		log.info("NestedPath - "+result.getNestedPath());
		try {
		 if (!result.hasErrors()) {
			UUID uuid=ige.loginUsuario(usuario.getLogin(), usuario.getPassword());
			log.info("Iniciando login!!"+uuid);	
			principal.setName(uuid.toString());
			view="redirect:/";
		 }else {
			 UUID uuid=ige.loginUsuario(usuario.getLogin(), usuario.getPassword());
			 log.info("Iniciando login!!"+uuid.toString());
			 log.info("login!!"+usuario.getLogin());
			 log.info("pass!!"+usuario.getPassword());
		 }
		}catch(ParametrosInvalidos p) {
			result.rejectValue("terminos", "error.usuario.login", "Login o contraseña invalidos");		
			log.info("Login o contraseña invalidos-1");
		}catch (UsuarioNoRegistrado e) {
			result.rejectValue("terminos", "error.usuario.login", "Login o contraseña invalidos");
			log.info("Login o contraseña invalidos-2");
		}catch (AccesoDenegado a) {
			result.rejectValue("password", "error.usuario.login", "Contraseña incorrecta");
			log.info("Contraseña incorrecta-3");
		}
		log.info("REDIRECCIONANDO A "+view);
		 return view;
	}


}
