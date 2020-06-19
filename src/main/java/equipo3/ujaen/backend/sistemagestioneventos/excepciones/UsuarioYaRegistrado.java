package equipo3.ujaen.backend.sistemagestioneventos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Ya existe un usuario con esos datos")
public class UsuarioYaRegistrado extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5499797749913536831L;

	public UsuarioYaRegistrado() {
		// TODO Auto-generated constructor stub
	}

}
