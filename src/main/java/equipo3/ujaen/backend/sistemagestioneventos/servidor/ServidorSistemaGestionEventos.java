package equipo3.ujaen.backend.sistemagestioneventos.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "equipo3.ujaen.backend.sistemagestioneventos.beans")
@EntityScan(basePackages = "equipo3.ujaen.backend.sistemagestioneventos.entidades")
@EnableJpaRepositories(basePackages = "equipo3.ujaen.backend.sistemagestioneventos.dao")
public class ServidorSistemaGestionEventos {

	public static void main(String[] args) {
		SpringApplication.run(ServidorSistemaGestionEventos.class, args);

		System.out.println("Saludos desde el servidor");
	}
}
