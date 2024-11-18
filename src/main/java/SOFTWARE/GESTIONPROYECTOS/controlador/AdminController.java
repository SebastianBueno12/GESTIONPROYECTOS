package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.Rol;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.servicio.RolServicio;
import SOFTWARE.GESTIONPROYECTOS.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private RolServicio rolServicio;

    // Panel de administración
    @GetMapping("/panel")
    public String mostrarPanelAdmin(Model model) {
        return "adminPanel";  // Carga la vista adminPanel.html
    }

    // Listar todos los usuarios
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String gestionarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioServicio.listarUsuarios());
        return "gestionUsuarios";  // Vista de gestión de usuarios
    }

    // Ver detalles de un usuario
    @GetMapping("/usuarios/detalle/{id}")
    public String verDetalles(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "detalleUsuario";  // Nombre de la vista para detalles del usuario
    }

    // Mostrar formulario para editar un usuario
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);
        List<Rol> roles = rolServicio.listarRoles();
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);
        return "editarUsuario";  // Nombre de la vista para editar usuario
    }

    // Guardar cambios de un usuario, incluyendo rol y datos personales
    @PostMapping("/usuarios/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuarioActualizado, @RequestParam("rol") String nombreRol) {
        usuarioServicio.actualizarUsuario(id, usuarioActualizado, nombreRol);
        return "redirect:/admin/usuarios?actualizado";  // Redirigir después de actualizar
    }

    // Eliminar un usuario
    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        usuarioServicio.eliminarUsuario(id);
        return "redirect:/admin/usuarios?eliminado";  // Redirigir después de eliminar
    }

    @GetMapping ("/usuarios/contratar/{id}")
    public String contratarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);

        // Verificar si el usuario ya está contratado
        if (usuario.getFechaContratacion() != null) {
            redirectAttributes.addFlashAttribute("error", "El usuario ya está contratado.");
            return "redirect:/admin/usuarios";
        }

        // Solo actualiza la fecha de contratación y el rol
        usuario.setFechaContratacion(new Date());

        Rol rolEmpleado = rolServicio.listarRoles().stream()
                .filter(rol -> "ROLE_EMPLEADO".equals(rol.getNombre()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rol EMPLEADO no encontrado"));
        usuario.getRoles().clear();
        usuario.getRoles().add(rolEmpleado);

        // Guardar cambios sin tocar otros campos
        usuarioServicio.actualizarUsuarioSinCambiarPassword(usuario);

        redirectAttributes.addFlashAttribute("success", "Usuario contratado exitosamente.");
        return "redirect:/admin/usuarios";
    }
    // Mostrar formulario de registro de usuario
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registroUsuario";
    }
    @PostMapping("/usuarios/registrar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioServicio.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/usuarios/nuevo"; // Redirigir al formulario de registro en caso de error
        }
        return "redirect:/admin/usuarios";
    }



}
