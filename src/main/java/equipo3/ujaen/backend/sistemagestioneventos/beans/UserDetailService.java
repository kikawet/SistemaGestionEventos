package equipo3.ujaen.backend.sistemagestioneventos.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import equipo3.ujaen.backend.sistemagestioneventos.dao.UsuarioDao;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO.RolUsuario;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	UsuarioDao usuarioDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByLogin(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		UserDetails userDetails = User.withUsername(username).password(usuario.getPassword())
				.roles(this.getRoles(usuario)).build();
		return userDetails;

	}

	public String[] getRoles(Usuario u) {

		if (u.getRol() == null) {
			return new String[0];
		} else {
			return new String[] { u.getRol().toString() };
		}
	}

}
