package equipo3.ujaen.backend.sistemagestioneventos.interfaces;

import java.util.List;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;

public interface InterfaceSistemaGestionEventos {

	public void registroUsuarios(String login, String password);

	public UsuarioDTO loginUsuario(String login, String password);

	public List<EventoDTO> listarEventos(CategoriaEvento categoria, String descripcionParcial, int cantidadMaxima);

	public List<EventoDTO> listarEventosInscritosDeUnUsuario(UsuarioDTO usuarioDTO);

	public List<EventoDTO> listarEventosCreadosPorUnUsuario(UsuarioDTO usuarioDTO);

	public void crearEventoPorUsuario(UsuarioDTO usuarioDTO, EventoDTO eventoDTO, boolean inscribirCreador);

	public void cancelarEventoPorUsuario(UsuarioDTO usuarioDTO, Long idEvento);

	public EstadoUsuarioEvento inscribirUsuario(UsuarioDTO usuarioDTO, Long idEvento);

	public void cancelarInscripcionUsuario(UsuarioDTO usuarioDTO, Long idEvento);

}
