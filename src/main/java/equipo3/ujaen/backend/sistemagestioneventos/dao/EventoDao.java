package equipo3.ujaen.backend.sistemagestioneventos.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;

@Repository
public interface EventoDao extends JpaRepository<Evento, Long> {
	List<Evento> findByCategoriaEventoAndDescripcionContainsIgnoreCase(CategoriaEvento categoria,
			String descripcionParcial, Pageable cantidad);

	@Override
	boolean existsById(Long idEvento);

}
