package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.AccesoDenegado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoExiste;
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
		return null;
	}

	@Override
	public List<Evento> listarEventos() {
		return eventos.values().stream().collect(Collectors.toList());
	}

	@Override
	public void crearEvento(Evento evento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void crearEventoPorusuario(String login, Evento evento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelarEvento(String idEvento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelarEventoPorUsuario(String idEvento, String login) {

		Evento evento = this.eventos.get(idEvento);
		Usuario usuario = this.usuarios.get(login);

		if (usuario == null)
			throw new UsuarioNoRegistrado();

		if (evento == null)
			throw new EventoNoExiste();

		int pos = usuario.getEventosCreados().indexOf(evento);

		if (pos == -1)
			throw new AccesoDenegado();

		usuario.getEventosCreados().remove(pos);

	}

	@Override
	public void inscribirUsuario(String login, String idEvento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelarInscripcionUsuario(String login, String idEvento) {
		// TODO Auto-generated method stub

	}

}
