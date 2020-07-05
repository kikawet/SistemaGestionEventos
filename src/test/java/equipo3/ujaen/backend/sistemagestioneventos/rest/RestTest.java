package equipo3.ujaen.backend.sistemagestioneventos.rest;

import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import equipo3.ujaen.backend.sistemagestioneventos.beans.SistemaGestionEventos;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.servidor.ServidorSistemaGestionEventos;

@SpringBootTest(classes = ServidorSistemaGestionEventos.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class RestTest {

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

	@PostConstruct
	void crearRestTemplate() {
		RestTemplateBuilder restTemplateBuilderUsuario = new RestTemplateBuilder()
				.rootUri("http://localhost:12021/sge-api/usuario");
		RestTemplateBuilder restTemplateBuilderEvento = new RestTemplateBuilder()
				.rootUri("http://localhost:12021/sge-api/evento");

		restTemplateUsuario = new TestRestTemplate(restTemplateBuilderUsuario);
		restTemplateEvento = new TestRestTemplate(restTemplateBuilderEvento);
	}

	@Test
	void testPing() {
		ResponseEntity<Void> respuesta = restTemplateUsuario.getForEntity("/ping", Void.class);

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
		sistemaGestionEventos.registroUsuarios(usuarioDTO.getLogin(), usuarioDTO.getPassword());
		UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		ResponseEntity<UsuarioDTO> respuesta = restTemplateUsuario.getForEntity("/" + uidUsuario + "?id=" + uidUsuario,
				UsuarioDTO.class);

		Assertions.assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		Assertions.assertEquals(usuarioDTO.getLogin(), respuesta.getBody().getLogin());
	}

	@Test
	void listarInscritosTest() {

		UsuarioDTO usuarioDTO = crearUsuarioValido();
		sistemaGestionEventos.registroUsuarios(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		UUID uidUsuario = sistemaGestionEventos.loginUsuario(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		ResponseEntity<> respuesta = restTemplateUsuario
				.getForEntity("/" + uidUsuario + "/inscritos?id=" + uidUsuario, Object[].class);

		Assertions.assertTrue(respuesta.getBody().length > 0);
	}
}
