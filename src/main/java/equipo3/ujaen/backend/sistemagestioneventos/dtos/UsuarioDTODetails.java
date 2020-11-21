package equipo3.ujaen.backend.sistemagestioneventos.dtos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class UsuarioDTODetails implements UserDetails {

	private static final long serialVersionUID = -1160774859723575032L;

	private UsuarioDTO usuario;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Set<GrantedAuthority> _grntdAuths = new HashSet<GrantedAuthority>();

		if (usuario != null && usuario.getRol() != null) {
			_grntdAuths.add(new SimpleGrantedAuthority(usuario.getRol().toString()));
		}

		return _grntdAuths;
	}

	@Override
	public String getPassword() {
		return usuario == null ? null : usuario.getPassword();
	}

	@Override
	public String getUsername() {

		return usuario == null ? null : usuario.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
