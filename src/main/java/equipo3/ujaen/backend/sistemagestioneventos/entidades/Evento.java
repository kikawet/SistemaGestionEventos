package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoEstaEvento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Evento {

	private int aforoMaximo;

	@ManyToMany
	private Set<Usuario> asistentes;

	@ManyToMany
	@OrderColumn
	private List<Usuario> listaEspera;

	@Column(columnDefinition = "LONGTEXT")
	private String descripcion;
	private LocalDateTime fecha;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Usuario creador;

	@Id
	@GeneratedValue
	private Long idEvento;

	private String lugar;
	private EventoDTO.TipoEvento tipoEvento;
	@NotNull
	private EventoDTO.CategoriaEvento categoriaEvento;

	private String titulo;
	private String foto;

	public Evento(EventoDTO eventoDTO, Usuario creador) {

		this(eventoDTO.getAforoMaximo(), eventoDTO.getDescripcion(), eventoDTO.getFecha(), eventoDTO.getLugar(),
				eventoDTO.getTipoEvento(), eventoDTO.getCategoriaEvento(), creador, eventoDTO.getTitulo(),
				eventoDTO.getFoto());

		if (eventoDTO.getIdEvento() != null)
			this.idEvento = eventoDTO.getIdEvento();
		else
			eventoDTO.setIdEvento(this.idEvento);
	}

	public Evento(int aforoMaximo, String descripcion, LocalDateTime fecha, String lugar,
			EventoDTO.TipoEvento tipoEvento, EventoDTO.CategoriaEvento categoriaEvento, Usuario creador, String titulo,
			String foto) {
		super();
		this.lugar = lugar;
		this.fecha = fecha;
		this.tipoEvento = tipoEvento;
		this.categoriaEvento = categoriaEvento;
		this.descripcion = descripcion;
		this.aforoMaximo = aforoMaximo;
		this.idEvento = null;
		this.creador = creador;
		this.asistentes = new HashSet<>();
		this.listaEspera = new ArrayList<>();
		this.titulo = titulo;
		this.foto = foto;
	}

	/**
	 * @brief Añade un nuevo usuario a la lista de asistentes o de espera en función
	 *        del aforo
	 * @param u
	 * @return null si no se ha añadido o la lista donde se insertó
	 */
	public EstadoUsuarioEvento anadirAsistente(Usuario u) {
		EstadoUsuarioEvento estado = null;

		if (this.asistentes.size() < this.aforoMaximo) {
			if (this.asistentes.add(u))
				estado = EstadoUsuarioEvento.ACEPTADO;
		} else if (!this.asistentes.contains(u) && !this.listaEspera.contains(u) && this.listaEspera.add(u)) {
			estado = EstadoUsuarioEvento.LISTA_DE_ESPERA;
		}

		return estado;
	}

	public void eliminarAsistente(Usuario u) {
		if (!this.asistentes.remove(u)) {
			if (!this.listaEspera.remove(u))
				throw new UsuarioNoEstaEvento();
		} else if (!this.listaEspera.isEmpty()) {
			Usuario primero = this.listaEspera.remove(0);
			this.asistentes.add(primero);
		}
	}

	public List<Usuario> getAsistentes() {
		return asistentes.stream().collect(Collectors.toList());
	}

	public List<Usuario> getListaEspera() {
		return listaEspera.stream().collect(Collectors.toList());
	}

	public EventoDTO toDTO(Usuario u) {
		UUID creadorId = this.creador == null ? null : this.creador.getUId();
		EventoDTO eventoDTO = new EventoDTO(this.idEvento, this.aforoMaximo, this.descripcion, this.fecha, this.lugar,
				this.tipoEvento, this.categoriaEvento, creadorId, this.asistentes.size(), this.listaEspera.size(),
				this.getEstadoUsuario(u), this.titulo, this.foto);

		return eventoDTO;
	}

	public EventoDTO toDTO() {
		return toDTO(null);
	}

	public void setAforoMaximo(int aforoMaximo) {
		if (aforoMaximo < this.asistentes.size())
			throw new InvalidParameterException("No se puede reducir el aforo si hay gente registrada");

		this.aforoMaximo = aforoMaximo;
	}

	public EstadoUsuarioEvento getEstadoUsuario(Usuario u) {
		return this.asistentes.contains(u) ? EstadoUsuarioEvento.ACEPTADO
				: this.listaEspera.contains(u) ? EstadoUsuarioEvento.LISTA_DE_ESPERA : null;
	}

}