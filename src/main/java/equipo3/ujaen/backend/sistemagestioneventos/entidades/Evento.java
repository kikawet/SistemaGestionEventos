package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento {

	
	public enum TipoEvento {
		BENEFICO,
		NO_BENEFICO
	}
	
	private String lugar;
	private Date fecha;
	private TipoEvento tipoEvento;
	private String descripcion;
	private int aforoMaximo;
	private String idEvento;
	
	private List<Usuario> asistentes;
	private List<Usuario> listaEspera;
	
	public Evento() {
		// TODO Auto-generated constructor stub
	}

	public Evento(String lugar, Date fecha, TipoEvento tipoEvento, String descripcion, int aforoMaximo,
			String idEvento) {
		super();
		this.lugar = lugar;
		this.fecha = fecha;
		this.tipoEvento = tipoEvento;
		this.descripcion = descripcion;
		this.aforoMaximo = aforoMaximo;
		this.idEvento = idEvento;
		
		this.asistentes = new ArrayList<>();
		this.listaEspera = new ArrayList<>();
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getLugar() {
		return lugar;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public int getAforoMaximo() {
		return aforoMaximo;
	}

	public String getIdEvento() {
		return idEvento;
	}

	public List<Usuario> getAsistentes() {
		return asistentes;
	}

	public List<Usuario> getListaEspera() {
		return listaEspera;
	}
	
	
	
	
	
}
