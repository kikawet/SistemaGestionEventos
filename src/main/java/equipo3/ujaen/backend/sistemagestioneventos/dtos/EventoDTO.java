package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO extends RepresentationModel<EventoDTO> implements Serializable {

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

	private Long idEvento;
	@Min(message = "El valor minimo para el aforo de un evento es 5", value = 5)
	@Max(message = "El valor máximo para el aforo de un evento es 5000", value = 5000)
	private int aforoMaximo;
	private String descripcion;
	@Future(message = "El evento tiene que ser en un futuro")
	private LocalDateTime fecha;
	private String lugar;
	private EventoDTO.TipoEvento tipoEvento;
	private EventoDTO.CategoriaEvento categoriaEvento;
	private UUID idCreador;
	private int numAsistentes;
	private int numListaEspera;

	private EventoDTO.EstadoUsuarioEvento estado;

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
	}

}
