package equipo3.ujaen.backend.sistemagestioneventos.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
		Date mañana = new Date((new Date()).getTime() + (1000 * 60 * 60 * 24));

		int aforoMaximo = 1500;
		String descripcion = "El evento al que todo el mundo vendrá";
		Date cuando = mañana;
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

		gestorEventos.crearEventoPorUsuario(u, e);

		Assertions.assertThrows(EventoYaRegistrado.class, () -> gestorEventos.crearEventoPorUsuario(u, e));

		assertNotNull(e);

		List<EventoDTO> eventos = gestorEventos.listarEventos(0, 1);

		assertEquals(1, eventos.size());

	}

	@Test
	void cancelarInscripcionUsuario() {

		EventoDTO evento = crearEventoValido();

		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();

		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

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

		EventoDTO evento = crearEventoValido();

		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();

		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

		gestorEventos.crearEventoPorUsuario(usuario1, evento);

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());

		// Se necesita función para obtener los asistentes a un evento

//		assertEquals(1, evento.getNumAsistentes());

//		assertEquals(usuario, evento.getAsistentes().get(0));

		Assertions.assertThrows(EventoNoRegistrado.class, () -> gestorEventos.inscribirUsuario(usuario, (long) 8));
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
			Assertions.assertThrows(AccesoDenegado.class, () -> gestorEventos.listarEventosDeUnUsuario(usuario2));
		}

		// TEST USUARIO NO INSCRITO //

		gestorEventos.crearEventoPorUsuario(usuario, evento);

		assertTrue(gestorEventos.listarEventosDeUnUsuario(usuario).isEmpty());

		// TEST SUPERANDO AFORO //

		gestorEventos.inscribirUsuario(usuario, evento.getIdEvento());
		gestorEventos.inscribirUsuario(usuario1, evento.getIdEvento());

		List<EventoDTO> eventosU = gestorEventos.listarEventosDeUnUsuario(usuario);
		List<EventoDTO> eventosU1 = gestorEventos.listarEventosDeUnUsuario(usuario1);

		assertTrue(eventosU.size() == 1);
		assertEquals(eventosU.get(0).getEstado(), EstadoUsuarioEvento.ACEPTADO);

		assertTrue(eventosU1.size() == 1);
		assertEquals(eventosU1.get(0).getEstado(), EstadoUsuarioEvento.LISTA_DE_ESPERA);

		// TEST LIBERANDO AFORO //

		gestorEventos.cancelarInscripcionUsuario(usuario, evento.getIdEvento());

		eventosU1 = gestorEventos.listarEventosDeUnUsuario(usuario1);

		assertTrue(eventosU1.size() == 1);
		assertEquals(eventosU1.get(0).getEstado(), EstadoUsuarioEvento.ACEPTADO);

	}

	@Test
	void cancelarEventoPorUsuario() {
		UsuarioDTO usuario = crearUsuarioRegistradoLogeado();
		UsuarioDTO usuario1 = crearUsuarioRegistradoLogeado();

		EventoDTO evento = crearEventoValido();

		// TEST USUARIO NO LOGEADO //

		{
			UsuarioDTO usuario2 = new UsuarioDTO("PeterParker33", "🕷");
			Assertions.assertThrows(AccesoDenegado.class,
					() -> gestorEventos.cancelarEventoPorUsuario(usuario2, evento.getIdEvento()));
		}

		// TEST EVENTO NO EXISTE //

		Assertions.assertThrows(EventoNoExiste.class,
				() -> gestorEventos.cancelarEventoPorUsuario(usuario, evento.getIdEvento()));

		gestorEventos.crearEventoPorUsuario(usuario, evento);
		assertEquals(0, usuario.getNumEventosCreados());

		// TEST BORRAR SIN PERMISO //

		Assertions.assertThrows(AccesoDenegado.class,
				() -> gestorEventos.cancelarEventoPorUsuario(usuario1, evento.getIdEvento()));

		// TEST BORRANDO //

		gestorEventos.cancelarEventoPorUsuario(usuario, evento.getIdEvento());

		assertTrue(!gestorEventos.listarEventos(0, 100).contains(evento));
		assertEquals(0, usuario.getNumEventosCreados());

	}

}
