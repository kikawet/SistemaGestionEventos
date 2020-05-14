package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

	private String login;
	private String password;

	private List<Evento> eventosCreados;
	
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Usuario(String login, String password) {
		super();
		this.login = login;
		this.password = password;
		
		this.eventosCreados = new ArrayList<>();
	}

	
	public Evento nuevoEvento() {
		return null;
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public List<Evento> getEventosCreados() {
		return eventosCreados;
	}
	
	public void crearEvento(Evento e) {
		eventosCreados.add(e);
	}
	
}
