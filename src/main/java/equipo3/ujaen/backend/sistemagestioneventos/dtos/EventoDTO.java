package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;

public class EventoDTO {
	
	public enum EstadoEvento {
		ACEPTADO,
		LISTA_DE_ESPERA
	}
	
	private Evento evento;
	private EstadoEvento estadoEvento;
	
	public EventoDTO(Evento evento, EstadoEvento estadoEvento) {
		// TODO Auto-generated constructor stub
		this.evento = evento;
		this.estadoEvento = estadoEvento;
	}

	public Evento getEvento() {
		return evento;
	}
	
	public EstadoEvento getEstadoEvento() {
		return estadoEvento;
	}
	
	public void setEstadoEvento(EstadoEvento estadoEvento) {
		this.estadoEvento = estadoEvento;
	}
	

}
