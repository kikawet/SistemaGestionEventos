package equipo3.ujaen.backend.sistemagestioneventos.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SistemaGestionEventosMvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		// WebMvcConfigurer.super.addViewControllers(registry);
		registry.addViewController("/").setViewName("index");

	}
}
