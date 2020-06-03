package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO.RolUsuario;
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
	public UsuarioDTO loginUsuario(String login, String password) {
		// TODO Auto-generated method stub
		Usuario usuario = usuarios.get(login);

		if (usuario == null) {
			throw new UsuarioNoRegistrado();
		}

		if (!usuario.getPassword().equals(password)) {
			throw new AccesoDenegado();
		}

		return usuario.toDTO();
	}

	/**
	 * @brief Método que lista los eventos que hay en el sistema
	 */
	@Override
	public List<EventoDTO> listarEventos(CategoriaEvento categoria, String descripcionParcial, long cantidadMaxima) {
		if (cantidadMaxima < 0)
			throw new IllegalArgumentException("La cantidad máxima no puede ser negativa");

		if (descripcionParcial == null)
			throw new IllegalArgumentException("La descripcion no puede ser null");

		if (categoria != null)
			return eventos.values().parallelStream().filter(evento -> evento.getCategoriaEvento() == categoria)
					.filter(evento -> evento.getDescripcion().toLowerCase().contains(descripcionParcial))
					.limit(cantidadMaxima).map(evento -> evento.toDTO()).collect(Collectors.toList());
		else
			return eventos.values().parallelStream()
					.filter(evento -> evento.getDescripcion().toLowerCase().contains(descripcionParcial))
					.limit(cantidadMaxima).map(evento -> evento.toDTO()).collect(Collectors.toList());

	}

	/**
	 * @brief Método para listar los eventos de un usuario, indicando si este está
	 *        aceptado o en lista de espera
	 */
	@Override
	public List<EventoDTO> listarEventosInscritosDeUnUsuario(UsuarioDTO usuarioDTO) {

		Usuario usuarioValido = validarUsuario(usuarioDTO);

		return usuarioValido.getEventosInscritos().stream().map(evento -> evento.toDTO(usuarioValido))
				.collect(Collectors.toList());
	}

	@Override
	public List<EventoDTO> listarEventosCreadosPorUnUsuario(UsuarioDTO usuarioDTO) {

		Usuario usuarioValido = validarUsuario(usuarioDTO);

		return usuarioValido.getEventosCreados().stream().map(evento -> evento.toDTO(usuarioValido))
				.collect(Collectors.toList());
	}

	/**
	 * @brief Método para crear un evento por usuaio
	 * @param Usuario que recibe del cliente
	 * @param Evento  que recibe del cliente
	 */
	@Override
	public void crearEventoPorUsuario(UsuarioDTO usuarioDTO, EventoDTO eventoDTO, boolean inscribirCreador) {

		// TODO Auto-generated method stub

		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (eventos.containsKey(eventoDTO.getIdEvento()))
			throw new EventoYaRegistrado();

		Evento evento = new Evento(eventoDTO);

		eventoDTO.setIdEvento(evento.getIdEvento());

		usuarioValido.crearEvento(evento);

		
		if (inscribirCreador) {
			Date hoy = new Date();

			// hoy > evento.getFecha
			if (hoy.compareTo(evento.getFecha()) > 0)
				throw new EventoPrescrito();

			usuarioValido.inscribir(evento);
			evento.anadirAsistente(usuarioValido);
		}

		eventos.put(evento.getIdEvento(), evento);

		usuarioDTO = usuarioValido.toDTO();
		usuarioDTO.setPassword(null);
		eventoDTO = evento.toDTO();
	}

	/**
	 * @brief Metodo para cancelar un evento de un usuario
	 */
	@Override
	public void cancelarEventoPorUsuario(UsuarioDTO usuarioDTO, Long idEvento) {

		Evento evento = this.eventos.get(idEvento);
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (evento == null)
			throw new EventoNoExiste();

		int pos = usuarioValido.getEventosCreados().indexOf(evento);

		if (usuarioValido.getRol() != UsuarioDTO.RolUsuario.ADMIN && pos == -1)
			throw new AccesoDenegado();

		usuarioValido.getEventosCreados().remove(pos);
		eventos.remove(idEvento);
	}

	/**
	 * @brief
	 */
	@Override
	public EstadoUsuarioEvento inscribirUsuario(UsuarioDTO usuarioDTO, Long idEvento) {

		Usuario usuarioValido = validarUsuario(usuarioDTO);
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
	public void cancelarInscripcionUsuario(UsuarioDTO usuarioDTO, Long idEvento) {

		// TODO Auto-generated method stub
		Usuario usuarioValido = validarUsuario(usuarioDTO);
		Evento evento = eventos.get(idEvento);

		if (evento == null) {
			throw new EventoNoRegistrado();
		}

		usuarioValido.cancelarInscripcion(evento);
		evento.eliminarAsistente(usuarioValido);
	}

	/**
	 * @brief Metodo para validar un usuario internamente
	 * @param usuario
	 * @return
	 */
	private Usuario validarUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuarioInterno = usuarios.get(usuarioDTO.getLogin());

		if (usuarioInterno == null)
			throw new UsuarioNoRegistrado();

		if (!usuarioInterno.getuId().equals(usuarioDTO.getuId()))
			throw new AccesoDenegado();

		return usuarioInterno;
	}

}
