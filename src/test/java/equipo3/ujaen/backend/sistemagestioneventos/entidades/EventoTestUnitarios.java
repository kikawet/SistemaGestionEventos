package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.Categoria;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.TipoEvento;

class EventoTestUnitarios {
	private final static String lugar = "Jaén";
	private final static Date fecha = new Date();
	private final static TipoEvento tipoEvento = TipoEvento.BENEFICO;
	private final static Categoria categoriaEvento = Categoria.DEPORTE;
	private final static String descripcion = "Evento al que todo el mundo asistirá";
	private final static int aforoMaximo = 1500;

	Evento crearEvento() {
		return new Evento(lugar, fecha, tipoEvento, categoriaEvento, descripcion, aforoMaximo);
	}

	Usuario crearUsuario() {
		// Aunque tengan el mismo usuario y contraseña tendrán disinto ID
		return new Usuario("login", "password");
	}

	/**
	 * @brief Comprobamos que tanto el constuctor como los getters devuelven los
	 *        datos esperados
	 */
	@Test
	void testEventoConstuctorGetters() {

		Evento evento = crearEvento();

		// Datos creados por el constuctor
		assertTrue(evento.getLugar().equals(lugar));
		assertTrue(evento.getFecha().equals(fecha));
		assertTrue(evento.getTipoEvento().equals(tipoEvento));
		assertTrue(evento.getCategoria().equals(categoriaEvento));
		assertTrue(evento.getDescripcion().equals(descripcion));
		assertTrue(evento.getAforoMaximo() == aforoMaximo);

		// Datos creados internamente
		assertTrue(evento.getIdEvento() != null && evento.getIdEvento() > 0);
		assertTrue(evento.getAsistentes().isEmpty());
		assertTrue(evento.getListaEspera().isEmpty());
	}

	@Test
	void testSetters() throws InterruptedException {

		Evento evento = crearEvento();

		String nuevoLugar = "Sevilla";
		TimeUnit.SECONDS.sleep(1);
		Date nuevaFecha = new Date();
		TipoEvento nuevoTipoEvento = TipoEvento.NO_BENEFICO;
		Categoria nuevaCategoriaEvento = Categoria.CULTURAL;
		String nuevaDescripcion = "Evento al que nadie en el mundo asistirá";
		int nuevoAforoMaximo = 30;

		evento.setLugar(nuevoLugar);
		evento.setFecha(nuevaFecha);
		evento.setTipoEvento(nuevoTipoEvento);
		evento.setCategoria(nuevaCategoriaEvento);
		evento.setDescripcion(nuevaDescripcion);
		evento.setAforoMaximo(nuevoAforoMaximo);

		assertTrue(evento.getLugar().equals(nuevoLugar));
		assertTrue(evento.getFecha().equals(nuevaFecha));
		assertTrue(evento.getTipoEvento().equals(nuevoTipoEvento));
		assertTrue(evento.getCategoria().equals(nuevaCategoriaEvento));
		assertTrue(evento.getDescripcion().equals(nuevaDescripcion));
		assertTrue(evento.getAforoMaximo() == nuevoAforoMaximo);

		Usuario usuario = crearUsuario();
		Usuario usuario2 = crearUsuario();

		evento.setAforoMaximo(2);

		evento.anadirAsistente(usuario);
		evento.anadirAsistente(usuario2);

		Assertions.assertThrows(InvalidParameterException.class, () -> evento.setAforoMaximo(1));
	}

	@Test
	void testAnadirAsistente() {
		Evento evento = crearEvento();

		Usuario usuario = crearUsuario();
		Usuario usuario2 = crearUsuario();

		evento.setAforoMaximo(1);

		EstadoEvento estado = evento.anadirAsistente(usuario);

		assertTrue(estado.equals(EstadoEvento.ACEPTADO));
		assertTrue(evento.getAsistentes().size() == 1 && evento.getListaEspera().isEmpty());

		estado = evento.anadirAsistente(usuario2);

		assertTrue(estado.equals(EstadoEvento.LISTA_DE_ESPERA));
		assertTrue(evento.getAsistentes().size() == 1 && evento.getListaEspera().size() == 1);

		// Si el usuario ya existe devuelve null
		assertNull(evento.anadirAsistente(usuario));
		assertNull(evento.anadirAsistente(usuario2));

		// No se inserta ningun asistenten nuevo
		assertTrue(evento.getAsistentes().size() == 1 && evento.getListaEspera().size() == 1);
	}

	@Test
	void testEliminarAsistente() {

		Evento evento = crearEvento();

		Usuario usuario = crearUsuario();
		Usuario usuario2 = crearUsuario();

		evento.setAforoMaximo(1);

		evento.anadirAsistente(usuario);
		evento.anadirAsistente(usuario2);

		evento.eliminarAsistente(usuario);

		// Al borrar el asistente inscrito el de la lista de espera se mueve
		assertTrue(evento.getAsistentes().size() == 1 && evento.getListaEspera().isEmpty());

	}

}
