package SOFTWARE.GESTIONPROYECTOS.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class LoginController {
    @GetMapping("/login")
    public String mostrarFormularioDeLogin() {
        return "login";  // Carga la vista del login
    }
}