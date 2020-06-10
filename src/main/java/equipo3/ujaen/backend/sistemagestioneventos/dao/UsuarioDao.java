package equipo3.ujaen.backend.sistemagestioneventos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import equipo3.ujaen.backend.sistemagestioneventos.entidades.Evento;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long> {
	boolean existsByLogin(String login);

	Usuario findByLogin(String login);

	List<Evento> findAllEventosInscritosByLogin(String login);

	List<Evento> findAllEventosCreadosByLogin(String login);

}
