package equipo3.ujaen.backend.sistemagestioneventos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EventoNoExiste extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4148961994738622571L;

	public EventoNoExiste() {
		// TODO Auto-generated constructor stub
	}

}
