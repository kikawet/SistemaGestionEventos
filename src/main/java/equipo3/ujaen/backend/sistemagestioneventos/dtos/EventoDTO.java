package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8509749646502520670L;

	public enum EstadoUsuarioEvento {
		ACEPTADO, LISTA_DE_ESPERA
	}

	public enum TipoEvento {
		BENEFICO, NO_BENEFICO
	}

	public enum CategoriaEvento {
		FESTIVAL_MUSICA, DEPORTE, CULTURAL, EXCURSIONES, CHARLAS, REUNIONES
	}

	// Atributos de evento
	private int aforoMaximo;
	private String descripcion;
	private LocalDateTime fecha;
	private Long idEvento;
	private String lugar;
	private EventoDTO.TipoEvento tipoEvento;
	private EventoDTO.CategoriaEvento categoriaEvento;
	private UsuarioDTO creador;;
	private int numAsistentes;
	private int numListaEspera;

	private EventoDTO.EstadoUsuarioEvento estado;

//	public EventoDTO(int aforoMaximo, String descripcion, LocalDateTime fecha, Long idEvento, String lugar,
//			EventoDTO.TipoEvento tipoEvento, EventoDTO.CategoriaEvento categoriaEvento, int numAsistentes,
//			int numListaEspera, EventoDTO.EstadoUsuarioEvento estado, UsuarioDTO creador) {
//		super();
//		this.aforoMaximo = aforoMaximo;
//		this.descripcion = descripcion;
//		this.fecha = fecha;
//		this.idEvento = idEvento;
//		this.lugar = lugar;
//		this.tipoEvento = tipoEvento;
//		this.categoriaEvento = categoriaEvento;
//		this.numAsistentes = numAsistentes;
//		this.numListaEspera = numListaEspera;
//		this.estado = estado;
//		this.creador = creador;
//	}

	public void clone(EventoDTO e) {
		this.aforoMaximo = e.aforoMaximo;
		this.descripcion = e.descripcion;
		this.fecha = e.fecha;
		this.idEvento = e.idEvento;
		this.lugar = e.lugar;
		this.tipoEvento = e.tipoEvento;
		this.categoriaEvento = e.categoriaEvento;
		this.numAsistentes = e.numAsistentes;
		this.numListaEspera = e.numListaEspera;
		this.estado = e.estado;
		this.creador = e.creador;
	}

}
