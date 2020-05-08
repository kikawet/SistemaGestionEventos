package equipo3.ujaen.backend.sistemagestioneventos.interfaces;

import java.util.List;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;

public interface InterfaceSistemaGestionEventos {

	public void registroUsuarios(String login, String password);
	public String loginUsuario(String login, String password);
	public List<Evento> listarEventos();
	public void crearEvento(Evento evento);
	public void crearEventoPorusuario(String login, Evento evento);
	public void cancelarEvento(String idEvento);
	public void cancelarEventoPorUsuario(String idEvento, String login);
	public void inscribirUsuario(String login, String idEvento);
	public void cancelarInscripcionUsuario(String login, String idEvento);
	
}
