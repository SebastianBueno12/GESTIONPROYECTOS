package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.EstadoProyecto;
import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.servicio.ProyectoServicio;
import SOFTWARE.GESTIONPROYECTOS.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class  ProyectoController {

    @Autowired
    private ProyectoServicio proyectoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/admin/projects/new")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "registroProyecto";
    }

    @PostMapping("/admin/projects")
    public String registrarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoServicio.guardarProyecto(proyecto);
        return "redirect:/admin/projects";
    }

    @GetMapping("/admin/projects")
    public String listarProyectos(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Proyecto> proyectos = proyectoServicio.listarProyectos(PageRequest.of(page, 3));
        model.addAttribute("proyectos", proyectos);
        return "listaProyectos";
    }


    @GetMapping("/admin/projects/updateStatus")
    public String mostrarFormularioActualizarEstado(@RequestParam("id") Long id, Model model) {
        Optional<Proyecto> proyecto = proyectoServicio.obtenerProyectoPorId(id);
        if (proyecto.isPresent()) {
            model.addAttribute("proyecto", proyecto.get());
            return "actualizarEstado";
        }
        model.addAttribute("error", "Proyecto no encontrado");
        return "redirect:/admin/projects";
    }
    @PostMapping("/admin/projects/updateStatus")
    public String actualizarEstado(@RequestParam("id") Long id, @RequestParam("estado") String estado) {
        Optional<Proyecto> proyectoOpt = proyectoServicio.obtenerProyectoPorId(id);

        if (proyectoOpt.isPresent()) {
            Proyecto proyectoActualizado = proyectoOpt.get();

            try {
                proyectoActualizado.setEstado(EstadoProyecto.valueOf(estado)); // Conversión de String a enum
                proyectoServicio.guardarProyecto(proyectoActualizado);
            } catch (IllegalArgumentException e) {
                // Manejar error si el estado no es válido
                return "redirect:/admin/projects?error=EstadoInvalido";
            }
        } else {
            return "redirect:/admin/projects?error=ProyectoNoEncontrado";
        }

        return "redirect:/admin/projects";
    }

    @GetMapping("/admin/projects/assign")
    public String mostrarFormularioAsignacion(@RequestParam("id") Long id, Model model) {
        Optional<Proyecto> proyectoOpt = proyectoServicio.obtenerProyectoPorId(id);
        if (proyectoOpt.isPresent()) {
            model.addAttribute("proyecto", proyectoOpt.get());
            model.addAttribute("coordinadores", usuarioServicio.listarCoordinadoresDisponibles());
            return "asignarCoordinador";
        }
        model.addAttribute("error", "Proyecto no encontrado");
        return "redirect:/admin/projects";
    }

    @PostMapping("/admin/projects/assign")
    public String asignarCoordinador(@RequestParam("proyectoId") Long proyectoId, @RequestParam("coordinadorId") Long coordinadorId) {
        Optional<Proyecto> proyectoOpt = proyectoServicio.obtenerProyectoPorId(proyectoId);
        if (proyectoOpt.isPresent()) {
            Proyecto proyecto = proyectoOpt.get();
            Usuario coordinador = usuarioServicio.obtenerUsuarioPorId(coordinadorId);

            if (coordinador != null) {
                proyecto.setCoordinador(coordinador);
                proyectoServicio.guardarProyecto(proyecto);
            }
        }
        return "redirect:/admin/projects";

    }



}