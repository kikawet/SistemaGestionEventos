package equipo3.ujaen.backend.sistemagestioneventos.dao;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@Repository
public interface EventoDao extends JpaRepository<Evento, Long> {
	// https://jira.spring.io/browse/DATAJPA-209
	// Hay que crear dos veces el mismo método o crear un @Query
	// @Cacheable({ "buscaEvento" })
	List<Evento> findByCategoriaEventoAndDescripcionContainsIgnoreCaseAndTituloContainsIgnoreCase(
			CategoriaEvento categoria, String descripcionParcial, String titulo, Pageable cantidad);

	// @Cacheable({ "buscaEvento" })
	List<Evento> findByDescripcionContainsIgnoreCaseAndTituloContainsIgnoreCase(String descripcionParcial,
			String titulo, Pageable pageable);

	@Query("select e from Evento e join fetch e.asistentes where e.idEvento=?1")
	Evento findByIdEventoFetchingAsistentes(Long idEvento);

	@Query("select e from Evento e join fetch e.listaEspera where e.idEvento=?1")
	Evento findByIdEventoFetchingListaEspera(Long idEvento);

	@Override
	@Caching(evict = { @CacheEvict("existeEvento"),
			@CacheEvict(value = { "buscaEvento", "buscaEvento" }, allEntries = true) })
	void deleteById(Long idEvento);

	@Override
	// @Cacheable({ "existeEvento" })
	boolean existsById(Long idEvento);

	@SuppressWarnings("unchecked")
	@Override
	// @CacheEvict(value = { "buscaEvento", "buscaEvento" }, allEntries = true)
	Evento save(Evento evento);

	@SuppressWarnings("unchecked")
	@Override
	// @CacheEvict(value = { "buscaEvento", "buscaEvento" }, allEntries = true)
	Evento saveAndFlush(Evento evento);

	@Query("select distinct e from Evento e where ?1 member of e.asistentes")
	List<Evento> findByIdUsuarioWhereUsuarioIsInscrito(Usuario usuario, Pageable cantidad);

	@Query("select distinct e from Evento e where ?1 member of e.listaEspera")
	List<Evento> findByIdUsuarioWhereUsuarioIsEsperando(Usuario usuario, Pageable cantidad);

}
