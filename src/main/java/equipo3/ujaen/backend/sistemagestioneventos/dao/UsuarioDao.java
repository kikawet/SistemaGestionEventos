package equipo3.ujaen.backend.sistemagestioneventos.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import equipo3.ujaen.backend.sistemagestioneventos.entidades.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long> {
	
}








