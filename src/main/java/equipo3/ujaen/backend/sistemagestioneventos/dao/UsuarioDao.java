package equipo3.ujaen.backend.sistemagestioneventos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long> {
	boolean existsByLogin(String login);

	Usuario findByLogin(String login);

	@Query("select u from Usuario u join fetch u.eventosInscritos where u.login=?1")
	Usuario findByLoginFetchingInscritos(String login);

	@Query("select u from Usuario u join fetch u.eventosCreados where u.login=?1")
	Usuario findByLoginFetchingCreados(String login);

}
