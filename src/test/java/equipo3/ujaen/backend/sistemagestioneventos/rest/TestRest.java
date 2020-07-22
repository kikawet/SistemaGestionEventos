package equipo3.ujaen.backend.sistemagestioneventos.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import equipo3.ujaen.backend.sistemagestioneventos.beans.SistemaGestionEventos;
import equipo3.ujaen.backend.sistemagestioneventos.dao.EventoDao;
import equipo3.ujaen.backend.sistemagestioneventos.dao.UsuarioDao;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;

@ActiveProfiles("test")
@WithMockUser(username = "ajml0012")
//@SpringBootTest(classes = equipo3.ujaen.backend.sistemagestioneventos.ServidorSistemaGestionEventos.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@WebMvcTest
public class TestRest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper jsonObjectMapper;


    @MockBean
    SistemaGestionEventos sge;

    @MockBean
    UsuarioDao usuarioDao;

    @MockBean
    EventoDao eventoDao;

    static final String rootPath = "/rest";

    UsuarioDTO crearUsuarioValido() {
	Long login = new Random().nextLong();
	String loginUsuario = login.toString();
	String passwordUsuario = "123456789a.";

	UsuarioDTO usu = new UsuarioDTO(loginUsuario, passwordUsuario);

	return usu;
    }

    EventoDTO crearEventoValido() {
	LocalDateTime manana = LocalDateTime.now().plusDays(1);

	int aforoMaximo = 1500;
	String descripcion = "El evento al que todo el mundo vendrá";
	LocalDateTime fecha = manana;
	Long idEvento = (long) 0;
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

    @BeforeEach
    void generarDatos() {
	BDDMockito.given(sge.getUsuarioLogin("ajml0012")).willReturn(new UsuarioDTO("ajml0012", "1234"));
	BDDMockito.given(sge.getEvento(0)).willReturn(crearEventoValido());
    }

    @Test
    void testPing() throws Exception {

	mvc.perform(get(rootPath + "/usuario/ping").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	mvc.perform(get(rootPath + "/evento/ping").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void registroYLoginUsuario() throws Exception{
	UsuarioDTO usuarioDTO = crearUsuarioValido();
	UUID UUIDUsuario=usuarioDTO.getUId();



	//registro
	mvc.perform(post(rootPath + "/usuario/registro")
		.contentType(MediaType.APPLICATION_JSON)
		.content(jsonObjectMapper.writeValueAsString(usuarioDTO))
		.accept(MediaType.APPLICATION_JSON))
	.andExpect(ResultMatcher.matchAll(status().isCreated()));


	//login
	BDDMockito.given(sge.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword())).willReturn(UUIDUsuario);
	mvc.perform(post(rootPath + "/usuario/login", UUIDUsuario)
		.contentType(MediaType.APPLICATION_JSON)
		.content(jsonObjectMapper.writeValueAsString(usuarioDTO))
		.accept(MediaType.APPLICATION_JSON))
	.andExpect(ResultMatcher.matchAll(
		status().isOk()
		));


    }

    @Test
    void getUsuarioTest() {

	//FALTA TERMINAR LINEA 149 TIPOS

	UsuarioDTO usuarioDTO=null;
	String idUsuario=UUID.randomUUID().toString();
	UUID uid=UUID.fromString(idUsuario);


	//		BDDMockito.given(sge.getUsuario(uid)).willReturn(usuarioDTO);
	//		mvc.perform(get(rootPath + "/usuario/{uId}", idUsuario)
	//				.accept(MediaType.APPLICATION_JSON))
	//		.andExpect(ResultMatcher.matchAll(status().isOk()));
	//

	//		mvc.perform(get(rootPath + "/usuario/{uId}", idUsuario)
	//				.accept(MediaType.APPLICATION_JSON))
	//				 .andExpect(ResultMatcher.matchAll(
	//						 status().isOk()
	//						 ));

	// sistemaGestionEventos.registroUsuarios(usuarioDTO);
	// UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(),
	// usuarioDTO.getPassword());


	// ResponseEntity<Void> respuesta =
	// restTemplateUsuario.postForEntity("/registro", usuarioDTO, Void.class);

	// Assertions.assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

	// ResponseEntity<UUID> esperado = restTemplateUsuario.postForEntity("/login",
	// usuarioDTO, UUID.class);

	// Assertions.assertEquals(sistemaGestionEventos.getUsuario(esperado.getBody()),
	// usuarioDTO);
    }


    @Test
    void listarInscritosTest() throws Exception {

	UsuarioDTO usuarioDTO = crearUsuarioValido();
	String UUIDusuario = UUID.randomUUID().toString();

	List<EventoDTO> listaEventos = new ArrayList<EventoDTO>();
	listaEventos.add(crearEventoValido());
	listaEventos.add(crearEventoValido());
	BDDMockito.given(sge.listarEventosUsuario(UUID.fromString(UUIDusuario), null, 0, 10)).willReturn(listaEventos);
	mvc.perform(get(rootPath + "/usuario/{uId}/inscritos", UUIDusuario).param("uId", UUIDusuario)
		.accept(MediaType.APPLICATION_JSON))
	.andExpect(ResultMatcher.matchAll(
		status().isOk(),
		content().contentType(MediaType.APPLICATION_JSON),
		jsonPath(".eventoDTOList").isArray(),
		jsonPath(".anterior").doesNotExist(),
		jsonPath(".siguiente").doesNotExist()));

	listaEventos.remove(0);

	BDDMockito.given(sge.listarEventosUsuario(UUID.fromString(UUIDusuario), null, 0, 1)).willReturn(listaEventos);
	mvc.perform(get(rootPath + "/usuario/{uId}/inscritos", UUIDusuario).param("uId", UUIDusuario).param("cant", "1")
		.accept(MediaType.APPLICATION_JSON))
	.andExpect(ResultMatcher.matchAll(
		status().isOk(),
		content().contentType(MediaType.APPLICATION_JSON),
		jsonPath(".eventoDTOList.length()").value(1),
		jsonPath(".anterior").doesNotExist(),
		jsonPath(".siguiente").exists()));

	BDDMockito.given(sge.listarEventosUsuario(UUID.fromString(UUIDusuario), null, 0, 1)).willReturn(listaEventos);
	mvc.perform(get(rootPath + "/usuario/{uId}/inscritos", UUIDusuario).param("uId", UUIDusuario).param("page", "1")
		.accept(MediaType.APPLICATION_JSON))
	.andExpect(ResultMatcher.matchAll(
		status().isOk(),
		content().contentType(MediaType.APPLICATION_JSON),
		jsonPath(".anterior").exists(),
		jsonPath(".siguiente").doesNotExist()));


    }

    @Test
    void crearEventoTest() {
	EventoDTO eventoDTO = crearEventoValido();
	UsuarioDTO usuarioDTO = crearUsuarioValido();
	// sistemaGestionEventos.registroUsuarios(usuarioDTO);

	// UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(),
	// usuarioDTO.getPassword());

	// ResponseEntity<Void> response = restTemplateEvento.postForEntity("/?id=" +
	// uidUsuario, eventoDTO, Void.class);

	// Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getEventoTest() {
	UsuarioDTO usuarioDTO = crearUsuarioValido();
	// sistemaGestionEventos.registroUsuarios(usuarioDTO);
	// UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(),
	// usuarioDTO.getPassword());

	// usuarioDTO.setUId(uidUsuario);
	EventoDTO eventoDTO = crearEventoValido();
	// eventoDTO.setIdCreador(uidUsuario);

	// sistemaGestionEventos.crearEventoPorUsuario(usuarioDTO, eventoDTO, false);

	// ResponseEntity<EventoDTO> response = restTemplateEvento
	// .getForEntity("/" + eventoDTO.getIdEvento() + "?uid=" + uidUsuario,
	// EventoDTO.class);

	// Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	// Assertions.assertEquals(eventoDTO, response.getBody());
    }
}
