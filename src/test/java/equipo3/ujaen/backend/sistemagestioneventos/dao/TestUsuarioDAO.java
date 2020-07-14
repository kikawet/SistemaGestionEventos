package equipo3.ujaen.backend.sistemagestioneventos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@ActiveProfiles("test")
@DataJpaTest
public class TestUsuarioDAO {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UsuarioDao usuarioDAO;

	Usuario generarUsuario() {
		Long login = new Random().nextLong();
		String loginUsuario = login.toString();
		String passwordUsuario = "123456789a.";
		String email = "flo00008@red.ujaen.es";

		Usuario u = new Usuario();

		u.setLogin(loginUsuario);

		return u;
	}

	@Test
	public void testFindByLogin() {

		Usuario u = generarUsuario();
		entityManager.persist(u);

		Usuario usuario = usuarioDAO.findByLogin(u.getLogin());
//		assertEquals(1, usuarios.size());

		assertThat(usuario).isNotNull();

//	        assertThat(books).extracting(Book::getName).containsOnly("C++");

	}
}
