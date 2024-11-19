package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.servicio.ProyectoServicio;
import SOFTWARE.GESTIONPROYECTOS.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProyectoServicio proyectoServicio;

    @GetMapping("/panel")
    public String mostrarPanelCoordinador(Model model, Principal principal) {
        String email = principal.getName();
        Usuario coordinador = usuarioServicio.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Coordinador no encontrado"));
        model.addAttribute("proyectos", coordinador.getProyectosAsignados());
        return "coordinadorPanel";
    }

    // Nuevo método para listar los proyectos asignados al coordinador
    @GetMapping("/proyectos")
    public String listarProyectosCoordinador(Model model, Principal principal) {
        String email = principal.getName();
        Usuario coordinador = usuarioServicio.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Coordinador no encontrado"));

        List<Proyecto> proyectos = proyectoServicio.listarProyectosPorCoordinador(coordinador.getId());

        // Actualiza el estado dinámicamente
        proyectos.forEach(proyecto -> proyectoServicio.actualizarEstadoProyectoSiEsFinalizado(proyecto.getId()));

        model.addAttribute("proyectos", proyectos);
        return "proyectosCoordinador";
    }

}
