package SOFTWARE.GESTIONPROYECTOS.seguridad;


import SOFTWARE.GESTIONPROYECTOS.modelo.Rol;
import SOFTWARE.GESTIONPROYECTOS.repositorio.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleInitializer {

    @Autowired
    private RolRepositorio rolRepositorio;

    @PostConstruct
    public void init() {
        if (rolRepositorio.findByNombre("ROLE_ADMIN") == null) {
            rolRepositorio.save(new Rol("ROLE_ADMIN"));
        }
        if (rolRepositorio.findByNombre("ROLE_COORDINADOR") == null) {
            rolRepositorio.save(new Rol("ROLE_COORDINADOR"));
        }
        if (rolRepositorio.findByNombre("ROLE_EMPLEADO") == null) {
            rolRepositorio.save(new Rol("ROLE_EMPLEADO"));
        }
    }
}