package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoExiste;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoPrescrito;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Component
public class SistemaGestionEventos implements InterfaceSistemaGestionEventos {

	private Map<String, Usuario> usuarios;
	private Map<Long, Evento> eventos;

	public SistemaGestionEventos() {
		// TODO Auto-generated constructor stub
		usuarios = new HashMap<>();
		eventos = new HashMap<>();
	}

	@Override
	public void registroUsuarios(String login, String password) {

		if (usuarios.containsKey(login)) {
			throw new UsuarioYaRegistrado();
		}

		Usuario usuario = new Usuario(login, password);

		usuarios.put(login, usuario);
	}

	/**
	 * @brief Método que devuelve un usuario al cliente para que este pueda trabajar
	 *        con el
	 * @param login    Es el id de usuario
	 * @param password Es la contraseña del usuario
	 * @return devuelve un usuario al cliente
	 */
	@Override
	public Usuario loginUsuario(String login, String password) {
		// TODO Auto-generated method stub
		Usuario usuario = usuarios.get(login);

		if (usuario == null) {
			throw new UsuarioNoRegistrado();
		}

		if (!usuario.getPassword().equals(password)) {
			throw new AccesoDenegado();
		}

		return usuario;
	}

	/**
	 * @brief Método que lista los eventos que hay en el sistema
	 */
	@Override
	public List<EventoDTO> listarEventos(long desplazamiento, long cantidad) {
		return eventos.values().parallelStream().skip(desplazamiento).limit(cantidad).map(evento -> evento.toDTO())
				.collect(Collectors.toList());
	}

	/**
	 * @brief Método para listar los eventos de un usuario, indicando si este está
	 *        aceptado o en lista de espera
	 */
	@Override
	public List<EventoDTO> listarEventosDeUnUsuario(Usuario usuario) {

		Usuario usuarioValido = validarUsuario(usuario);

		return usuarioValido.getEventosInscritos().stream().map(evento -> evento.toDTO(usuarioValido))
				.collect(Collectors.toList());
	}

	/**
	 * @brief Método para crear un evento por usuaio
	 * @param Usuario que recibe del cliente
	 * @param Evento  que recibe del cliente
	 */
	@Override
	public void crearEventoPorUsuario(Usuario usuario, EventoDTO eventoDTO) {
		// TODO Auto-generated method stub

		Usuario usuarioValido = validarUsuario(usuario);

		if (eventos.containsKey(eventoDTO.getIdEvento()))
			throw new EventoYaRegistrado();

		Evento evento = new Evento(eventoDTO);

		usuarioValido.crearEvento(evento);

		eventos.put(evento.getIdEvento(), evento);
	}

	/**
	 * @brief Metodo para cancelar un evento de un usuario
	 */
	@Override
	public void cancelarEventoPorUsuario(Usuario usuario, Long idEvento) {

		Evento evento = this.eventos.get(idEvento);
		Usuario usuarioValido = validarUsuario(usuario);

		if (evento == null)
			throw new EventoNoExiste();

		int pos = usuarioValido.getEventosCreados().indexOf(evento);

		if (usuarioValido.getRol() != Usuario.RolUsuario.ADMIN && pos == -1)
			throw new AccesoDenegado();

		usuarioValido.getEventosCreados().remove(pos);
		eventos.remove(idEvento);
	}

	/**
	 * @brief
	 */
	@Override
	public EstadoUsuarioEvento inscribirUsuario(Usuario usuario, Long idEvento) {

		Usuario usuarioValido = validarUsuario(usuario);
		Evento evento = eventos.get(idEvento);

		if (evento == null)
			throw new EventoNoRegistrado();

		Date hoy = new Date();

		// hoy > evento.getFecha
		if (hoy.compareTo(evento.getFecha()) > 0)
			throw new EventoPrescrito();

		usuarioValido.inscribir(evento);
		return evento.anadirAsistente(usuarioValido);

	}

	@Override
	public void cancelarInscripcionUsuario(Usuario usuario, Long idEvento) {

		// TODO Auto-generated method stub
		Usuario usuarioValido = validarUsuario(usuario);
		Evento evento = eventos.get(idEvento);

		if (evento == null) {
			throw new EventoNoRegistrado();
		}

		usuario.cancelarInscripcion(evento);
		evento.eliminarAsistente(usuarioValido);
	}

	/**
	 * @brief Metodo para validar un usuario internamente
	 * @param usuario
	 * @return
	 */
	private Usuario validarUsuario(Usuario usuario) {
		Usuario usuarioInterno = usuarios.get(usuario.getLogin());

		if (usuarioInterno == null || !usuarioInterno.mismoUID(usuario))
			throw new AccesoDenegado();

		return usuarioInterno;
	}

}
