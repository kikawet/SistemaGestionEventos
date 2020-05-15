package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoExiste;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoRegistrado;
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
	 * @brief Método que devuelve un usuario al cliente para que este pueda trabajar con el
	 * @param login Es el id de usuario
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
	public List<Evento> listarEventos() {
		return eventos.values().stream().collect(Collectors.toList());
	}

	
	/**
	 * @brief Método para listar los eventos de un usuario, indicando
	 * 		  si este está aceptado o en lista de espera  
	 */
	@Override
	public List<EventoDTO> listarEventosDeUnUsuario(Usuario usuario) {
		
		Usuario usuarioValido = validarUsuario(usuario);
		
		List<EventoDTO> eventosUsuario = new ArrayList<>();
		
		for(Evento e : eventos.values()) {
			for(Usuario u : e.getAsistentes()) {
				if(u.getLogin().equals(usuarioValido.getLogin())) {
					eventosUsuario.add(e.toDTO(EstadoEvento.ACEPTADO));
				}
			}
			
			for(Usuario u : e.getListaEspera()) {
				if(u.getLogin().equals(usuarioValido.getLogin())) {
					eventosUsuario.add(e.toDTO(EstadoEvento.LISTA_DE_ESPERA));
				}
			}
		}
		
		return eventosUsuario;
	}
	

	/**
	 * @brief Método para crear un evento por usuaio
	 * @param Usuario que recibe del cliente
	 * @param Evento que recibe del cliente
	 */
	@Override
	public void crearEventoPorUsuario(Usuario usuario, Evento evento) {
		// TODO Auto-generated method stub

		Usuario usuarioValido = validarUsuario(usuario);
		
		if(eventos.containsKey(evento.getIdEvento()))
			throw new EventoYaRegistrado();

		usuarioValido.crearEvento(evento);

		eventos.put(evento.getIdEvento(), evento);
	}

	
	/**
	 * @brief Metodo para cancelar un evento de un usuario
	 */
	@Override
	public void cancelarEventoPorUsuario(Usuario usuario, String idEvento) {

		Evento evento = this.eventos.get(idEvento);
		Usuario usuarioValido = validarUsuario(usuario);

		if (evento == null)
			throw new EventoNoExiste();

		int pos = usuarioValido.getEventosCreados().indexOf(evento);

		if (!usuarioValido.getRol().equals(Usuario.RolUsuario.ADMIN) && pos == -1)
			throw new AccesoDenegado();

		
		usuarioValido.getEventosCreados().remove(pos);
		eventos.remove(idEvento);
	}

	
	/**
	 * @brief
	 */
	@Override
	public void inscribirUsuario(Usuario usuario, String idEvento) {
		// TODO Auto-generated method stub

		Usuario usuarioValido = validarUsuario(usuario);
		
		if (!eventos.containsKey(idEvento))
			throw new EventoNoRegistrado();

		eventos.get(idEvento).anadirAsistente(usuarioValido);

	}

	@Override
	public void cancelarInscripcionUsuario(Usuario usuario, String idEvento) {

		// TODO Auto-generated method stub
		Usuario usuarioValido = validarUsuario(usuario);
		
		if (!eventos.containsKey(idEvento)) {
			throw new EventoNoRegistrado();
		}
		
		eventos.get(idEvento).eliminarAsistente(usuarioValido);
	}

	
	
	/**
	 * @brief Metodo para validar un usuario internamente
	 * @param usuario
	 * @return
	 */
	private Usuario validarUsuario(Usuario usuario) {
		Usuario usuarioInterno = usuarios.get(usuario.getLogin());
		
		if(usuarioInterno == null || !usuarioInterno.mismoUID(usuario))
			throw new AccesoDenegado();
		
		return usuarioInterno;
	}
	
}
