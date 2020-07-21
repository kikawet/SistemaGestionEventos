package equipo3.ujaen.backend.sistemagestioneventos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ParametrosInvalidos extends RuntimeException {

	private static final long serialVersionUID = 3679609300221696669L;

	public ParametrosInvalidos(String msg) {
		super(msg);
	}

	public ParametrosInvalidos() {
	}
}
