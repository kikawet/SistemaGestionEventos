package equipo3.ujaen.backend.sistemagestioneventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = ServidorSistemaGestionEventos.basePackageRoot + ".entidades")
@EnableJpaRepositories(basePackages = ServidorSistemaGestionEventos.basePackageRoot + ".dao")
@EnableCaching
public class ServidorSistemaGestionEventos {

	public static final String basePackageRoot = "equipo3.ujaen.backend.sistemagestioneventos";

	public static void main(String[] args) {
		SpringApplication.run(ServidorSistemaGestionEventos.class, args);

		System.out.println("Saludos desde el servidor");
	}
}
