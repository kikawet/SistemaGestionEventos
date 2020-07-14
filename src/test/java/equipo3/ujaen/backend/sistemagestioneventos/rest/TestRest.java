package equipo3.ujaen.backend.sistemagestioneventos.rest;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import equipo3.ujaen.backend.sistemagestioneventos.beans.SistemaGestionEventos;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;

@SpringBootTest(classes = equipo3.ujaen.backend.sistemagestioneventos.ServidorSistemaGestionEventos.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestRest {

	@LocalServerPort
	int localPort;

	@Value("${server.servlet.context-path}${rest.base-path}")
	String rootPath;

	TestRestTemplate restTemplateUsuario;
	TestRestTemplate restTemplateEvento;

	@Autowired
	SistemaGestionEventos sistemaGestionEventos;

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
		Long idEvento = null;
		String lugar = "Jaén";
		EventoDTO.TipoEvento tipoEvento = TipoEvento.NO_BENEFICO;
		EventoDTO.CategoriaEvento categoriaEvento = CategoriaEvento.EXCURSIONES;
		int numAsistentes = 603;
		int numListaEspera = 0;
		EstadoUsuarioEvento estado = null;
		UUID idCreador = null;

		return new EventoDTO(idEvento, aforoMaximo, descripcion, fecha, lugar, tipoEvento, categoriaEvento, idCreador,
				numAsistentes, numListaEspera, estado);
	}

	@PostConstruct
	void crearRestTemplate() {
		RestTemplateBuilder restTemplateBuilderUsuario = new RestTemplateBuilder()
				.rootUri("http://localhost:" + localPort + rootPath + "/usuario");
		RestTemplateBuilder restTemplateBuilderEvento = new RestTemplateBuilder()
				.rootUri("http://localhost:" + localPort + rootPath + "/evento");

		restTemplateUsuario = new TestRestTemplate(restTemplateBuilderUsuario);
		restTemplateEvento = new TestRestTemplate(restTemplateBuilderEvento);
	}

	@Test
	void testPing() {
		ResponseEntity<Void> respuesta = restTemplateEvento.getForEntity("/ping", Void.class);

		Assertions.assertEquals(HttpStatus.OK, respuesta.getStatusCode());
	}

	@Test
	void registroYLoginUsuario() {
		UsuarioDTO usuarioDTO = crearUsuarioValido();

		ResponseEntity<Void> respuesta = restTemplateUsuario.postForEntity("/registro", usuarioDTO, Void.class);

		Assertions.assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

		ResponseEntity<UUID> esperado = restTemplateUsuario.postForEntity("/login", usuarioDTO, UUID.class);

		Assertions.assertEquals(sistemaGestionEventos.getUsuario(esperado.getBody()), usuarioDTO);
	}

	@Test
	void getUsuarioTest() {

		UsuarioDTO usuarioDTO = crearUsuarioValido();
		sistemaGestionEventos.registroUsuarios(usuarioDTO);
		UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		ResponseEntity<UsuarioDTO> respuesta = restTemplateUsuario.getForEntity("/" + uidUsuario + "?id=" + uidUsuario,
				UsuarioDTO.class);

		Assertions.assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		Assertions.assertEquals(usuarioDTO.getLogin(), respuesta.getBody().getLogin());
	}

	@Test
	void listarInscritosTest() {

		UsuarioDTO usuarioDTO = crearUsuarioValido();
		sistemaGestionEventos.registroUsuarios(usuarioDTO);

		UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		// ResponseEntity<> respuesta = restTemplateUsuario
		// .getForEntity("/" + uidUsuario + "/inscritos?id=" + uidUsuario,
		// Object[].class);

		// Assertions.assertTrue(respuesta.getBody().length > 0);
	}

	@Test
	void crearEventoTest() {
		EventoDTO eventoDTO = crearEventoValido();
		UsuarioDTO usuarioDTO = crearUsuarioValido();
		sistemaGestionEventos.registroUsuarios(usuarioDTO);

		UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		ResponseEntity<Void> response = restTemplateEvento.postForEntity("/?id=" + uidUsuario, eventoDTO, Void.class);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void getEventoTest() {
		UsuarioDTO usuarioDTO = crearUsuarioValido();
		sistemaGestionEventos.registroUsuarios(usuarioDTO);
		UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		usuarioDTO.setUId(uidUsuario);
		EventoDTO eventoDTO = crearEventoValido();
		eventoDTO.setIdCreador(uidUsuario);

		sistemaGestionEventos.crearEventoPorUsuario(usuarioDTO, eventoDTO, false);

		ResponseEntity<EventoDTO> response = restTemplateEvento
				.getForEntity("/" + eventoDTO.getIdEvento() + "?uid=" + uidUsuario, EventoDTO.class);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(eventoDTO, response.getBody());
	}
}
