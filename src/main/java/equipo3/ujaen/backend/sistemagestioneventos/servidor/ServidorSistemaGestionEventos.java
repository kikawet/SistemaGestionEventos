package equipo3.ujaen.backend.sistemagestioneventos.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;

@EntityScan(basePackages = "equipo3.ujaen.backend.sistemagestioneventos.entidades")
@SpringBootApplication(scanBasePackages = "equipo3.ujaen.backend.sistemagestioneventos.beans")
public class ServidorSistemaGestionEventos {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ServidorSistemaGestionEventos.class);
		ApplicationContext appContext = app.run(args);
			
		System.out.println("Saludos desde el servidor");
	}
}
