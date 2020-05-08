package equipo3.ujaen.backend.sistemagestioneventos.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import equipo3.ujaen.backend.sistemagestioneventos.clientes.Cliente;

@SpringBootApplication(scanBasePackages = "equipo3.ujaen.backend.sistemagestioneventos.beans")
public class ServidorSistemaGestionEventos {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ServidorSistemaGestionEventos.class);
		ApplicationContext appContext = app.run(args);
		
		
		Cliente cliente = new Cliente(appContext);
		cliente.operar();
	}
}
