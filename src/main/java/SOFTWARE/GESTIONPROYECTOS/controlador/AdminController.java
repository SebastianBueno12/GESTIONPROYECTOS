package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/panel")
    public String mostrarPanelAdmin(Model model) {
        // Agrega cualquier atributo que desees pasar a la vista, si es necesario
        return "adminPanel";  // Carga la vista adminPanel.html
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/usuarios")
    public String gestionarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioServicio.listarUsuarios());
        return "gestionUsuarios";  // Vista de gestión de usuarios
    }

    // Inyección de UsuarioServicio
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/actualizar/{id}")
    public String mostrarFormularioDeActualizacion(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "actualizarUsuario";  // Nombre de la vista que muestra el formulario de actualización
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuarioActualizado) {
        usuarioServicio.actualizarUsuario(id, usuarioActualizado);
        return "redirect:/?actualizado";  // Redirige después de actualizar
    }
}
