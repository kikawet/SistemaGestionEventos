package equipo3.ujaen.backend.sistemagestioneventos.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@Repository
/**
 * Como en el sistema no se borran usuarios cachear al validar y borrarlo al
 * guardar no tiene sentido, pero tampoco hay una opción mejor para liberar la
 * cache, por lo tanto se descarta usar cache para usuarios
 *
 * @author flo00008
 *
 */
public interface UsuarioDao extends JpaRepository<Usuario, Long> {
//	@Cacheable({ "existeUsuario" })
	boolean existsByLogin(String login);

//	@Cacheable({ "buscaUsuario" })
	Usuario findByLogin(String login);

	@Query("select u from Usuario u join fetch u.eventosInscritos where u.login=?1")
	Usuario findByLoginFetchingInscritos(String login, Pageable pageable);

	@Query("select u from Usuario u join fetch u.eventosCreados where u.login=?1")
	Usuario findByLoginFetchingCreados(String login, Pageable pageable);

	@SuppressWarnings("unchecked")
	@Override
//	@Caching(evict = { @CacheEvict("existeUsuario"), @CacheEvict("buscaUsuario") })
	Usuario save(Usuario usuario);

}
