package equipo3.ujaen.backend.sistemagestioneventos.entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO.RolUsuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
	private RolUsuario rol;

	@Column(unique = true)
	private String login;
	private String email;
	private String password;

	@Id
	@GeneratedValue
	@Type(type = "uuid-char")
	private UUID uId;

	// Al modificar creador se actualiza la lista
	@OneToMany(mappedBy = "creador")
	private List<Evento> eventosCreados;

	@ManyToMany
	private List<Evento> eventosInscritos;// Para trabajar con la paginación usar listas es más comodo

	public Usuario(UsuarioDTO usuarioDTO) {
		this(usuarioDTO.getLogin(), usuarioDTO.getPassword());

		this.rol = usuarioDTO.getRol();
		this.email = usuarioDTO.getEmail();

		if (usuarioDTO.getUId() != null)
			this.uId = usuarioDTO.getUId();

	}

	public Usuario(String login, String password) {
		super();
		this.login = login;
		this.password = password;

		this.eventosCreados = new ArrayList<>();
		this.eventosInscritos = new ArrayList<>();
		this.rol = null;
		this.uId = null;
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
		return new UsuarioDTO(this.login, this.password, this.email, this.uId, this.rol, this.eventosCreados.size(),
				this.eventosInscritos.size(), true);
	}

}