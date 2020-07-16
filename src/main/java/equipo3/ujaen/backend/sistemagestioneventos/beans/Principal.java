package equipo3.ujaen.backend.sistemagestioneventos.beans;

import java.io.Serializable;

import javax.websocket.Session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@SessionScope
@NoArgsConstructor
@Getter
@Setter
public class Principal implements Serializable {
	private String name;
}
