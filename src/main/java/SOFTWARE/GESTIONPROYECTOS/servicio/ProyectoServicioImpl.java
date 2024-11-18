package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.modelo.Proyecto;
import SOFTWARE.GESTIONPROYECTOS.repositorio.ProyectoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServicioImpl implements ProyectoServicio {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Override
    public Proyecto guardarProyecto(Proyecto proyecto) {
        return proyectoRepositorio.save(proyecto);
    }

    @Override
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return proyectoRepositorio.findById(id);
    }

    @Override
    public Page<Proyecto> listarProyectos(Pageable pageable) {
        return proyectoRepositorio.findAll(pageable);
    }

    @Override
    public List<Proyecto> obtenerProyectosPorCoordinador(Long coordinadorId) {
        return proyectoRepositorio.findByCoordinadorId(coordinadorId);
    }
}
