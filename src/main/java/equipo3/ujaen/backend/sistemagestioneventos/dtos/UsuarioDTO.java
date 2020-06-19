package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioDTO {

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
//		this(login, password, null, null, -1, -1);
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
