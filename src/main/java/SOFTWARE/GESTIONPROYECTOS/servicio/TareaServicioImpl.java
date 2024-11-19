package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.EstadoTarea;
import SOFTWARE.GESTIONPROYECTOS.modelo.Tarea;
import SOFTWARE.GESTIONPROYECTOS.repositorio.TareaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaServicioImpl implements TareaServicio {

    private final TareaRepositorio tareaRepositorio;

    public TareaServicioImpl(TareaRepositorio tareaRepositorio) {
        this.tareaRepositorio = tareaRepositorio;
    }

    @Override
    public void guardarTarea(Tarea tarea) {
        tareaRepositorio.save(tarea);
    }

    @Override
    public void cambiarEstado(Long tareaId, EstadoTarea estado) {
        Tarea tarea = tareaRepositorio.findById(tareaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con ID: " + tareaId));
        tarea.setEstado(estado);
        tareaRepositorio.save(tarea);
    }

    @Override
    public List<Tarea> listarTareasPorProyecto(Long proyectoId) {
        return tareaRepositorio.findByProyectoId(proyectoId);
    }

    @Override
    public void eliminarTarea(Long tareaId) {
        if (tareaRepositorio.existsById(tareaId)) {
            tareaRepositorio.deleteById(tareaId);
        } else {
            throw new IllegalArgumentException("Tarea no encontrada con ID: " + tareaId);
        }
    }


    @Override
    public int calcularProgreso(Long proyectoId) {
        List<Tarea> tareas = tareaRepositorio.findByProyectoId(proyectoId);
        if (tareas.isEmpty()) {
            return 0; // Si no hay tareas, el progreso es 0%
        }

        long tareasCompletadas = tareas.stream()
                .filter(t -> t.getEstado() == EstadoTarea.COMPLETADA)
                .count();

        return (int) ((double) tareasCompletadas / tareas.size() * 100); // Calcula el porcentaje completado
    }

    @Override
    public List<Tarea> buscarTareas(Long proyectoId, String query) {
        return tareaRepositorio.findByProyectoIdAndDescripcionContainingIgnoreCase(proyectoId, query);
    }
}
