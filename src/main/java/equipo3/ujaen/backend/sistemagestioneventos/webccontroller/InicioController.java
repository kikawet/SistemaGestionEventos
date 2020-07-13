package equipo3.ujaen.backend.sistemagestioneventos.webccontroller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.qos.logback.classic.Logger;

import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Controller
@RequestMapping("/inicio")
public class InicioController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(InicioController.class);
	
	@Autowired
	InterfaceSistemaGestionEventos ige;

	
	
	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String resgitro() {
		
		return "registro";
	}
}
