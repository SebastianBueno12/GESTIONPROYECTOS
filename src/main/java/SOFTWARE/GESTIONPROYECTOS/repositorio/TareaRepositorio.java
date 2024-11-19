package SOFTWARE.GESTIONPROYECTOS.repositorio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepositorio extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyectoId(Long proyectoId);

    List<Tarea> findByProyectoIdAndDescripcionContainingIgnoreCase(Long proyectoId, String descripcion);
}
