package equipo3.ujaen.backend.sistemagestioneventos.webconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@Order(1)
public class ConfigRestSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
	BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
	entryPoint.setRealmName("club api");
	return entryPoint;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

	//		http.antMatcher("/rest/**").authorizeRequests()
	//				.antMatchers(HttpMethod.GET, "/**/ping").permitAll()
	//				.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
	//				.antMatchers(HttpMethod.GET, "/**/evento").permitAll()
	//				.antMatchers(HttpMethod.DELETE, "/evento/**")
	//				.access("#uId == principal.usuario.uId or hasRole('ADMIN')").antMatchers(HttpMethod.POST, "/evento/**")
	//				.access("#uId == principal.usuario.uId or hasRole('ADMIN')").antMatchers("/usuario/registro")
	//				.permitAll().antMatchers("/usuario/login").permitAll().antMatchers("/usuario/**")
	//				.access("#id == principal.usuario.uId").anyRequest().authenticated().and().httpBasic()
	//				.authenticationEntryPoint(authenticationEntryPoint()).and().sessionManagement()
	//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and().csrf().disable();

	http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	http.httpBasic().authenticationEntryPoint(authenticationEntryPoint());

	String uriRestEvento = "/rest/evento";
	String uriRestUsuario = "/rest/usuario";

	http.authorizeRequests()
	.antMatchers(HttpMethod.GET, uriRestEvento).permitAll()
	.antMatchers(uriRestUsuario+"/login", uriRestUsuario+"/registro", uriRestUsuario+"/ping").permitAll()
	.anyRequest().authenticated();
    }

}
