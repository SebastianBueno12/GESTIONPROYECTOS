package SOFTWARE.GESTIONPROYECTOS.repositorio;


import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    // Buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Buscar un usuario por su token de restablecimiento de contraseña
    Optional<Usuario> findByTokenRestablecimiento(String token);
}
