package equipo3.ujaen.backend.sistemagestioneventos.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		auth.inMemoryAuthentication().withUser("usuario").password("{noop}secreto").roles("USUARIOS").and()
				.withUser("admin").password(encoder.encode("secreto")).roles("ADMINISTRADORES");
	}

	
	
	@Override
	protected void configure( HttpSecurity http) throws Exception {	
		http.
		 authorizeRequests()	 
		 .antMatchers("/usuario/registro").not().authenticated()
		 .antMatchers("/usuario/**").authenticated()
		 .antMatchers("/inicio/**","/").permitAll()
		 .anyRequest().denyAll()		 
		 .and().formLogin().defaultSuccessUrl("/").loginPage("/usuario/login").permitAll();
	}
}
