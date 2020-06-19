package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 413336268119887503L;

	// ROL = null = ningún rol
	public static enum RolUsuario {
		ADMIN
	}

	private String login;
	private String password;
	private Long uId;
	private RolUsuario rol;

	private int numEventosCreados;
	private int numEventosInscritos;

	public UsuarioDTO(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public void clone(UsuarioDTO u) {
		this.rol = u.rol;
		this.login = u.login;
		this.password = u.password;
		this.uId = u.uId;
		this.numEventosCreados = u.numEventosCreados;
		this.numEventosInscritos = u.numEventosInscritos;
	}

}
