package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProyectoServicio {
    Proyecto guardarProyecto(Proyecto proyecto);
    Optional<Proyecto> obtenerProyectoPorId(Long id);
    Page<Proyecto> listarProyectos(Pageable pageable);
    // Otros métodos necesarios...
}
