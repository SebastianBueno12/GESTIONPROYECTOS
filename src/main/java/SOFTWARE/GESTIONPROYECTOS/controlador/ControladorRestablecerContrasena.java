package SOFTWARE.GESTIONPROYECTOS.controlador;


import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class ControladorRestablecerContrasena {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/restablecerContrasena")
    public String mostrarFormularioRestablecimiento(String token, Model model) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findByTokenRestablecimiento(token);

        if (usuarioOptional.isPresent()) {
            model.addAttribute("token", token);
            return "restablecerContrasena";
        } else {
            model.addAttribute("error", "Enlace de restablecimiento inválido.");
            return "restablecerContrasena";
        }
    }

    @PostMapping("/restablecerContrasena")
    public String procesarRestablecimientoContrasena(String token, String nuevaContrasena, String confirmarContrasena, Model model) {
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "restablecerContrasena";
        }

        Optional<Usuario> usuarioOptional = usuarioRepositorio.findByTokenRestablecimiento(token);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
            usuario.setTokenRestablecimiento(null);  // Elimina el token después de usarlo

            usuarioRepositorio.save(usuario);

            model.addAttribute("mensaje", "Contraseña restablecida con éxito. Ahora puede iniciar sesión.");
            return "redirect:/login?restablecimientoExitoso=true";
        } else {
            model.addAttribute("error", "Enlace de restablecimiento inválido.");
            return "restablecerContrasena";
        }
    }
}