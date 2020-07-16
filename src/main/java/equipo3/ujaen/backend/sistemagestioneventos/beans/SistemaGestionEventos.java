package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	private static final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	public SistemaGestionEventos() {
	}

	@Override
	public void registroUsuarios(UsuarioDTO usuario) {

		if (usuario.getLogin() == null || usuario.getLogin().length() == 0 || usuario.getPassword() == null
				|| usuario.getPassword().length() == 0)
			throw new ParametrosInvalidos("Ni el login ni la contraseña pueden estar vacios");

		if (usuarioDAO.existsByLogin(usuario.getLogin())) {
			throw new UsuarioYaRegistrado();
		}

		Usuario newUsuario = new Usuario(usuario);

		newUsuario.setPassword(encoder.encode(newUsuario.getPassword()));

		usuarioDAO.save(newUsuario);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public UUID loginUsuario(String login, String password) {
		if (login == null || password == null)
			throw new ParametrosInvalidos("Ni el login ni la contraseña pueden ser null");

		Usuario usuario = usuarioDAO.findByLogin(login);

		if (usuario == null) {
			throw new UsuarioNoRegistrado();
		}

		if (!encoder.matches(password, usuario.getPassword())) {
			throw new AccesoDenegado("Contraseña incorrecta");
		}

		return usuario.getUId();
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

		int fromIndex = Math.min(usuarioValido.getEventosInscritos().size(), pagina * cantidad);
		int toIndex = Math.min(usuarioValido.getEventosInscritos().size(), (pagina + 1) * cantidad);

		List<Evento> eventos = usuarioValido.getEventosInscritos().subList(fromIndex, toIndex);

		return eventos.stream().map(evento -> evento.toDTO(usuarioValido)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventoDTO> listarEventosCreadosPorUnUsuario(UsuarioDTO usuarioDTO, int pagina, int cantidad) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);
		if (pagina < 0)
			throw new ParametrosInvalidos("La pagina no puede ser negativa");

		if (cantidad <= 0)
			throw new ParametrosInvalidos("La cantidad no puede ser <= 0");

		List<Evento> e = usuarioValido.getEventosCreados();

		int fromIndex = Math.min(usuarioValido.getEventosCreados().size(), pagina * cantidad);
		int toIndex = Math.min(usuarioValido.getEventosCreados().size(), (pagina + 1) * cantidad);

		List<Evento> eventos = usuarioValido.getEventosCreados().subList(fromIndex, toIndex);

		return eventos.stream().map(evento -> evento.toDTO(usuarioValido)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void crearEventoPorUsuario(UsuarioDTO usuarioDTO, EventoDTO eventoDTO, boolean inscribirCreador) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (eventoDTO.getIdEvento() != null && eventoDAO.existsById(eventoDTO.getIdEvento()))
			throw new EventoYaRegistrado();

		Evento evento = new Evento(eventoDTO, usuarioValido);

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
		eventoDTO.clone(evento.toDTO());

	}

	@Override
	@Transactional
	public void cancelarEventoPorUsuario(UsuarioDTO usuarioDTO, Long idEvento) {
		Usuario usuarioValido = validarUsuario(usuarioDTO);

		if (idEvento == null)
			throw new ParametrosInvalidos("idEvento no puede ser null");

		Evento evento = eventoDAO.findById(idEvento).orElseThrow(EventoNoExiste::new);

		boolean contiene = usuarioValido.getEventosCreados().contains(evento);

		if (usuarioValido.getRol() != UsuarioDTO.RolUsuario.ADMIN && !contiene)
			throw new AccesoDenegado("No es administrador ni el creador");

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
		Evento evento = eventoDAO.findById(idEvento).orElseThrow(EventoNoRegistrado::new);

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
		Evento evento = eventoDAO.findById(idEvento).orElseThrow(EventoNoRegistrado::new);

		usuarioValido.cancelarInscripcion(evento);
		evento.eliminarAsistente(usuarioValido);
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO getUsuario(UUID idUsuario) {
		return usuarioDAO.findById(idUsuario).orElseThrow(() -> new AccesoDenegado("id de usuario inexistente"))
				.toDTO();
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO getUsuarioLogin(String login) {
		return usuarioDAO.findByLogin(login).toDTO();
	}

	@Override
	@Transactional(readOnly = true)
	public EventoDTO getEvento(long idEvento) {
		return eventoDAO.findById(idEvento).orElseThrow(EventoNoExiste::new).toDTO();
	}

	@Override
	@Transactional(readOnly = true)
	public EstadoUsuarioEvento getEstadoUsuarioEvento(UUID idUsuario, long idEvento) {

		Usuario u = usuarioDAO.findById(idUsuario).orElseThrow(() -> new AccesoDenegado("id de usuario inexistente"));
		Evento e = eventoDAO.findById(idEvento).orElseThrow(EventoNoExiste::new);

		return e.getEstadoUsuario(u);
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

		if (!usuarioInterno.getUId().equals(usuarioDTO.getUId()))
			throw new AccesoDenegado("id de usuario incorrecto");

		return usuarioInterno;
	}

	private Usuario validarUsuarioId(UUID idUsuario) {

		return usuarioDAO.findById(idUsuario).orElseThrow(() -> new AccesoDenegado("id de usuario inexistente"));

	}

	@Override
	@Transactional(readOnly = true)
	public List<EventoDTO> listarEventosUsuario(UUID idUsuario, EstadoUsuarioEvento eue, int pagina, int cantidad) {
		Usuario usuarioValido = validarUsuarioId(idUsuario);
		List<Evento> result = new ArrayList<Evento>();

		if (pagina < 0)
			throw new ParametrosInvalidos("La pagina no puede ser negativa");

		if (cantidad <= 0)
			throw new ParametrosInvalidos("La cantidad no puede ser <= 0");

		if (eue == null) {

			int fromIndex = Math.min(usuarioValido.getEventosInscritos().size(), pagina * cantidad);
			int toIndex = Math.min(usuarioValido.getEventosInscritos().size(), (pagina + 1) * cantidad);

			result = usuarioValido.getEventosInscritos().subList(fromIndex, toIndex);

		} else if (eue.equals(EstadoUsuarioEvento.LISTA_DE_ESPERA)) {
			result = eventoDAO.findByIdUsuarioWhereUsuarioIsEsperando(usuarioValido, PageRequest.of(pagina, cantidad));
		} else if (eue.equals(EstadoUsuarioEvento.ACEPTADO)) {
			result = eventoDAO.findByIdUsuarioWhereUsuarioIsInscrito(usuarioValido, PageRequest.of(pagina, cantidad));
		}
		return result.stream().map(evento -> evento.toDTO()).collect(Collectors.toList());
	}

}
