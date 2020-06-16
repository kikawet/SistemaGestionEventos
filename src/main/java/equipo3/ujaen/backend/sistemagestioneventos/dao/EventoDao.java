package equipo3.ujaen.backend.sistemagestioneventos.dao;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;

@Repository
public interface EventoDao extends JpaRepository<Evento, Long> {
	// https://jira.spring.io/browse/DATAJPA-209
	// Hay que crear dos veces el mismo método o crear un @Query
	@Cacheable({ "buscaEvento" })
	List<Evento> findByCategoriaEventoAndDescripcionContainsIgnoreCase(CategoriaEvento categoria,
			String descripcionParcial, Pageable cantidad);

	@Cacheable({ "buscaEvento" })
	List<Evento> findByDescripcionContainsIgnoreCase(String descripcionParcial, Pageable cantidad);

	@Query("select e from Evento e join fetch e.asistentes where e.idEvento=?1")
	Evento findByIdEventoFetchingAsistentes(Long idEvento);

	@Query("select e from Evento e join fetch e.listaEspera where e.idEvento=?1")
	Evento findByIdEventoFetchingListaEspera(Long idEvento);

	@Override
	@Caching(evict = { @CacheEvict("existeEvento"), @CacheEvict(value = "buscaEvento"/* ,allEntries = true */) })
	void deleteById(Long idEvento);

	@Override
	@Cacheable({ "existeEvento" })
	boolean existsById(Long idEvento);

}
