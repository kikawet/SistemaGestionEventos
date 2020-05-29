package equipo3.ujaen.backend.sistemagestioneventos.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.Categoria;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoEstaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@SpringBootTest(classes = { SistemaGestionEventos.class })
public class SistemaGestionEventosIntegrationTest {

	@Autowired
	InterfaceSistemaGestionEventos gestorEventos;

	Usuario crearUsuarioRegistradoLogeado() {
		Long login = new Random().nextLong();
		String loginUsuario = login.toString();
		String passwordUsuario = "123456789a.";

		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);

		return gestorEventos.loginUsuario(loginUsuario, passwordUsuario);
	}

	@Test
	void loginUsuarioTest() {

		String loginUsuario = "21025923J";
		String passwordUsuario = "123456789a.";

		Assertions.assertThrows(UsuarioNoRegistrado.class,
				() -> gestorEventos.loginUsuario(loginUsuario, passwordUsuario));

		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);

		Assertions.assertThrows(UsuarioYaRegistrado.class,
				() -> gestorEventos.registroUsuarios(loginUsuario, passwordUsuario));

		Usuario u = gestorEventos.loginUsuario(loginUsuario, passwordUsuario);

		assertNotNull(u);
		assertEquals(loginUsuario, u.getLogin());
		assertEquals(passwordUsuario, u.getPassword());
	}

	@Test
	void crearEventoPorUsuario() {
		String loginUsuario = "11111111X";
		String passwordUsuario = "123456789a.";

		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);

		Usuario u = gestorEventos.loginUsuario(loginUsuario, passwordUsuario);

		assertNotNull(u);
		assertEquals(loginUsuario, u.getLogin());
		assertEquals(passwordUsuario, u.getPassword());

		Evento e = new Evento("Jaén", Date.from(Instant.now()), TipoEvento.BENEFICO, Categoria.CHARLAS, "Hola caracola",
				30);

		gestorEventos.crearEventoPorUsuario(u, e);

		Assertions.assertThrows(EventoYaRegistrado.class, () -> gestorEventos.crearEventoPorUsuario(u, e));

		assertNotNull(e);

		List<Evento> eventos = gestorEventos.listarEventos(0, 1);

		assertEquals(1, eventos.size());

	}

	@Test
	void cancelarInscripcionUsuario() {

		Date mañana = new Date((new Date()).getTime() + (1000 * 60 * 60 * 24));

		Evento evento = new Evento("Lugar Evento", mañana, Evento.TipoEvento.NO_BENEFICO, Evento.Categoria.REUNIONES,
				"Descripción evento", 20);

		Usuario usuario = crearUsuarioRegistradoLogeado();

		Usuario usuario1 = crearUsuarioRegistradoLogeado();

		gestorEventos.crearEventoPorUsuario(usuario1, evento);

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());

		// Usuario resgistrado e inscrito
		gestorEventos.cancelarInscripcionUsuario(usuario, evento.getIdEvento());

		// Usuario resgistrado y no inscrito
		Assertions.assertThrows(UsuarioNoEstaEvento.class,
				() -> gestorEventos.cancelarInscripcionUsuario(usuario, evento.getIdEvento()));

	}

	@Test
	void inscribirseUsuarioTest() {

		Date mañana = new Date((new Date()).getTime() + (1000 * 60 * 60 * 24));

		Evento evento = new Evento("Lugar Evento", mañana, Evento.TipoEvento.NO_BENEFICO, Evento.Categoria.REUNIONES,
				"Descripción evento", 20);

		Usuario usuario = crearUsuarioRegistradoLogeado();

		Usuario usuario1 = crearUsuarioRegistradoLogeado();

		gestorEventos.crearEventoPorUsuario(usuario1, evento);

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());

		assertEquals(1, evento.getAsistentes().size());

		assertEquals(usuario, evento.getAsistentes().get(0));

		Assertions.assertThrows(EventoNoRegistrado.class, () -> gestorEventos.inscribirUsuario(usuario, (long) 8));
	}
}
