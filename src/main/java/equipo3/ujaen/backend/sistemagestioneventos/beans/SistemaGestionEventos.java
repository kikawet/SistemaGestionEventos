package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.util.HashMap;
import java.util.Map;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

public class SistemaGestionEventos {
	
	private Map<String, Usuario> usuarios;
	private Map<String, Evento> eventos;
	
	public SistemaGestionEventos() {
		// TODO Auto-generated constructor stub
		usuarios = new HashMap<>();
		eventos = new HashMap<>();
	}
	
	

	
}
