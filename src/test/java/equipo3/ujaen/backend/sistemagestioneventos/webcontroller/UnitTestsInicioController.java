package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import equipo3.ujaen.backend.sistemagestioneventos.dao.EventoDao;
import equipo3.ujaen.backend.sistemagestioneventos.dao.UsuarioDao;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@ActiveProfiles("test")
@WebMvcTest(InicioController.class)
public class UnitTestsInicioController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    InterfaceSistemaGestionEventos sge;

    @MockBean
    private EventoDao eventoDao;

    @MockBean
    private UsuarioDao usuarioDao;

    EventoDTO crearEventoValido() {
	LocalDateTime manana = LocalDateTime.now().plusDays(1);

	int aforoMaximo = 1500;
	String descripcion = "El evento al que todo el mundo vendrá";
	LocalDateTime fecha = manana;
	Long idEvento = null;
	String lugar = "Jaén";
	EventoDTO.TipoEvento tipoEvento = TipoEvento.NO_BENEFICO;
	EventoDTO.CategoriaEvento categoriaEvento = CategoriaEvento.EXCURSIONES;
	int numAsistentes = 603;
	int numListaEspera = 0;
	EstadoUsuarioEvento estado = null;
	UUID idCreador = null;
	String titulo = null;
	String foto = null;

	return new EventoDTO(idEvento, aforoMaximo, descripcion, fecha, lugar, tipoEvento, categoriaEvento, idCreador,
		numAsistentes, numListaEspera, estado, titulo, foto);
    }

    @Test
    void testIndexPage() throws Exception {
	mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void testBuscaEvento() throws Exception{
	mvc.perform(post("/")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.param("buscarNombre", "1")
		.with(csrf()))
	.andExpect(ResultMatcher.matchAll(
		status().is3xxRedirection(),
		redirectedUrl("/"),
		flash().attribute("busqueda", "1")
		));
    }

    @Test
    void testFiltrando() throws Exception{
	String busqueda = "1";
	List<EventoDTO> eventos = new ArrayList<EventoDTO>();
	eventos.add(crearEventoValido());

	BDDMockito.given(sge.listarEventos(Optional.empty(), null, "", busqueda, 0, 10))
	.willReturn(eventos);
	mvc.perform(get("/").param("busqueda", busqueda))
	.andExpect(model().attribute("filtroTitulo", busqueda));
    }


}
