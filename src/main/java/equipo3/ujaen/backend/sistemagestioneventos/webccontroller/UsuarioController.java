package equipo3.ujaen.backend.sistemagestioneventos.webccontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	InterfaceSistemaGestionEventos ige;
}
