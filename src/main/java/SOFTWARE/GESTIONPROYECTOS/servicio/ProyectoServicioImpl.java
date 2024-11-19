package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.EstadoProyecto;
import SOFTWARE.GESTIONPROYECTOS.modelo.EstadoTarea;
import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import SOFTWARE.GESTIONPROYECTOS.modelo.Tarea;
import SOFTWARE.GESTIONPROYECTOS.repositorio.ProyectoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServicioImpl implements ProyectoServicio {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Override
    public void guardarProyecto(Proyecto proyecto) {
        proyectoRepositorio.save(proyecto);
    }

    @Override
    public Page<Proyecto> listarProyectos(PageRequest pageRequest) {
        return proyectoRepositorio.findAll(pageRequest);
    }

    @Override
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return proyectoRepositorio.findById(id);
    }
    @Override
    public Optional<Proyecto> obtenerPorId(Long id) {
        return proyectoRepositorio.findById(id);
    }

    @Override
    public void actualizarEstadoProyectoSiEsFinalizado(Long proyectoId) {
        Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        int progreso = calcularProgreso(proyectoId);

        if (progreso == 100) {
            proyecto.setEstado(EstadoProyecto.FINALIZADO);
        } else if (progreso > 0) {
            proyecto.setEstado(EstadoProyecto.EN_PROGRESO);
        } else {
            proyecto.setEstado(EstadoProyecto.PENDIENTE);
        }

        proyectoRepositorio.save(proyecto);
    }


    @Override
    public int calcularProgreso(Long proyectoId) {
        Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        List<Tarea> tareas = proyecto.getTareas();
        if (tareas.isEmpty()) {
            return 0; // Si no hay tareas, el progreso es 0
        }

        long tareasCompletadas = tareas.stream()
                .filter(t -> t.getEstado() == EstadoTarea.COMPLETADA)
                .count();

        return (int) ((tareasCompletadas * 100) / tareas.size());
    }
    @Override
    public List<Proyecto> listarProyectosPorCoordinador(Long coordinadorId) {
        return proyectoRepositorio.findByCoordinadorId(coordinadorId);
    }

}

