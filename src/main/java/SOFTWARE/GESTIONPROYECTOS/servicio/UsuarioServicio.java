package SOFTWARE.GESTIONPROYECTOS.servicio;


import SOFTWARE.GESTIONPROYECTOS.controlador.dto.UsuarioRegistroDTO;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


    public interface UsuarioServicio extends UserDetailsService {

    Optional<Usuario> findByEmail(String email);

    void crearTokenDeRecuperacion(Usuario usuario, String token);

    Usuario findByTokenDeRecuperacion(String token);

    void actualizarContrasena(Usuario usuario, String nuevaContrasena);

    List<Usuario> listarUsuarios();

    long contarUsuarios();

    Usuario obtenerUsuarioPorId(Long id);

    void actualizarUsuario(Long id, Usuario usuarioActualizado);

    @Transactional
    Usuario guardar(UsuarioRegistroDTO registroDTO);

    @Transactional
    void guardarUsuarioConRol(Usuario usuario, String nombreRol);

    void actualizarUsuario(Long id, Usuario usuarioActualizado, String nombreRol);

    void eliminarUsuario(Long id);

}
