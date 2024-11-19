package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.EstadoTarea;
import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import SOFTWARE.GESTIONPROYECTOS.modelo.Tarea;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.repositorio.TareaRepositorio;
import SOFTWARE.GESTIONPROYECTOS.servicio.ProyectoServicio;
import SOFTWARE.GESTIONPROYECTOS.servicio.TareaServicio;
import SOFTWARE.GESTIONPROYECTOS.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coordinador/tareas")
public class TareaController {

    @Autowired
    private TareaServicio tareaServicio;
    @Autowired
    private TareaRepositorio tareaRepositorio;

    @Autowired
    private ProyectoServicio proyectoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/{proyectoId}")
    public String listarTareas(@PathVariable Long proyectoId, Model model) {
        List<Tarea> tareas = tareaServicio.listarTareasPorProyecto(proyectoId);

        Proyecto proyecto = proyectoServicio.obtenerProyectoPorId(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        int progreso = tareaServicio.calcularProgreso(proyectoId);

        model.addAttribute("tareas", tareas);
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("progreso", progreso);
        return "Tareas"; // Nombre de la plantilla Thymeleaf
    }


    @GetMapping("/nueva/{proyectoId}")
    public String mostrarFormularioNuevaTarea(@PathVariable Long proyectoId, Model model) {
        Tarea nuevaTarea = new Tarea();
        Proyecto proyecto = proyectoServicio.obtenerProyectoPorId(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        model.addAttribute("tarea", nuevaTarea);
        model.addAttribute("proyectoId", proyectoId);
        model.addAttribute("empleados", usuarioServicio.listarEmpleados());
        return "nuevaTarea"; // Nombre de la plantilla Thymeleaf
    }

    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea, @RequestParam Long proyectoId, @RequestParam Long empleadoId) {
        Proyecto proyecto = proyectoServicio.obtenerProyectoPorId(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        Usuario empleado = usuarioServicio.obtenerUsuarioPorId(empleadoId);

        tarea.setProyecto(proyecto);
        tarea.setEmpleado(empleado); // Asigna el empleado a la tarea
        tarea.setEstado(EstadoTarea.PENDIENTE); // Estado inicial

        tareaServicio.guardarTarea(tarea);
        return "redirect:/coordinador/tareas/" + proyectoId;
    }



    @PostMapping("/{proyectoId}/estado/{tareaId}/{estado}")
    public String cambiarEstado(@PathVariable Long proyectoId,
                                @PathVariable Long tareaId,
                                @PathVariable String estado) {
        try {
            EstadoTarea nuevoEstado = EstadoTarea.valueOf(estado.toUpperCase());
            tareaServicio.cambiarEstado(tareaId, nuevoEstado);

            // Verifica y actualiza dinámicamente el estado del proyecto
            proyectoServicio.actualizarEstadoProyectoSiEsFinalizado(proyectoId);

            return "redirect:/coordinador/tareas/" + proyectoId;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + estado, e);
        }
    }




    @GetMapping("/{proyectoId}/eliminar/{tareaId}")
    public String eliminarTarea(@PathVariable Long proyectoId, @PathVariable Long tareaId) {
        if (tareaRepositorio.existsById(tareaId)) {  // Verifica si la tarea existe
            tareaServicio.eliminarTarea(tareaId);
        } else {
            throw new RuntimeException("Tarea no encontrada con ID: " + tareaId);
        }
        return "redirect:/coordinador/tareas/" + proyectoId;
    }

}
