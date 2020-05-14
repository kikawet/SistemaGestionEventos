package equipo3.ujaen.backend.sistemagestioneventos.clientes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento.TipoEvento;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.EventoYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioNoRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;
import equipo3.ujaen.backend.sistemagestioneventos.utils.ConsoleUtils;
import equipo3.ujaen.backend.sistemagestioneventos.utils.Menu;
import equipo3.ujaen.backend.sistemagestioneventos.utils.MenuItem;
import equipo3.ujaen.backend.sistemagestioneventos.utils.Pair;

public class Cliente {

	private InterfaceSistemaGestionEventos servicios;

	public Cliente() {
		// TODO Auto-generated constructor stub
	}

	public Cliente(ApplicationContext context) {
		servicios = context.getBean(InterfaceSistemaGestionEventos.class);
	}

	public void registrarUsuario() {
		String login = ConsoleUtils.getStringField("Introducir login de usuario");
		String password = ConsoleUtils.getStringField("Introduzca la contraseña que desea");

		try {
			servicios.registroUsuarios(login, password);
		} catch (UsuarioYaRegistrado e) {
			System.err.println("Usuario ya registrado en el sistema");
		}

	}

	public void crearEvento() {
		/*
		 * Evento evento = new
		 * Evento(ConsoleUtils.getStringField("Introducir lugar del evento"),
		 * ConsoleUtils.getDateField("Introducir fecha del evento"), () -> { int
		 * tipoEvento = 0; do { if(tipoEvento == 1) return TipoEvento.BENEFICO;
		 * if(tipoEvento == 2) return TipoEvento.NO_BENEFICO; } while(tipoEvento != 1 ||
		 * tipoEvento != 2);},
		 * ConsoleUtils.getStringField("Introducir descripcion del evento"),
		 * ConsoleUtils.getIntField("Introducir aforo del evento"),
		 * ConsoleUtils.getStringField("Introducir id del evento"));
		 */
	}

	public void datosDePrueba() {
		
		try {
			servicios.crearEvento(
					new Evento("1", Date.from(Instant.now()), TipoEvento.BENEFICO, "desc", 0, "idEvento1"));
			servicios.crearEvento(
					new Evento("2", Date.from(Instant.now()), TipoEvento.BENEFICO, "desc", 1, "idEvento2"));

			servicios.registroUsuarios("123", "123");
			servicios.inscribirUsuario("123", "idEvento1");
			servicios.inscribirUsuario("123", "idEvento2");
			servicios.inscribirUsuario("1234", "idEvento2");
		} catch (UsuarioYaRegistrado | UsuarioNoRegistrado | EventoYaRegistrado | EventoNoRegistrado e) {

		}
	}

	public void listarEventosPorUsuario() {
		String login = ConsoleUtils.getStringField("Introduzca el login del usuario");

		List<Pair<Boolean, Evento>> eventosUsuario = new ArrayList<>();

		try {
			eventosUsuario = servicios.listarEventosDeUnUsuario(login);

			for (Pair<Boolean, Evento> e : eventosUsuario) {
				if (e.getElement0().booleanValue() == true) {
					System.out.println("El usuario asistirá al evento " + e.getElement1().getIdEvento());
				} else {
					System.out.println(
							"El usuario está en lista de espera en el evento " + e.getElement1().getIdEvento());
				}
			}

		} catch (UsuarioNoRegistrado e) {
			System.err.println("Usuario no registrado en el sistema");
		}
	}

	public void operar() {

		Menu menuPrincipal = new Menu("Salir", "Salir");

		menuPrincipal.setTitle("Cliente Sistema de gestion de eventos para curso JEE");

		menuPrincipal.addItem(new MenuItem("Registrar usuario: ", this::registrarUsuario));
		menuPrincipal.addItem(new MenuItem("Datos de prueba: ", this::datosDePrueba));
		menuPrincipal.addItem(new MenuItem("Listar Eventos: ", this::listarEventosPorUsuario));

		menuPrincipal.execute();
	}

}
