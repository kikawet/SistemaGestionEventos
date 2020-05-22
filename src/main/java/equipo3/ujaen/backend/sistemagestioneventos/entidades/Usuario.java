package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Usuario {
	// ROL = null = ningún rol
	public static enum RolUsuario {
		ADMIN
	}

	private RolUsuario rol;

	private String login;
	private String password;

	private long uId;

	private List<Evento> eventosCreados;
	private Set<Evento> eventosInscritos;

	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	public Usuario(String login, String password) {
		super();
		this.login = login;
		this.password = password;

		this.eventosCreados = new ArrayList<>();
		this.eventosInscritos = new HashSet<>();
		this.rol = null;
		this.uId = new Random().nextLong();
	}

	public Evento nuevoEvento() {
		return null;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return uId == other.uId;
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

	public RolUsuario getRol() {
		return rol;
	}

	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}

	public long getuId() {
		return uId;
	}

	public boolean mismoUID(Usuario u) {
		return uId == u.uId;
	}

	public List<Evento> getEventosInscritos() {
		return eventosInscritos.stream().collect(Collectors.toList());
	}

	public boolean inscribir(Evento e) {
		return this.eventosInscritos.add(e);
	}

	public boolean cancelarInscripcion(Evento e) {
		return this.eventosInscritos.remove(e);
	}

}
