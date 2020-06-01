package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO.RolUsuario;;

public class Usuario {
	private RolUsuario rol;

	private String login;
	private String password;

	private Long uId;

	private List<Evento> eventosCreados;
	private Set<Evento> eventosInscritos;

	public Usuario(UsuarioDTO usuarioDTO) {
		this(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		this.rol = usuarioDTO.getRol();

		if (usuarioDTO.getuId() != null)
			this.uId = usuarioDTO.getuId();

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

	public Long getuId() {
		return uId;
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

	public UsuarioDTO toDTO() {
		return new UsuarioDTO(this.login, this.password, this.uId, this.rol, this.eventosCreados.size(),
				this.eventosInscritos.size());
	}

}
