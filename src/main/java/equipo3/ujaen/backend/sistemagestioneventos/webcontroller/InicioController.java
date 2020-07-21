package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTODetails;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Controller
@RequestMapping("/")
public class InicioController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(InicioController.class);

    @Autowired
    InterfaceSistemaGestionEventos ige;

    // private void initController (WebDataBinder web) {
    // log.info("Iniciando controlador!!");
    // }

    @GetMapping(path = { "/" })
    public String listado(ModelMap model, @ModelAttribute("busqueda") String tituloParcial, @AuthenticationPrincipal
	    UsuarioDTODetails principal) {

	UUID uId = null;

	if(principal!= null)
	    uId = principal.getUsuario().getUId();

	if (tituloParcial == null) {
	    tituloParcial = "";
	}

	List<EventoDTO> eventos = ige.listarEventos(Optional.ofNullable(uId), null, "", tituloParcial, 0, 10);//ige.listarEventos(null, "", tituloParcial, 0, 10);
	model.addAttribute("eventos", eventos);
	model.addAttribute("filtroTitulo",tituloParcial);
	log.info("Cargando eventos!!" + eventos.size());

	return "index";
    }

    @PostMapping
    public String busqueda(@RequestParam(value = "buscarNombre") String busqueda,@RequestParam(required = false) boolean limpiar, RedirectAttributes redirect) {
	if(!limpiar) {
	    redirect.addFlashAttribute("busqueda", busqueda);
	}

	return "redirect:/";
    }
}
