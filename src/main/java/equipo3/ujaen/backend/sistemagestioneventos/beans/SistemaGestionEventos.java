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
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.ParametrosInvalidos;
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
			throw new ParametrosInvalidos("Ni el login ni la contraseña pueden ser null");

		if (usuarioDAO.existsByLogin(login)) {
			throw new UsuarioYaRegistrado();
		}

		Usuario usuario = new Usuario(login, password);

		usuarioDAO.save(usuario);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public UsuarioDTO loginUsuario(String login, String password) {
		if (login == null || password == null)
			throw new ParametrosInvalidos("Ni el login ni la contraseña pueden ser null");

		Usuario usuario = usuarioDAO.findByLogin(login);

		if (usuario == null) {
			throw new UsuarioNoRegistrado();
		}

		if (!usuario.getPassword().equals(password)) {
			throw new AccesoDenegado();
		}

		return usuario.toDTO();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventoDTO> listarEventos(CategoriaEvento categoria, String descripcionParcial, int pagina,
			int cantidad) {
		if (pagina < 0)
			throw new ParametrosInvalidos("La pagina no puede ser negativa");

		if (cantidad <= 0)
			throw new ParametrosInvalidos("La cantidad no puede ser <= 0");

		if (descripcionParcial == null)
			throw new ParametrosInvalidos("La descripcion no puede ser null");

		List<Evento> resultado = null;

		if (categoria != null)
			resultado = eventoDAO.findByCategoriaEventoAndDescripcionContainsIgnoreCase(categoria, descripcionParcial,
					PageRequest.of(pagina, cantidad));
		else
			resultado = eventoDAO.findByDescripcionContainsIgnoreCase(descripcionParcial,
					PageRequest.of(pagina, cantidad));

		if (resultado == null)
			return new ArrayList<>();
		else
			return resultado.parallelStream().map(evento -> evento.toDTO()).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventoDTO> listarEventosInscritosDeUnUsuario(UsuarioDTO usuarioDTO, int pagina, int cantidad) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);
		if (pagina < 0)
			throw new ParametrosInvalidos("La pagina no puede ser negativa");

		if (cantidad <= 0)
			throw new ParametrosInvalidos("La cantidad no puede ser <= 0");

		Usuario u = usuarioDAO.findByLoginFetchingInscritos(usuarioValido.getLogin(), PageRequest.of(pagina, cantidad));

		return u == null ? new ArrayList<>()
				: u.getEventosInscritos().stream().map(evento -> evento.toDTO(usuarioValido))
						.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventoDTO> listarEventosCreadosPorUnUsuario(UsuarioDTO usuarioDTO, int pagina, int cantidad) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);
		if (pagina < 0)
			throw new ParametrosInvalidos("La pagina no puede ser negativa");

		if (cantidad <= 0)
			throw new ParametrosInvalidos("La cantidad no puede ser <= 0");

		Usuario u = usuarioDAO.findByLoginFetchingCreados(usuarioValido.getLogin(), PageRequest.of(pagina, cantidad));

		return u == null ? new ArrayList<EventoDTO>()
				: u.getEventosCreados().stream().map(evento -> evento.toDTO(usuarioValido))
						.collect(Collectors.toList());
	}

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

		eventoDAO.saveAndFlush(evento);

		usuarioDTO.clone(usuarioValido.toDTO());
		usuarioDTO.setPassword(null);

		eventoDTO.clone(evento.toDTO());
	}

	@Override
	@Transactional
	public void cancelarEventoPorUsuario(UsuarioDTO usuarioDTO, Long idEvento) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (idEvento == null)
			throw new ParametrosInvalidos("idEvento no puede ser null");

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

		usuarioDTO.clone(usuarioValido.toDTO());
	}

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
	}

	/**
	 * @brief Metodo para validar un usuario internamente
	 * @param usuario
	 * @return usuario enlazado de la base de datos
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
