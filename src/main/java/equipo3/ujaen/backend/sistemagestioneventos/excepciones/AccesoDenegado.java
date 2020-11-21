package equipo3.ujaen.backend.sistemagestioneventos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AccesoDenegado extends RuntimeException {

	private static final long serialVersionUID = 7950259912747954645L;

	public AccesoDenegado(String msg) {
		super(msg);
	}

}
