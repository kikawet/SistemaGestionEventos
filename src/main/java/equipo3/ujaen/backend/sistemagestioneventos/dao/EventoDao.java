package equipo3.ujaen.backend.sistemagestioneventos.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@Repository
public interface EventoDao extends JpaRepository<Evento, Long> {
	// https://jira.spring.io/browse/DATAJPA-209
	// Hay que crear dos veces el mismo método o crear un @Query
	List<Evento> findByCategoriaEventoAndDescripcionContainsIgnoreCase(CategoriaEvento categoria,
			String descripcionParcial, Pageable cantidad);

	List<Evento> findByDescripcionContainsIgnoreCase(String descripcionParcial, Pageable cantidad);

	List<Usuario> findAllAsistentesById(Long idEvento);

	List<Usuario> findAllListaEsperaById(Long idEvento);

	@Override
	boolean existsById(Long idEvento);

}
