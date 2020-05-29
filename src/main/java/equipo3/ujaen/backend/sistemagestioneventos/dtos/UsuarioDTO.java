package equipo3.ujaen.backend.sistemagestioneventos.dtos;

public class UsuarioDTO {

	// ROL = null = ningún rol
	public static enum RolUsuario {
		ADMIN
	}

	private RolUsuario rol;

	private String login;
	private String password;

	private Long uId;

	private int numEventosCreados;

	private int numEventosInscritos;

	public UsuarioDTO(String login, String password) {
		this(login, password, null, null, -1, -1);
	}

	public UsuarioDTO(String login, String password, Long uId, RolUsuario rol, int numEventosCreados,
			int numEventosInscritos) {
		super();
		this.rol = rol;
		this.login = login;
		this.password = password;
		this.uId = uId;
		this.numEventosCreados = numEventosCreados;
		this.numEventosInscritos = numEventosInscritos;
	}

	public RolUsuario getRol() {
		return rol;
	}

	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getuId() {
		return uId;
	}

	public void setuId(Long uId) {
		this.uId = uId;
	}

	public int getNumEventosCreados() {
		return numEventosCreados;
	}

	public void setNumEventosCreados(int numEventosCreados) {
		this.numEventosCreados = numEventosCreados;
	}

	public int getNumEventosInscritos() {
		return numEventosInscritos;
	}

	public void setNumEventosInscritos(int numEventosInscritos) {
		this.numEventosInscritos = numEventosInscritos;
	}

}
