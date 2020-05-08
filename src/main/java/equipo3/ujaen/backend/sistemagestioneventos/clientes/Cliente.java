package equipo3.ujaen.backend.sistemagestioneventos.clientes;

import org.springframework.context.ApplicationContext;

import equipo3.ujaen.backend.sistemagestioneventos.excepciones.UsuarioYaRegistrado;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;
import equipo3.ujaen.backend.sistemagestioneventos.utils.ConsoleUtils;
import equipo3.ujaen.backend.sistemagestioneventos.utils.Menu;
import equipo3.ujaen.backend.sistemagestioneventos.utils.MenuItem;

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
	
	public void operar() {
		
		Menu menuPrincipal = new Menu("Salir", "Salir");
		
		menuPrincipal.setTitle("Cliente Sistema de gestion de eventos para curso JEE");
		
		menuPrincipal.addItem(new MenuItem("Registrar usuario: ", this::registrarUsuario));
		
		menuPrincipal.execute();
	}
	
}
