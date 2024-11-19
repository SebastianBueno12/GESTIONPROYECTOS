package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ProyectoServicio {

    // Guarda un proyecto en la base de datos
    void guardarProyecto(Proyecto proyecto);

    // Lista proyectos paginados
    Page<Proyecto> listarProyectos(PageRequest pageRequest);

    // Obtiene un proyecto por su ID
    Optional<Proyecto> obtenerProyectoPorId(Long id);

    Optional<Proyecto> obtenerPorId(Long id);

    // Actualiza el estado de un proyecto a "FINALIZADO" si el progreso es del 100%
    void actualizarEstadoProyectoSiEsFinalizado(Long proyectoId);

    // Calcula el progreso de un proyecto basado en sus tareas
    int calcularProgreso(Long proyectoId);

    List<Proyecto> listarProyectosPorCoordinador(Long coordinadorId);
}


