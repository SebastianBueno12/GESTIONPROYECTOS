package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import SOFTWARE.GESTIONPROYECTOS.servicio.ProyectoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProyectoController {

    @Autowired
    private ProyectoServicio proyectoServicio;

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
        Page<Proyecto> proyectos = proyectoServicio.listarProyectos(PageRequest.of(page, 10)); // 10 proyectos por página
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
        Optional<Proyecto> proyecto = proyectoServicio.obtenerProyectoPorId(id);
        if (proyecto.isPresent()) {
            Proyecto proyectoActualizado = proyecto.get();
            proyectoActualizado.setEstado(estado);
            proyectoServicio.guardarProyecto(proyectoActualizado);
        }
        return "redirect:/admin/projects";
    }
}
