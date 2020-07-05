package equipo3.ujaen.backend.sistemagestioneventos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UsuarioNoRegistrado extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -2575289938644460213L;

	public UsuarioNoRegistrado() {
		this("No existe ese login");
	}

	public UsuarioNoRegistrado(String msg) {
		super(msg);
	}

}