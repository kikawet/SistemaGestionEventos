package equipo3.ujaen.backend.sistemagestioneventos.interfaces;

import java.util.List;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoExiste;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoPrescrito;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.ParametrosInvalidos;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;

public interface InterfaceSistemaGestionEventos {

	/**
	 * @brief Para añadir usuarios al sistema
	 * @param login    id del usuario
	 * @param password contraseña del usuario
	 *
	 * @throws ParametrosInvalidos login o password son null
	 * @throws UsuarioYaRegistrado existe otro usuario con ese login
	 */
	public void registroUsuarios(String login, String password);

	/**
	 * @brief Método que devuelve un usuario al cliente para que este pueda trabajar
	 *        con el. Una vez logeado nunca se cierra sesión
	 * @param login    Es el id de usuario
	 * @param password Es la contraseña del usuario
	 * @return UsuarioDTO devuelve un usuario al cliente
	 *
	 * @throws ParametrosInvalidos login o password son null
	 * @throws UsuarioNoRegistrado no existe ningún usuario con ese login
	 * @throws AccesoDenegado      la contraseña es incorrecta
	 */
	public UsuarioDTO loginUsuario(String login, String password);

	/**
	 * @brief Método que lista los eventos que hay en el sistema
	 *
	 * @param categoria          categoría del evento. Null para nuscar de cualquier
	 *                           categoría
	 * @param descripcionParcial descripción parcial del evento
	 * @param cantidadMaxima     cantidad máxima de resultados devueltos
	 * @return lista de DTO que cumplen las restricicones
	 *
	 * @throws ParametrosInvalidos cantidad <= 0
	 * @throws ParametrosInvalidos descripcionParcial == null
	 */
	public List<EventoDTO> listarEventos(CategoriaEvento categoria, String descripcionParcial, int cantidadMaxima);

	/**
	 * @brief Método para listar los eventos de un usuario, indicando si este está
	 *        aceptado o en lista de espera
	 *
	 * @param usuario usuario del que devolver la lista de eventos inscritos
	 */
	public List<EventoDTO> listarEventosInscritosDeUnUsuario(UsuarioDTO usuario);

	/**
	 * @brief Método para listar los eventos que ha creado un usuario
	 *
	 * @param usuario usuario del que devolver la lista de eventos que ha creado
	 */
	public List<EventoDTO> listarEventosCreadosPorUnUsuario(UsuarioDTO usuario);

	/**
	 * @brief Método para crear un evento por usuario
	 *
	 * @param usuario          usuario que crea el evento
	 * @param evento           datos con los que se va a cear el evento
	 * @param inscribirCreador true para inscribir al creador al evento
	 *
	 * @throws EventoYaRegistrado ya existe ese evento
	 * @throws EventoPrescrito    la fecha del evento es anterior a la de hoy
	 */
	public void crearEventoPorUsuario(UsuarioDTO usuario, EventoDTO evento, boolean inscribirCreador);

	/**
	 * @brief Metodo para borrar un evento
	 *
	 * @param usuario  creador del evento o admin
	 * @param idEvento id del evento que se quiere cancelar
	 *
	 * @throws ParametrosInvalidos idEvento == null
	 * @throws EventoNoExiste      no existe ningun evento con ese id
	 * @throws AccesoDenegado      el usuario no tiene permisos para cancelar el
	 *                             evento
	 */
	public void cancelarEventoPorUsuario(UsuarioDTO usuario, Long idEvento);

	/**
	 * @brief Inscribir un usuario a un evento
	 *
	 * @param usuario  usuario que se va a inscribir
	 * @param idEvento id del evento al que se va a inscribir
	 *
	 * @return EstadoUsuarioEvento.ACEPTADO el usuario se ha inscrito
	 * @return EstadoUsuarioEvento.LISTA_DE_ESPERA el usuario se ha insertado en la
	 *         lista de espera
	 * @return null el usuario ya estaba inscrito
	 *
	 * @throws EventoNoRegistrado no existe ningún evento con ese id
	 * @throws EventoPrescrito    un usuario intenta inscribirse pasada la fecha del
	 *                            evento
	 */
	public EstadoUsuarioEvento inscribirUsuario(UsuarioDTO usuario, Long idEvento);

	/**
	 *
	 * @param usuario  usuario que quiere cancelar la inscripción
	 * @param idEvento id del evento al que se va a inscribir
	 *
	 * @throws EventoNoRegistrado no existe ningún evento con ese id
	 */
	public void cancelarInscripcionUsuario(UsuarioDTO usuario, Long idEvento);

	public UsuarioDTO getUsuario(Long idUsuario);

}
