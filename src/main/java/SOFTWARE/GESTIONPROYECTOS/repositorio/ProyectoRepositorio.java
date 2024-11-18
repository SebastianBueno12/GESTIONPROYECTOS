package SOFTWARE.GESTIONPROYECTOS.repositorio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByCoordinadorId(Long coordinadorId);
}
