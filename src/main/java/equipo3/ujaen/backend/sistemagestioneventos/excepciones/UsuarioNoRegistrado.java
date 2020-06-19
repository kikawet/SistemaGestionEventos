package equipo3.ujaen.backend.sistemagestioneventos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No existe ese usuario")
public class UsuarioNoRegistrado extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1458181230160378012L;

	public UsuarioNoRegistrado() {
		// TODO Auto-generated constructor stub
	}

}