package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import java.time.LocalDateTime;

public class EventoDTO {

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
	private Long idCreador;;
	private int numAsistentes;
	private int numListaEspera;

	private EventoDTO.EstadoUsuarioEvento estado;

	public EventoDTO(int aforoMaximo, String descripcion, LocalDateTime fecha, Long idEvento, String lugar,
			EventoDTO.TipoEvento tipoEvento, EventoDTO.CategoriaEvento categoriaEvento, int numAsistentes,
			int numListaEspera, EventoDTO.EstadoUsuarioEvento estado, Long idCreador) {
		super();
		this.aforoMaximo = aforoMaximo;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.idEvento = idEvento;
		this.lugar = lugar;
		this.tipoEvento = tipoEvento;
		this.categoriaEvento = categoriaEvento;
		this.numAsistentes = numAsistentes;
		this.numListaEspera = numListaEspera;
		this.estado = estado;
		this.idCreador = idCreador;
	}

	public int getAforoMaximo() {
		return aforoMaximo;
	}

	public void setAforoMaximo(int aforoMaximo) {
		this.aforoMaximo = aforoMaximo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Long getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public EventoDTO.TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(EventoDTO.TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public EventoDTO.CategoriaEvento getCategoriaEvento() {
		return categoriaEvento;
	}

	public void setCategoriaEvento(EventoDTO.CategoriaEvento categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}

	public int getNumAsistentes() {
		return numAsistentes;
	}

	public Long getIdCreador() {
		return idCreador;
	}

	public void setIdCreador(Long idCreador) {
		this.idCreador = idCreador;
	}

	public void setNumAsistentes(int numAsistentes) {
		this.numAsistentes = numAsistentes;
	}

	public int getNumListaEspera() {
		return numListaEspera;
	}

	public void setNumListaEspera(int numListaEspera) {
		this.numListaEspera = numListaEspera;
	}

	public EventoDTO.EstadoUsuarioEvento getEstado() {
		return estado;
	}

	public void setEstado(EventoDTO.EstadoUsuarioEvento estado) {
		this.estado = estado;
	}

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
