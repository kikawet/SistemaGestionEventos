package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoEstaEvento;

@Entity
public class Evento {

	private int aforoMaximo;

	@ManyToMany
	private Set<Usuario> asistentes;

	@ManyToMany
	private Set<Usuario> listaEspera;

	private String descripcion;
	private LocalDateTime fecha;
	@OneToMany(mappedBy = "uId")
	private Long idCreador;

	@Id
	@GeneratedValue
	private Long idEvento;

	private String lugar;
	private EventoDTO.TipoEvento tipoEvento;
	private EventoDTO.CategoriaEvento categoriaEvento;

	public Evento(EventoDTO eventoDTO) {
		this(eventoDTO.getAforoMaximo(), eventoDTO.getDescripcion(), eventoDTO.getFecha(), eventoDTO.getLugar(),
				eventoDTO.getTipoEvento(), eventoDTO.getCategoriaEvento(), eventoDTO.getIdCreador());

		if (eventoDTO.getIdEvento() != null)
			this.idEvento = eventoDTO.getIdEvento();
		else
			eventoDTO.setIdEvento(this.idEvento);
	}

	public Evento(int aforoMaximo, String descripcion, LocalDateTime fecha, String lugar, EventoDTO.TipoEvento tipoEvento,
			EventoDTO.CategoriaEvento categoriaEvento, Long idCreador) {
		super();
		this.lugar = lugar;
		this.fecha = fecha;
		this.tipoEvento = tipoEvento;
		this.categoriaEvento = categoriaEvento;
		this.descripcion = descripcion;
		this.aforoMaximo = aforoMaximo;
		this.idEvento = null;
		this.idCreador = idCreador;
		this.asistentes = new HashSet<>();
		this.listaEspera = new LinkedHashSet<>();
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
		} else if (!this.asistentes.contains(u) && this.listaEspera.add(u)) {
			estado = EstadoUsuarioEvento.LISTA_DE_ESPERA;
		}

		return estado;
	}

	public void eliminarAsistente(Usuario u) {
		if (!this.asistentes.remove(u)) {
			if (!this.listaEspera.remove(u))
				throw new UsuarioNoEstaEvento();
		} else if (!this.listaEspera.isEmpty()) {
			Iterator<Usuario> primero = this.listaEspera.iterator();

			// Insertamos el primer elemento de la lista
			this.asistentes.add(primero.next());
			// Lo borramos de la lista de espera
			primero.remove();
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
				getEstadoUsuario(u), this.idCreador);
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

}