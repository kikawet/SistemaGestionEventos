package equipo3.ujaen.backend.sistemagestioneventos.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTODetails;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	InterfaceSistemaGestionEventos sge;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UsuarioDTO usuario = sge.getUsuarioLogin(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		return new UsuarioDTODetails(usuario);
	}

	public String[] getRoles(Usuario u) {

		if (u.getRol() == null) {
			return new String[0];
		} else {
			return new String[] { u.getRol().toString() };
		}
	}

}
