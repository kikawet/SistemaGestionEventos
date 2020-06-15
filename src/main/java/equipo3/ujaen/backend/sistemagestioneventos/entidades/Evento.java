package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoEstaEvento;

@Entity
public class Evento {

	private int aforoMaximo;

	@ManyToMany
	private Set<Usuario> asistentes;

	@ManyToMany
	@OrderColumn
	private List<Usuario> listaEspera;

	private String descripcion;
	private LocalDateTime fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario creador;

	@Id
	@GeneratedValue
	private Long idEvento;

	private String lugar;
	private EventoDTO.TipoEvento tipoEvento;
	private EventoDTO.CategoriaEvento categoriaEvento;

	public Evento(EventoDTO eventoDTO) {

		this(eventoDTO.getAforoMaximo(), eventoDTO.getDescripcion(), eventoDTO.getFecha(), eventoDTO.getLugar(),
				eventoDTO.getTipoEvento(), eventoDTO.getCategoriaEvento(),
				eventoDTO.getCreador() != null ? new Usuario(eventoDTO.getCreador()) : null);

		if (eventoDTO.getIdEvento() != null)
			this.idEvento = eventoDTO.getIdEvento();
		else
			eventoDTO.setIdEvento(this.idEvento);
	}

	public Evento() {
		super();
		// TODO llamar al constuctor parametrizado
		this.lugar = null;
		this.fecha = null;
		this.tipoEvento = null;
		this.categoriaEvento = null;
		this.descripcion = null;
		this.aforoMaximo = 0;
		this.idEvento = null;
		this.creador = null;
		this.asistentes = new HashSet<>();
		this.listaEspera = new ArrayList<>();
	}

	public Evento(int aforoMaximo, String descripcion, LocalDateTime fecha, String lugar,
			EventoDTO.TipoEvento tipoEvento, EventoDTO.CategoriaEvento categoriaEvento, Usuario creador) {
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

	public int getAforoMaximo() {
		return aforoMaximo;
	}

	public List<Usuario> getAsistentes() {
		return asistentes.stream().collect(Collectors.toList());
	}

	public String getDescripcion() {
		return descripcion;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public Long getIdEvento() {
		return idEvento;
	}

	public List<Usuario> getListaEspera() {
		return listaEspera.stream().collect(Collectors.toList());
	}

	public String getLugar() {
		return lugar;
	}

	public EventoDTO.TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public EventoDTO.CategoriaEvento getCategoriaEvento() {
		return this.categoriaEvento;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public EventoDTO toDTO(Usuario u) {
		EventoDTO eventoDTO = new EventoDTO(this.aforoMaximo, this.descripcion, this.fecha, this.idEvento, this.lugar,
				this.tipoEvento, this.categoriaEvento, this.asistentes.size(), this.listaEspera.size(),
				getEstadoUsuario(u), this.creador.toDTO());
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

	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public void setTipoEvento(EventoDTO.TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public void setCategoriaEvento(EventoDTO.CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}

	public EstadoUsuarioEvento getEstadoUsuario(Usuario u) {
		return this.asistentes.contains(u) ? EstadoUsuarioEvento.ACEPTADO
				: this.listaEspera.contains(u) ? EstadoUsuarioEvento.LISTA_DE_ESPERA : null;

	}

	public Usuario getCreador() {
		return creador;
	}

	public void setCreador(Usuario creador) {
		this.creador = creador;
	}

}