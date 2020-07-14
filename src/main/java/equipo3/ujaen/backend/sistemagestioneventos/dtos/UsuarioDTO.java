package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

	@Size(min = 6, max = 25, message = "El login debe de estar entre {min} y {max} caracteres")
	private String login;
	@Pattern(regexp = "^((?=(.*[\\d]){2,})(?=(.*[a-z]){2,})(?=(.*[A-Z]){2,})(?=(.*[^\\w\\d\\s]){2,})).{8,}$", message = "La contraseña debe de tener al menos 8 caracteres, 2 minusculas, 2 mayusculas, 2 números y 2 caracteres especiales. Sin repetir dos caracteres seguidos")
	private String password;
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Email incorrecto ejemplo: ejemplo@simple.com")
	private String email;
	private UUID uId;
	private RolUsuario rol;

	private int numEventosCreados;
	private int numEventosInscritos;

	@AssertTrue(message = "Debes de aceptar los terminos y condiciones")
	private boolean terminos = false;

	public UsuarioDTO(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public void clone(UsuarioDTO u) {
		this.rol = u.rol;
		this.login = u.login;
		this.email = u.email;
		this.password = u.password;
		this.uId = u.uId;
		this.numEventosCreados = u.numEventosCreados;
		this.numEventosInscritos = u.numEventosInscritos;
	}

}
