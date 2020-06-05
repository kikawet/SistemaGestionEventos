package equipo3.ujaen.backend.sistemagestioneventos.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoExiste;
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

	UsuarioDTO crearUsuarioRegistradoLogeado() {
		Long login = new Random().nextLong();
		String loginUsuario = login.toString();
		String passwordUsuario = "123456789a.";

		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);

		return gestorEventos.loginUsuario(loginUsuario, passwordUsuario);
	}

	EventoDTO crearEventoValido() {
		Date manana = new Date((new Date()).getTime() + (1000 * 60 * 60 * 24));

		int aforoMaximo = 1500;
		String descripcion = "El evento al que todo el mundo vendrá";
		Date cuando = manana;
		Long idEvento = null;
		String lugar = "Jaén";
		EventoDTO.TipoEvento tipoEvento = TipoEvento.NO_BENEFICO;
		EventoDTO.CategoriaEvento categoriaEvento = CategoriaEvento.EXCURSIONES;
		int numAsistentes = 603;
		int numListaEspera = 0;
		EventoDTO.EstadoUsuarioEvento estado = null;

		return new EventoDTO(aforoMaximo, descripcion, cuando, idEvento, lugar, tipoEvento, categoriaEvento,
				numAsistentes, numListaEspera, estado);
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

		UsuarioDTO u = gestorEventos.loginUsuario(loginUsuario, passwordUsuario);

		assertNotNull(u);
		assertEquals(loginUsuario, u.getLogin());
		assertEquals(passwordUsuario, u.getPassword());
	}

	@Test
	void crearEventoPorUsuario() {
		String loginUsuario = "11111111X";
		String passwordUsuario = "123456789a.";

		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);

		UsuarioDTO u = gestorEventos.loginUsuario(loginUsuario, passwordUsuario);

		assertNotNull(u);
		assertEquals(loginUsuario, u.getLogin());
		assertEquals(passwordUsuario, u.getPassword());

		EventoDTO e = crearEventoValido();

		assertNull(e.getIdEvento());

		boolean inscribirCreador = true;

		gestorEventos.crearEventoPorUsuario(u, e, inscribirCreador);

		assertEquals(1, e.getNumAsistentes());

		assertNotNull(e.getIdEvento());

		Assertions.assertThrows(EventoYaRegistrado.class,
				() -> gestorEventos.crearEventoPorUsuario(u, e, inscribirCreador));

		assertNotNull(e);

		List<EventoDTO> eventos = gestorEventos.listarEventos(null, "", 1);

		assertEquals(1, eventos.size());

		gestorEventos.cancelarEventoPorUsuario(u, e.getIdEvento());

	}

	void cancelarInscripcionUsuario() {

		EventoDTO evento = crearEventoValido();

		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();

		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

		boolean inscribirCreador = false;

		gestorEventos.crearEventoPorUsuario(usuario1, evento, inscribirCreador);

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());

		// Usuario resgistrado e inscrito
		gestorEventos.cancelarInscripcionUsuario(usuario, evento.getIdEvento());

		// Usuario resgistrado y no inscrito
		Assertions.assertThrows(UsuarioNoEstaEvento.class,
				() -> gestorEventos.cancelarInscripcionUsuario(usuario, evento.getIdEvento()));

		gestorEventos.cancelarEventoPorUsuario(usuario1, evento.getIdEvento());

	}

	@Test
	void inscribirseUsuarioTest() {

		EventoDTO evento = crearEventoValido();

		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();

		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

		boolean inscribirCreador = false;

		gestorEventos.crearEventoPorUsuario(usuario1, evento, inscribirCreador);

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());

		// Se necesita función para obtener los asistentes a un evento

//		assertEquals(1, evento.getNumAsistentes());

//		assertEquals(usuario, evento.getAsistentes().get(0));

		Assertions.assertThrows(EventoNoRegistrado.class, () -> gestorEventos.inscribirUsuario(usuario, (long) 8));

		gestorEventos.cancelarEventoPorUsuario(usuario1, evento.getIdEvento());
	}

	@Test
	void listarEventosDeUnUsuario() {

		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();
		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

		EventoDTO evento = crearEventoValido();

		evento.setAforoMaximo(1);

		// TEST USUARIO NO LOGEADO //
		{
			UsuarioDTO usuario2 = new UsuarioDTO("PeterParker33", "🕷");
			Assertions.assertThrows(UsuarioNoRegistrado.class,
					() -> gestorEventos.listarEventosInscritosDeUnUsuario(usuario2));
		}

		// TEST USUARIO NO INSCRITO //

		boolean inscribirCreador = false;

		gestorEventos.crearEventoPorUsuario(usuario, evento, inscribirCreador);

		assertTrue(gestorEventos.listarEventosInscritosDeUnUsuario(usuario).isEmpty());
		assertEquals(1, gestorEventos.listarEventosCreadosPorUnUsuario(usuario).size());

		// TEST SUPERANDO AFORO //

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());
		gestorEventos.inscribirUsuario(usuario1, evento.getIdEvento());

		// Eventos inscritos

		List<EventoDTO> eventosU = gestorEventos.listarEventosInscritosDeUnUsuario(usuario);
		List<EventoDTO> eventosU1 = gestorEventos.listarEventosInscritosDeUnUsuario(usuario1);

		assertTrue(eventosU.size() == 1);
		assertEquals(eventosU.get(0).getEstado(), EstadoUsuarioEvento.ACEPTADO);

		assertTrue(eventosU1.size() == 1);
		assertEquals(eventosU1.get(0).getEstado(), EstadoUsuarioEvento.LISTA_DE_ESPERA);

		// Eventos creados

		eventosU = gestorEventos.listarEventosCreadosPorUnUsuario(usuario);
		eventosU1 = gestorEventos.listarEventosCreadosPorUnUsuario(usuario1);

		assertTrue(eventosU.size() == 1);
		assertEquals(eventosU.get(0).getEstado(), EstadoUsuarioEvento.ACEPTADO);

		assertTrue(eventosU1.isEmpty());

		// TEST LIBERANDO AFORO //

		gestorEventos.cancelarInscripcionUsuario(usuario, evento.getIdEvento());

		// Eventos inscritos

		eventosU = gestorEventos.listarEventosInscritosDeUnUsuario(usuario);
		eventosU1 = gestorEventos.listarEventosInscritosDeUnUsuario(usuario1);

		assertTrue(eventosU.isEmpty());

		assertTrue(eventosU1.size() == 1);
		assertEquals(eventosU1.get(0).getEstado(), EstadoUsuarioEvento.ACEPTADO);

		// Eventos creados

		eventosU = gestorEventos.listarEventosCreadosPorUnUsuario(usuario);
		eventosU1 = gestorEventos.listarEventosCreadosPorUnUsuario(usuario1);

		assertTrue(eventosU.size() == 1);
		assertNull(eventosU.get(0).getEstado());

		assertTrue(eventosU1.isEmpty());

		gestorEventos.cancelarEventoPorUsuario(usuario, evento.getIdEvento());

	}

	@Test
	void listarEventos() {

		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();

		List<EventoDTO> borrar = new ArrayList<>();

		// TEST USUARIO NO LOGEADO //
		{
			UsuarioDTO usuario2 = new UsuarioDTO("PeterParker33", "🕷");
			Assertions.assertThrows(UsuarioNoRegistrado.class,
					() -> gestorEventos.listarEventosInscritosDeUnUsuario(usuario2));
		}

		// TEST SIN EVENTOS

		List<EventoDTO> eventos = gestorEventos.listarEventos(null, "", 100);

		assertTrue(eventos.isEmpty());

		// TEST CON TIPO SIN DESCRIPCION

		EventoDTO evento1 = crearEventoValido();
		borrar.add(evento1);

		evento1.setCategoriaEvento(CategoriaEvento.CHARLAS);
		evento1.setDescripcion("Evento de pruebas");

		boolean inscribirCreador = false;

		gestorEventos.crearEventoPorUsuario(usuario, evento1, inscribirCreador);

		eventos = gestorEventos.listarEventos(CategoriaEvento.CHARLAS, "", 100);
		assertEquals(1, eventos.size());

		// TEST CON TIPO CON DESCRIPCION

		EventoDTO evento2 = crearEventoValido();
		borrar.add(evento2);

		evento2.setCategoriaEvento(CategoriaEvento.DEPORTE);
		evento2.setDescripcion("Evento de pruebas de integracion");

		gestorEventos.crearEventoPorUsuario(usuario, evento2, inscribirCreador);

		eventos = gestorEventos.listarEventos(CategoriaEvento.DEPORTE, "integracion", 100);
		assertEquals(1, eventos.size());

		// TEST SIN TIPO SIN DESCRIPCION
		EventoDTO evento = crearEventoValido();
		borrar.add(evento);

		evento.setCategoriaEvento(null);
		evento.setDescripcion("Evento de pruebas");

		gestorEventos.crearEventoPorUsuario(usuario, evento, inscribirCreador);

		eventos = gestorEventos.listarEventos(null, "", 100);
		assertEquals(borrar.size(), eventos.size());

		Assertions.assertThrows(IllegalArgumentException.class, () -> gestorEventos.listarEventos(null, null, 100));

		// TEST CANTIDAD

		eventos = gestorEventos.listarEventos(null, "", 100);
		assertEquals(borrar.size(), eventos.size());

		eventos = gestorEventos.listarEventos(null, "", 1);
		assertEquals(1, eventos.size());

		eventos = gestorEventos.listarEventos(null, "", 0);
		assertEquals(0, eventos.size());

		Assertions.assertThrows(IllegalArgumentException.class, () -> gestorEventos.listarEventos(null, "", -1));

	}

	@Test
	void cancelarEventoPorUsuario() {
		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();
		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

		EventoDTO evento = crearEventoValido();

		// TEST USUARIO NO LOGEADO //

		{
			UsuarioDTO usuario2 = new UsuarioDTO("PeterParker33", "🕷");
			Assertions.assertThrows(UsuarioNoRegistrado.class,
					() -> gestorEventos.cancelarEventoPorUsuario(usuario2, evento.getIdEvento()));
		}

		// TEST EVENTO NO EXISTE //

		Assertions.assertThrows(EventoNoExiste.class,
				() -> gestorEventos.cancelarEventoPorUsuario(usuario, evento.getIdEvento()));

		boolean inscribirCreador = false;

		gestorEventos.crearEventoPorUsuario(usuario, evento, inscribirCreador);
		assertEquals(1, usuario.getNumEventosCreados());

		// TEST BORRAR SIN PERMISO //

		Assertions.assertThrows(AccesoDenegado.class,
				() -> gestorEventos.cancelarEventoPorUsuario(usuario1, evento.getIdEvento()));

		// TEST BORRANDO //

		gestorEventos.cancelarEventoPorUsuario(usuario, evento.getIdEvento());

		assertTrue(!gestorEventos.listarEventos(null, "", 100).contains(evento));
		assertEquals(0, usuario.getNumEventosCreados());

	}

}
