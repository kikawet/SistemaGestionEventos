package equipo3.ujaen.backend.sistemagestioneventos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@ActiveProfiles("test")
@DataJpaTest
class TestEventoDAO {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private EventoDao eventoDAO;

	@Test
	void testExistsByIdLong() {
		Evento e = new Evento();
		Usuario u = new Usuario();

		e.setCreador(u);
		e.setCategoriaEvento(CategoriaEvento.CHARLAS);
		entityManager.persist(u);
		entityManager.persist(e);

		assertThat(eventoDAO.existsById(e.getIdEvento())).isTrue();
	}
}
