package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;

//No se necesita acceso a base de datos
@ActiveProfiles("test")
class EventoTestUnitarios {
    private final static String lugar = "Jaén";
    private final static LocalDateTime fecha = LocalDateTime.now();
    private final static EventoDTO.TipoEvento tipoEvento = EventoDTO.TipoEvento.BENEFICO;
    private final static EventoDTO.CategoriaEvento categoriaEvento = EventoDTO.CategoriaEvento.DEPORTE;
    private final static String descripcion = "Evento al que todo el mundo asistirá";
    private final static int aforoMaximo = 1500;
    private final static String titulo = "Evento 1";
    private final static String foto = "https://mymiddlec.files.wordpress.com/2013/09/empty-box.jpg";

    Evento crearEvento() {
	return new Evento(aforoMaximo, descripcion, fecha, lugar, tipoEvento, categoriaEvento, null, titulo, foto);
    }

    Usuario crearUsuario() {
	// Aunque tengan el mismo usuario y contraseña tendrán disinto ID
	Usuario u = new Usuario("login", "password");
	u.setUId(UUID.randomUUID());
	return u;
    }

    /**
     * @brief Comprobamos que tanto el constuctor como los getters devuelven los
     *        datos esperados
     */
    @Test
    void testEventoConstuctorGetters() {

	Evento evento = crearEvento();

	// Datos creados por el constuctor
	assertThat(evento).extracting(Evento::getLugar).isEqualTo(lugar);
	assertThat(evento).extracting(Evento::getFecha).isEqualTo(fecha);
	assertThat(evento).extracting(Evento::getTipoEvento).isEqualTo(tipoEvento);
	assertThat(evento).extracting(Evento::getCategoriaEvento).isEqualTo(categoriaEvento);
	assertThat(evento).extracting(Evento::getDescripcion).isEqualTo(descripcion);
	assertThat(evento).extracting(Evento::getAforoMaximo).isEqualTo(aforoMaximo);

	// Datos creados internamente

	// REPASAR!!!!!!!!
	// assertNotNull(evento.getIdEvento());
	// assertTrue(evento.getIdEvento() != 0);

	assertThat(evento.getAsistentes()).isEmpty();
	assertThat(evento.getListaEspera()).isEmpty();
    }

    @Test
    void testSetters() throws InterruptedException {

	Evento evento = crearEvento();

	String nuevoLugar = "Sevilla";
	TimeUnit.SECONDS.sleep(1);
	LocalDateTime nuevaFecha = LocalDateTime.now();
	EventoDTO.TipoEvento nuevoTipoEvento = EventoDTO.TipoEvento.NO_BENEFICO;
	EventoDTO.CategoriaEvento nuevaCategoriaEventoEvento = EventoDTO.CategoriaEvento.CULTURAL;
	String nuevaDescripcion = "Evento al que nadie en el mundo asistirá";
	int nuevoAforoMaximo = 30;

	evento.setLugar(nuevoLugar);
	evento.setFecha(nuevaFecha);
	evento.setTipoEvento(nuevoTipoEvento);
	evento.setCategoriaEvento(nuevaCategoriaEventoEvento);
	evento.setDescripcion(nuevaDescripcion);
	evento.setAforoMaximo(nuevoAforoMaximo);

	assertThat(evento).extracting(Evento::getLugar).isEqualTo(nuevoLugar);
	assertThat(evento).extracting(Evento::getFecha).isEqualTo(nuevaFecha);
	assertThat(evento).extracting(Evento::getTipoEvento).isEqualTo(nuevoTipoEvento);
	assertThat(evento).extracting(Evento::getCategoriaEvento).isEqualTo(nuevaCategoriaEventoEvento);
	assertThat(evento).extracting(Evento::getDescripcion).isEqualTo(nuevaDescripcion);
	assertThat(evento).extracting(Evento::getAforoMaximo).isEqualTo(nuevoAforoMaximo);

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

	EstadoUsuarioEvento estado = evento.anadirAsistente(usuario);

	assertThat(estado).isEqualTo(EstadoUsuarioEvento.ACEPTADO);
	assertThat(evento.getAsistentes()).hasSize(1);
	assertThat(evento.getListaEspera()).isEmpty();

	estado = evento.anadirAsistente(usuario2);

	assertThat(estado).isEqualTo(EstadoUsuarioEvento.LISTA_DE_ESPERA);
	assertThat(evento.getAsistentes()).hasSize(1);
	assertThat(evento.getListaEspera()).hasSize(1);

	// Si el usuario ya existe devuelve null
	assertThat(evento.anadirAsistente(usuario)).isNull();
	assertThat(evento.anadirAsistente(usuario2)).isNull();

	// No se inserta ningun asistenten nuevo
	assertThat(evento.getAsistentes()).hasSize(1);
	assertThat(evento.getListaEspera()).hasSize(1);
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
	assertThat(evento.getAsistentes()).hasSize(1);
	assertThat(evento.getListaEspera()).isEmpty();
    }

}
