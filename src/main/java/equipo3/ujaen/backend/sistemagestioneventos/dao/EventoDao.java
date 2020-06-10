package equipo3.ujaen.backend.sistemagestioneventos.dao;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;

@Repository
public interface EventoDao extends JpaRepository<Evento, Long> {
	List<Evento> findByDescripcionContainsIgnoreCase(String descripcion);
	List<Evento> findByStartDateAfter(LocalDateTime fecha);
	List<Evento> findByFirstnameEquals(EventoDTO.CategoriaEvento categoriaEvento);
	
}
