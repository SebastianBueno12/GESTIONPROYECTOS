package SOFTWARE.GESTIONPROYECTOS.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirigirAlLoginControlador {
    @GetMapping("/")
    public String redirigirAlLogin() {
        // Redirigir siempre al login al acceder a la raíz
        return "redirect:/login";
    }
}