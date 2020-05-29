package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoUsuarioEvento;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoEstaEvento;

public class Evento {

	private int aforoMaximo;
	private Set<Usuario> asistentes;
	private String descripcion;
	private Date fecha;
	private Long idEvento;
	private Set<Usuario> listaEspera;

	private String lugar;
	private EventoDTO.TipoEvento tipoEvento;
	private EventoDTO.CategoriaEvento categoriaEvento;

	public Evento(String lugar, Date fecha, EventoDTO.TipoEvento tipoEvento, EventoDTO.CategoriaEvento categoriaEventoEvento, String descripcion,
			int aforoMaximo) {
		super();
		this.lugar = lugar;
		this.fecha = fecha;
		this.tipoEvento = tipoEvento;
		this.categoriaEvento = categoriaEventoEvento;
		this.descripcion = descripcion;
		this.aforoMaximo = aforoMaximo;
		this.idEvento = new Random().nextLong();

		this.asistentes = new HashSet<>();
		this.listaEspera = new LinkedHashSet<>();
	}

	public EstadoUsuarioEvento anadirAsistente(Usuario u) {
		if (this.asistentes.size() < this.aforoMaximo) {
			this.asistentes.add(u);
			return EstadoUsuarioEvento.ACEPTADO;
		} else {
			this.listaEspera.add(u);
			return EstadoUsuarioEvento.LISTA_DE_ESPERA;
		}
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

	public Date getFecha() {
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

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public EventoDTO toDTO(Usuario u) {
		EventoDTO eventoDTO = new EventoDTO(this.aforoMaximo, this.descripcion, this.fecha, this.idEvento, this.lugar,
				this.tipoEvento, this.categoriaEvento, this.asistentes.size(), this.listaEspera.size(), getEstadoUsuario(u));
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
