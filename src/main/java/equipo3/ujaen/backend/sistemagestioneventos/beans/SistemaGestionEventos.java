package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import equipo3.ujaen.backend.sistemagestioneventos.dao.EventoDao;
import equipo3.ujaen.backend.sistemagestioneventos.dao.UsuarioDao;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
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

@Repository
public class SistemaGestionEventos implements InterfaceSistemaGestionEventos {

	@Autowired
	private UsuarioDao usuarioDAO;

	@Autowired
	private EventoDao eventoDAO;

	public SistemaGestionEventos() {
	}

	@Override
	public void registroUsuarios(String login, String password) {
		if (login == null || password == null)
			throw new IllegalArgumentException("Ni el login ni la contraseña pueden ser null");

		if (usuarioDAO.existsByLogin(login)) {
			throw new UsuarioYaRegistrado();
		}

		Usuario usuario = new Usuario(login, password);

		usuarioDAO.save(usuario);
	}

	/**
	 * @brief Método que devuelve un usuario al cliente para que este pueda trabajar
	 *        con el
	 * @param login    Es el id de usuario
	 * @param password Es la contraseña del usuario
	 * @return devuelve un usuario al cliente
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public UsuarioDTO loginUsuario(String login, String password) {
		if (login == null || password == null)
			throw new IllegalArgumentException("Ni el login ni la contraseña pueden ser null");

		Usuario usuario = usuarioDAO.findByLogin(login);

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<EventoDTO> listarEventos(CategoriaEvento categoria, String descripcionParcial, int cantidadMaxima) {
		if (cantidadMaxima < 0)
			throw new IllegalArgumentException("La cantidad máxima no puede ser negativa");

		if (descripcionParcial == null)
			throw new IllegalArgumentException("La descripcion no puede ser null");

		if (categoria != null)
			return eventoDAO
					.findByCategoriaEventoAndDescripcionContainsIgnoreCase(categoria, descripcionParcial,
							PageRequest.of(0, cantidadMaxima))
					.parallelStream().map(evento -> evento.toDTO()).collect(Collectors.toList());
		else
			return eventoDAO.findByDescripcionContainsIgnoreCase(descripcionParcial, PageRequest.of(0, cantidadMaxima))
					.parallelStream().map(evento -> evento.toDTO()).collect(Collectors.toList());
	}

	/**
	 * @brief Método para listar los eventos de un usuario, indicando si este está
	 *        aceptado o en lista de espera
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<EventoDTO> listarEventosInscritosDeUnUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		Usuario u = usuarioDAO.findByLoginFetchingInscritos(usuarioValido.getLogin());

		return u == null ? new ArrayList<>()
				: u.getEventosInscritos().stream().map(evento -> evento.toDTO(usuarioValido))
						.collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<EventoDTO> listarEventosCreadosPorUnUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		Usuario u = usuarioDAO.findByLoginFetchingCreados(usuarioValido.getLogin());

		return u == null ? new ArrayList<EventoDTO>()
				: u.getEventosCreados().stream().map(evento -> evento.toDTO(usuarioValido))
						.collect(Collectors.toList());
	}

	/**
	 * @brief Método para crear un evento por usuaio
	 * @param Usuario que recibe del cliente
	 * @param Evento  que recibe del cliente
	 */
	@Override
	@Transactional
	public void crearEventoPorUsuario(UsuarioDTO usuarioDTO, EventoDTO eventoDTO, boolean inscribirCreador) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (eventoDTO.getIdEvento() != null && eventoDAO.existsById(eventoDTO.getIdEvento()))
			throw new EventoYaRegistrado();

		Evento evento = new Evento(eventoDTO);

		evento.setCreador(usuarioValido);

		if (inscribirCreador) {
			LocalDateTime hoy = LocalDateTime.now();

			// hoy > evento.getFecha
			if (hoy.compareTo(evento.getFecha()) > 0)
				throw new EventoPrescrito();

			usuarioValido.inscribir(evento);
			evento.anadirAsistente(usuarioValido);
		}

		evento = eventoDAO.saveAndFlush(evento);
		usuarioValido = usuarioDAO.save(usuarioValido);

		usuarioDTO.clone(usuarioValido.toDTO());
		usuarioDTO.setPassword(null);

		eventoDTO.clone(evento.toDTO());
		
		
	}

	/**
	 * @brief Metodo para cancelar un evento de un usuario
	 */
	@Override
	@Transactional
	public void cancelarEventoPorUsuario(UsuarioDTO usuarioDTO, Long idEvento) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (idEvento == null)
			throw new IllegalArgumentException("idEvento no puede ser null");

		Evento evento = eventoDAO.findById(idEvento).orElse(null);

		if (evento == null)
			throw new EventoNoExiste();

		boolean contiene = usuarioValido.getEventosCreados().contains(evento);

		if (usuarioValido.getRol() != UsuarioDTO.RolUsuario.ADMIN && !contiene)
			throw new AccesoDenegado();

		Evento cancelarEvento = eventoDAO.findByIdEventoFetchingAsistentes(evento.getIdEvento());
		if (cancelarEvento != null)
			cancelarEvento.getAsistentes().stream().forEach(usuario -> usuario.cancelarInscripcion(evento));

		cancelarEvento = eventoDAO.findByIdEventoFetchingListaEspera(evento.getIdEvento());
		if (cancelarEvento != null)
			cancelarEvento.getListaEspera().stream().forEach(usuario -> usuario.cancelarInscripcion(evento));

		eventoDAO.deleteById(evento.getIdEvento());
		eventoDAO.flush();
		usuarioValido = usuarioDAO.save(usuarioValido);

		usuarioDTO.clone(usuarioValido.toDTO());
	}

	/**
	 * @brief
	 */
	@Override
	@Transactional
	public EstadoUsuarioEvento inscribirUsuario(UsuarioDTO usuarioDTO, Long idEvento) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);
		Evento evento = eventoDAO.findById(idEvento).orElse(null);

		if (evento == null)
			throw new EventoNoRegistrado();

		LocalDateTime hoy = LocalDateTime.now();

		// hoy > evento.getFecha
		if (hoy.compareTo(evento.getFecha()) > 0)
			throw new EventoPrescrito();

		usuarioValido.inscribir(evento);
		EstadoUsuarioEvento estado = evento.anadirAsistente(usuarioValido);

		usuarioValido = usuarioDAO.save(usuarioValido);
		evento = eventoDAO.save(evento);

		usuarioDTO.clone(usuarioValido.toDTO());

		return estado;

	}

	@Override
	@Transactional
	public void cancelarInscripcionUsuario(UsuarioDTO usuarioDTO, Long idEvento) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);
		Evento evento = eventoDAO.findById(idEvento).get();

		if (evento == null) {
			throw new EventoNoRegistrado();
		}

		usuarioValido.cancelarInscripcion(evento);
		evento.eliminarAsistente(usuarioValido);

		usuarioDAO.save(usuarioValido);
		eventoDAO.save(evento);
	}

	/**
	 * @brief Metodo para validar un usuario internamente
	 * @param usuario
	 * @return
	 */
	private Usuario validarUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuarioInterno = usuarioDAO.findByLogin(usuarioDTO.getLogin());

		if (usuarioInterno == null)
			throw new UsuarioNoRegistrado();

		if (usuarioInterno.getuId() != usuarioDTO.getuId())
			throw new AccesoDenegado();

		return usuarioInterno;
	}

}
