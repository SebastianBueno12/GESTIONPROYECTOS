package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.EstadoTarea;
import SOFTWARE.GESTIONPROYECTOS.modelo.Tarea;

import java.util.List;

public interface TareaServicio {
    void guardarTarea(Tarea tarea);

    void cambiarEstado(Long tareaId, EstadoTarea estado);

    List<Tarea> listarTareasPorProyecto(Long proyectoId);

    void eliminarTarea(Long tareaId);

    int calcularProgreso(Long proyectoId);

    List<Tarea> buscarTareas(Long proyectoId, String query);
}
