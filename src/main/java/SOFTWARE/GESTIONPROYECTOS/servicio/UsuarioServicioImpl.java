package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.controlador.dto.UsuarioRegistroDTO;
import SOFTWARE.GESTIONPROYECTOS.modelo.Rol;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.repositorio.RolRepositorio;
import SOFTWARE.GESTIONPROYECTOS.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class UsuarioServicioImpl implements UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepositorio.findByEmail(email);
    }

    @Override
    public void crearTokenDeRecuperacion(Usuario usuario, String token) {
        usuario.setTokenRestablecimiento(token);
        usuarioRepositorio.save(usuario);
    }

    @Override
    public Usuario findByTokenDeRecuperacion(String token) {
        return usuarioRepositorio.findByTokenRestablecimiento(token)
                .orElseThrow(() -> new IllegalArgumentException("Token no encontrado"));
    }

    @Override
    public void actualizarContrasena(Usuario usuario, String nuevaContrasena) {
        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepositorio.save(usuario);
    }

    public long contarUsuarios() {
        return usuarioRepositorio.count();  // Devuelve el número total de usuarios registrados
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellido(usuarioActualizado.getApellido());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        usuarioRepositorio.save(usuario);
    }

    @Override
    public Usuario guardar(UsuarioRegistroDTO registroDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        // Asignación del rol por defecto (puedes cambiar esto si necesitas otro rol)
        Rol rol = rolRepositorio.findByNombre("ROLE_EMPLEADO");
        if (rol == null) {
            throw new IllegalArgumentException("Rol no encontrado");
        }
        usuario.setRoles(new HashSet<>(Collections.singletonList(rol)));

        return usuarioRepositorio.save(usuario); // Guardar el usuario en la base de datos
    }

    @Override
    @Transactional
    public void guardarUsuarioConRol(Usuario usuario, String nombreRol) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Rol rol = rolRepositorio.findByNombre(nombreRol);
        if (rol == null) {
            throw new IllegalArgumentException("Rol no encontrado");
        }

        usuario.setRoles(new HashSet<>(Collections.singletonList(rol)));
        usuarioRepositorio.save(usuario);
    }

    @Override
    @Transactional
    public void actualizarUsuario(Long id, Usuario usuarioActualizado, String nombreRol) {
        Usuario usuarioExistente = obtenerUsuarioPorId(id);

        // Actualizar los campos del usuario
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        // Buscar y asignar el nuevo rol
        Rol nuevoRol = rolRepositorio.findByNombre(nombreRol);
        if (nuevoRol == null) {
            throw new IllegalArgumentException("Rol no encontrado: " + nombreRol);
        }

        // Actualizar roles (reemplazar roles actuales por el nuevo rol)
        Set<Rol> roles = new HashSet<>();
        roles.add(nuevoRol);
        usuarioExistente.setRoles(roles);

        // Guardar los cambios
        usuarioRepositorio.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario o contraseña inválidos"));

        Set<Rol> roles = usuario.getRoles();
        Set<org.springframework.security.core.GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Rol rol : roles) {
            grantedAuthorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(rol.getNombre()));
        }

        return new User(usuario.getEmail(), usuario.getPassword(), grantedAuthorities);
    }
}
