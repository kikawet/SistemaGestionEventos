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
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;


@Component
public class SistemaGestionEventos implements InterfaceSistemaGestionEventos {

	private Map<String, Usuario> usuarios;
	private Map<String, Evento> eventos;

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

	@Override
	public String loginUsuario(String login, String password) {
		// TODO Auto-generated method stub
		Usuario usuario = usuarios.get(login);

		if (usuario.equals(null)) {
			throw new UsuarioNoRegistrado();
		}

		if (!usuario.getPassword().equals(password)) {
			throw new AccesoDenegado();
		}

		return "LOGIN COMPLETADO";
	}

	@Override
	public List<Evento> listarEventos() {
		return eventos.values().stream().collect(Collectors.toList());
	}

	
	
	
	@Override
	public List<EventoDTO> listarEventosDeUnUsuario(String login) {
		// TODO Auto-generated method stub
		if(!usuarios.containsKey(login)) {
			throw new UsuarioNoRegistrado();
		}
		
		List<EventoDTO> eventosUsuario = new ArrayList<>();
		for(Evento e : eventos.values()) {
			for(Usuario u : e.getAsistentes()) {
				if(u.getLogin().equals(login)) {
					eventosUsuario.add(e.toDTO(EstadoEvento.ACEPTADO));
				}
			}
			
			for(Usuario u : e.getListaEspera()) {
				if(u.getLogin().equals(login)) {
					eventosUsuario.add(e.toDTO(EstadoEvento.LISTA_DE_ESPERA));
				}
			}
		}
		
		return eventosUsuario;
	}
	

	@Override
	public void crearEventoPorusuario(String login, Evento evento) {
		// TODO Auto-generated method stub

		Usuario usuario = usuarios.get(login);

		if (usuario == null) {
			throw new UsuarioNoRegistrado();
		}

		usuario.crearEvento(evento);

		eventos.put(evento.getIdEvento(), evento);
	}

	@Override
	public void cancelarEventoPorUsuario(String login, String idEvento) {

		Evento evento = this.eventos.get(idEvento);
		Usuario usuario = this.usuarios.get(login);

		if (usuario == null)
			throw new UsuarioNoRegistrado();

		if (evento == null)
			throw new EventoNoExiste();

		int pos = usuario.getEventosCreados().indexOf(evento);

		if (!usuario.getRol().equals(Usuario.RolUsuario.ADMIN) && pos == -1)
			throw new AccesoDenegado();

		usuario.getEventosCreados().remove(pos);

	}

	@Override
	public void inscribirUsuario(String login, String idEvento) {
		// TODO Auto-generated method stub

		if (!usuarios.containsKey(login))
			throw new UsuarioNoRegistrado();

		if (!eventos.containsKey(idEvento))
			throw new EventoNoRegistrado();

		eventos.get(idEvento).anadirAsistente(usuarios.get(login));

	}

	@Override
	public void cancelarInscripcionUsuario(String login, String idEvento) {
		// TODO Auto-generated method stub
		if (!usuarios.containsKey(login)) {
			throw new UsuarioNoRegistrado();
		}
		if (!eventos.containsKey(idEvento)) {
			throw new EventoNoRegistrado();
		}
		eventos.get(idEvento).eliminarAsistente(usuarios.get(login));
	}

	
}
