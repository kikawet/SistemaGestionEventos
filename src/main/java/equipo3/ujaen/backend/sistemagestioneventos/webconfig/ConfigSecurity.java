package equipo3.ujaen.backend.sistemagestioneventos.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@Order(2)
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService uds;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	//		auth.inMemoryAuthentication().withUser("usuario").password("{noop}secreto").roles().and().withUser("admin")
	//				.password(encoder.encode("secreto")).roles(UsuarioDTO.RolUsuario.ADMIN.toString());

	auth.userDetailsService(uds);
    }

    @Value("${remember-me.key}")
    private static String rememberMeKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.authorizeRequests()
	.antMatchers("/usuario/registro").not().authenticated()
	.antMatchers("/usuario/**").authenticated()
	.antMatchers("/evento/**").authenticated()
	.antMatchers("/inicio/**", "/").permitAll().anyRequest().denyAll()
	.and()
	.formLogin().defaultSuccessUrl("/").loginPage("/usuario/login").permitAll()
	.and()
	.logout().logoutSuccessUrl("/")
	.deleteCookies("JSESSIONID").and().rememberMe().key(rememberMeKey);
    }
}
