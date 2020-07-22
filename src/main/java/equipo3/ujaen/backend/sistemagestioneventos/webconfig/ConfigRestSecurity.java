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
	http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	http.httpBasic().authenticationEntryPoint(authenticationEntryPoint());

	String uriRestEvento = "/rest/evento";
	String uriRestUsuario = "/rest/usuario";

	http.antMatcher("/rest/**")
	.authorizeRequests()
	.antMatchers(HttpMethod.GET, uriRestEvento + "/**").permitAll()
	.antMatchers(uriRestUsuario + "/login", uriRestUsuario + "/registro", uriRestUsuario + "/ping").permitAll()
	.anyRequest().authenticated();
    }

}
