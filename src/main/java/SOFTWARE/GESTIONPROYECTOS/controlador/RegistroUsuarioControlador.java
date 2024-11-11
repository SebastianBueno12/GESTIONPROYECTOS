package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.controlador.dto.UsuarioRegistroDTO;
import SOFTWARE.GESTIONPROYECTOS.servicio.UsuarioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public RegistroUsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }

    @GetMapping
    public String mostrarFormularioDeRegistro() {
        return "registro";  // Retorna la vista del formulario de registro
    }

    @PostMapping
    public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO, Model model) {
        // Verificar si el correo ya existe
        if (usuarioServicio.findByEmail(registroDTO.getEmail()).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado. Intenta con otro.");
            return "registro";  // Volver a la vista de registro con el mensaje de error
        }

        // Registrar el usuario si el correo no existe
        usuarioServicio.guardar(registroDTO);
        model.addAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "redirect:/registro?exito";  // Redirige a la misma página con un parámetro de éxito
    }
}
