package equipo3.ujaen.backend.sistemagestioneventos.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.Categoria;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@SpringBootTest(classes = { SistemaGestionEventos.class })
public class SistemaGestionEventosIntegrationTest {

	@Autowired
	InterfaceSistemaGestionEventos gestorEventos;
	
	@Test
	void loginUsuarioTest() {
		
		String loginUsuario = "21025923J";
		String passwordUsuario = "123456789a.";
		
		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);
		
		Usuario u = gestorEventos.loginUsuario(loginUsuario, passwordUsuario);
		
		assertNotNull(u);
		assertEquals(loginUsuario, u.getLogin());
		assertEquals(passwordUsuario, u.getPassword());
	}
	
	@Test
	void crearEventoPorUsuario() {
		String loginUsuario = "11111111X";
		String passwordUsuario = "123456789a.";
		
		gestorEventos.registroUsuarios(loginUsuario, passwordUsuario);
		
		Usuario u = gestorEventos.loginUsuario(loginUsuario, passwordUsuario);

		assertNotNull(u);
		assertEquals(loginUsuario, u.getLogin());
		assertEquals(passwordUsuario, u.getPassword());
		
		Evento e = new Evento("Jaén", Date.from(Instant.now()), TipoEvento.BENEFICO,
							  Categoria.CHARLAS, "Hola caracola", 30);
		
		gestorEventos.crearEventoPorUsuario(u, e);
		
		assertNotNull(e);
		
		List<Evento> eventos = gestorEventos.listarEventos();
		
		assertEquals(1, eventos.size());
	}

}
