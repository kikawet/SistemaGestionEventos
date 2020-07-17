package equipo3.ujaen.backend.sistemagestioneventos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ch.qos.logback.classic.Logger;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoPrescrito;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@SpringBootApplication
@EntityScan(basePackages = ServidorSistemaGestionEventos.basePackageRoot + ".entidades")
@EnableJpaRepositories(basePackages = ServidorSistemaGestionEventos.basePackageRoot + ".dao")
@EnableCaching
public class ServidorSistemaGestionEventos {

	public static final String basePackageRoot = "equipo3.ujaen.backend.sistemagestioneventos";

	private static final Logger log = (Logger) LoggerFactory.getLogger(ServidorSistemaGestionEventos.class);

	public static void main(String[] args) {
		SpringApplication.run(ServidorSistemaGestionEventos.class, args);

		System.out.println("Saludos desde el servidor");
	}

	@Autowired(required = false)
	private InterfaceSistemaGestionEventos ige;

	@PostConstruct
	public void init() {
		if (ige != null) {
			log.info("Creando eventos y usuarios");

			String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed\r\n"
					+ "										do eiusmod tempor incididunt ut labore et dolore magna aliqua.\r\n"
					+ "										Ut enim ad minim veniam, quis nostrud exercitation ullamco\r\n"
					+ "										laboris nisi ut aliquip ex ea commodo consequat. Duis aute\r\n"
					+ "										irure dolor in reprehenderit in voluptate velit esse cillum\r\n"
					+ "										dolore eu fugiat nulla paria";

			UsuarioDTO patricio = new UsuarioDTO("Patricio Ruiz", "1234");
			UsuarioDTO francisco = new UsuarioDTO("Francisco López", "1234");

			patricio.setEmail("patricio@yahoo.com");
			francisco.setEmail("flo00008@red.ujaen.es");

			try {
				ige.registroUsuarios(patricio);
				ige.registroUsuarios(francisco);

	            log.info("Usuarios creados");
    		} catch (UsuarioYaRegistrado e) {
    			log.info("Usuarios ya registrados");
    		}
    
    		patricio = ige.getUsuario(ige.loginUsuario("Patricio Ruiz", "1234"));
    		francisco = ige.getUsuario(ige.loginUsuario("Francisco López", "1234"));
    
    		LocalDateTime manana = LocalDateTime.now().plusDays(20);
    
    		EventoDTO evento1 = new EventoDTO(null, 0, lorem, manana, "Jáen", TipoEvento.NO_BENEFICO,
    				CategoriaEvento.DEPORTE, null, 0, 0, null, "Evento 1",
    				"https://cdn.discordapp.com/attachments/559336561934729217/707619803828846592/EVvyVijX0AMJdaq.png");
    
    		EventoDTO evento2 = new EventoDTO(null, 150, lorem, manana, "Jáen", TipoEvento.NO_BENEFICO,
    				CategoriaEvento.CHARLAS, null, 0, 0, null, "Evento 2",
    				"https://mymiddlec.files.wordpress.com/2013/09/empty-box.jpg");


			patricio = ige.getUsuario(ige.loginUsuario("Patricio Ruiz", "1234"));
			francisco = ige.getUsuario(ige.loginUsuario("Francisco López", "1234"));

		// En el caso de persistencia necesitamos borrar los eventos
		try {
			EventoDTO borraEvento = ige.listarEventosCreadosPorUnUsuario(patricio, 0, 10).get(0);
			ige.cancelarEventoPorUsuario(patricio, borraEvento.getIdEvento());

			borraEvento = ige.listarEventosCreadosPorUnUsuario(francisco, 0, 10).get(0);
			ige.cancelarEventoPorUsuario(francisco, borraEvento.getIdEvento());

			log.info("Eventos anteriores borrados");
		} catch (IndexOutOfBoundsException e) {
			log.info("Los eventos no existían");
		}

		log.info("Creando eventos");
		try {
			ige.crearEventoPorUsuario(patricio, evento1, true);
			ige.crearEventoPorUsuario(francisco, evento2, false);
			log.info("Eventos creados");
		} catch (EventoYaRegistrado e) {
			log.info("Eventos ya creados");
		} catch (EventoPrescrito e) {
			log.info("Eventos prescritos, recreando eventos");

			EventoDTO evento1 = new EventoDTO(null, 2, lorem, manana, "Jáen", TipoEvento.NO_BENEFICO,
					CategoriaEvento.DEPORTE, null, 0, 0, null, "Evento 1",
					"https://cdn.discordapp.com/attachments/559336561934729217/707619803828846592/EVvyVijX0AMJdaq.png");

			EventoDTO evento2 = new EventoDTO(null, 150, lorem, manana, "Jáen", TipoEvento.NO_BENEFICO,
					CategoriaEvento.CHARLAS, null, 0, 0, null, "Evento 2",
					"https://mymiddlec.files.wordpress.com/2013/09/empty-box.jpg");

			try {
				ige.crearEventoPorUsuario(patricio, evento1, true);
				ige.crearEventoPorUsuario(francisco, evento2, false);
				log.info("Eventos creados");
			} catch (EventoYaRegistrado e) {
				log.info("Eventos ya creados");
			} catch (EventoPrescrito e) {
				log.info("Eventos prescritos, recreando eventos");

				evento1 = ige.listarEventosCreadosPorUnUsuario(patricio, 0, 10).get(0);
				evento2 = ige.listarEventosCreadosPorUnUsuario(francisco, 0, 10).get(0);

				ige.cancelarEventoPorUsuario(patricio, evento1.getIdEvento());
				ige.cancelarEventoPorUsuario(francisco, evento2.getIdEvento());

				try {
					ige.crearEventoPorUsuario(patricio, evento1, true);
					ige.crearEventoPorUsuario(francisco, evento2, false);

					log.info("Eventos creados");
				} catch (Exception e2) {
					log.info("Error al volver a crear los eventos");
				}
			}
		}
	}
}
