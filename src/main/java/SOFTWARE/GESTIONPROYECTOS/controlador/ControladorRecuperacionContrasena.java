package SOFTWARE.GESTIONPROYECTOS.controlador;

import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.repositorio.UsuarioRepositorio;
import SOFTWARE.GESTIONPROYECTOS.servicio.ServicioCorreo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.Optional;
import java.util.UUID;

@Controller
public class ControladorRecuperacionContrasena {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ServicioCorreo servicioCorreo;

    // Mostrar formulario para solicitar el restablecimiento de contraseña
    @GetMapping("/recuperarContrasena")
    public String mostrarFormularioRestablecimiento() {
        return "recuperarContrasena";
    }

    // Procesar la solicitud de restablecimiento de contraseña
    @PostMapping("/recuperarContrasena")
    public String procesarSolicitudRestablecimiento(String correo, Model model) {
        // Buscar el usuario por correo
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findByEmail(correo);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Generar un token único para restablecimiento de contraseña
            String token = UUID.randomUUID().toString();
            usuario.setTokenRestablecimiento(token);

            // Guardar el token en la base de datos
            usuarioRepositorio.save(usuario);

            // Crear el enlace de restablecimiento de contraseña
            String enlaceRestablecimiento = "http://localhost:3030/restablecerContrasena?token=" + token;

            // Enviar el enlace de restablecimiento de contraseña por correo
            servicioCorreo.enviarCorreo(
                    usuario.getEmail(),
                    "Restablecer Contraseña - Club Buena Vida",
                    "Para restablecer su contraseña, haga clic en el siguiente enlace: " + enlaceRestablecimiento
            );

            model.addAttribute("mensaje", "Se ha enviado un enlace de restablecimiento a su correo electrónico.");
        } else {
            model.addAttribute("error", "No se encontró ninguna cuenta con ese correo.");
        }

        return "recuperarContrasena";
    }
}