package SOFTWARE.GESTIONPROYECTOS.repositorio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, Long> {
}
