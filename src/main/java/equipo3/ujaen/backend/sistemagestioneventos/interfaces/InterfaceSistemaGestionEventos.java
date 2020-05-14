package equipo3.ujaen.backend.sistemagestioneventos.interfaces;

import java.util.List;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.utils.Pair;

public interface InterfaceSistemaGestionEventos {

	public void registroUsuarios(String login, String password);

	public String loginUsuario(String login, String password);

	public List<Evento> listarEventos();

	// El booleano indica si está en lista de espera o no:
	// True: no está en lista de espera
	// False: está en lista de espera
	public List<Pair<Boolean, Evento>> listarEventosDeUnUsuario(String login);

	public void crearEventoPorusuario(String login, Evento evento);

	public void cancelarEventoPorUsuario(String login, String idEvento);

	public void inscribirUsuario(String login, String idEvento);

	public void cancelarInscripcionUsuario(String login, String idEvento);

}
