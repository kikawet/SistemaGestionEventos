package equipo3.ujaen.backend.sistemagestioneventos.webccontroller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(InicioController.class);

	
	@Autowired
	InterfaceSistemaGestionEventos ige;
	
	
}
