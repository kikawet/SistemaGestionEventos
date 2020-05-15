package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.EstadoEvento;

public class Evento {

	public enum TipoEvento {
		BENEFICO, NO_BENEFICO
	}
	
	
	public enum Categoria{
		FESTIVAL_MUSICA,DEPORTE,CULTURAL,EXCURSIONES,CHARLAS,REUNIONES
	}

	private int aforoMaximo;
	private List<Usuario> asistentes;
	private String descripcion;
	private Date fecha;
	private String idEvento;
	private List<Usuario> listaEspera;

	private String lugar;
	private TipoEvento tipoEvento;
	private Categoria categoria;

	public Evento() {
		// TODO Auto-generated constructor stub
	}

	public Evento(String lugar, Date fecha, TipoEvento tipoEvento,Categoria categoriaEvento, String descripcion, int aforoMaximo,
			String idEvento) {
		super();
		this.lugar = lugar;
		this.fecha = fecha;
		this.tipoEvento = tipoEvento;
		this.categoria=categoriaEvento;
		this.descripcion = descripcion;
		this.aforoMaximo = aforoMaximo;
		this.idEvento = idEvento;

		this.asistentes = new ArrayList<>();
		this.listaEspera = new ArrayList<>();
	}

	public EstadoEvento anadirAsistente(Usuario u) {
		if (this.asistentes.size() < this.aforoMaximo) {
			this.asistentes.add(u);
			return EstadoEvento.ACEPTADO;
		}else {
			this.listaEspera.add(u);
			return EstadoEvento.LISTA_DE_ESPERA;
		}
	}

	public void eliminarAsistente(Usuario u) {
		if (this.asistentes.size() < this.aforoMaximo) {
			this.asistentes.remove(u);
			this.anadirAsistente(this.listaEspera.remove(0));
		} else {
			this.listaEspera.remove(u);
		}
	}

	public int getAforoMaximo() {
		return aforoMaximo;
	}

	public List<Usuario> getAsistentes() {
		return asistentes;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getIdEvento() {
		return idEvento;
	}

	public List<Usuario> getListaEspera() {
		return listaEspera;
	}

	public String getLugar() {
		return lugar;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	
	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public EventoDTO toDTO(EstadoEvento estadoEvento) {
		EventoDTO eventoDTO = new EventoDTO(this, estadoEvento);
		return eventoDTO;
	}

}
