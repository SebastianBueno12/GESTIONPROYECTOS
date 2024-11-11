package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Rol;
import SOFTWARE.GESTIONPROYECTOS.repositorio.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RolServicio {
    @Autowired
    private RolRepositorio rolRepositorio;

    public List<Rol> listarRoles() {
        return rolRepositorio.findAll();
    }
}

