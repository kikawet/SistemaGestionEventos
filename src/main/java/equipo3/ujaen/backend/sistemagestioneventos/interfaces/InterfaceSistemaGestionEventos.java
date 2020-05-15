package equipo3.ujaen.backend.sistemagestioneventos.interfaces;

import java.util.List;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

public interface InterfaceSistemaGestionEventos {

	public void registroUsuarios(String login, String password);

	public Usuario loginUsuario(String login, String password);

	public List<Evento> listarEventos();

	public List<EventoDTO> listarEventosDeUnUsuario(Usuario usuario);

	public void crearEventoPorUsuario(Usuario usuario, Evento evento);

	public void cancelarEventoPorUsuario(Usuario usuario, Long idEvento);

	public EstadoEvento inscribirUsuario(Usuario usuario, Long idEvento);

	public void cancelarInscripcionUsuario(Usuario usuario, Long idEvento);

	
}
