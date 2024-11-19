package SOFTWARE.GESTIONPROYECTOS.servicio;

import SOFTWARE.GESTIONPROYECTOS.controlador.dto.UsuarioRegistroDTO;
import SOFTWARE.GESTIONPROYECTOS.modelo.Rol;
import SOFTWARE.GESTIONPROYECTOS.modelo.Usuario;
import SOFTWARE.GESTIONPROYECTOS.repositorio.ProyectoRepositorio;
import SOFTWARE.GESTIONPROYECTOS.repositorio.RolRepositorio;
import SOFTWARE.GESTIONPROYECTOS.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.*;

@Service
@Transactional
public class UsuarioServicioImpl implements UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

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

        usuario.setDocumento(usuarioActualizado.getDocumento());
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellido(usuarioActualizado.getApellido());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        usuarioRepositorio.save(usuario);
    }

    @Override
    public Usuario guardar(UsuarioRegistroDTO registroDTO) {
        Usuario usuario = new Usuario();
        usuario.setDocumento(registroDTO.getDocumento());
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setDireccion(registroDTO.getDireccion());
        usuario.setTelefono(registroDTO.getTelefono());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Rol rol = rolRepositorio.findByNombre("ROLE_USUARIO");
        usuario.setRoles(new HashSet<>(Collections.singletonList(rol)));

        return usuarioRepositorio.save(usuario);
    }

    @Override
    @Transactional
    public void guardarUsuarioConRol(Usuario usuario, String nombreRol) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignar el rol
        Rol rol = rolRepositorio.findByNombre(nombreRol);
        if (rol == null) {
            throw new IllegalArgumentException("Rol no encontrado");
        }
        usuario.setRoles(new HashSet<>(Collections.singletonList(rol)));

        // Guardar el usuario con todos sus campos
        usuarioRepositorio.save(usuario);
    }
    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Rol rol = rolRepositorio.findByNombre("ROLE_USUARIO");
        usuario.setRoles(new HashSet<>(Collections.singletonList(rol)));
        return usuarioRepositorio.save(usuario);
    }

    @Override
    @Transactional
    public void actualizarUsuario(Long id, Usuario usuarioActualizado, String nombreRol) {
        Usuario usuarioExistente = obtenerUsuarioPorId(id);

        // Actualizar los campos del usuario
        usuarioExistente.setDocumento(usuarioActualizado.getDocumento());
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
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
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean esCoordinador = usuario.getRoles().stream()
                .anyMatch(rol -> "ROLE_COORDINADOR".equals(rol.getNombre()));

        if (esCoordinador) {
            if (usuario.getProyectosAsignados() != null && !usuario.getProyectosAsignados().isEmpty()) {
                usuario.getProyectosAsignados().forEach(proyecto -> {
                    proyecto.setCoordinador(null);
                    proyectoRepositorio.save(proyecto);
                });
            }
        }

        usuarioRepositorio.delete(usuario);
    }

    @Override
    public List<Usuario> listarCoordinadoresDisponibles() {
        return usuarioRepositorio.findAll().stream()
                .filter(usuario -> usuario.getRoles().stream()
                        .anyMatch(rol -> "ROLE_COORDINADOR".equals(rol.getNombre()))
                        && usuario.getProyectosAsignados().isEmpty())
                .collect(Collectors.toList());
    }
    @Override
    public List<Usuario> listarEmpleados() {
        return usuarioRepositorio.findAll().stream()
                .filter(usuario -> usuario.getRoles().stream()
                        .anyMatch(rol -> rol.getNombre().equals("ROLE_EMPLEADO")))
                .collect(Collectors.toList());
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
    @Override
    public void actualizarUsuarioSinCambiarPassword(Usuario usuarioActualizado) {
        Usuario usuarioExistente = obtenerUsuarioPorId(usuarioActualizado.getId());

        // Solo actualiza los campos necesarios
        usuarioExistente.setDocumento(usuarioActualizado.getDocumento());
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setFechaContratacion(usuarioActualizado.getFechaContratacion());
        usuarioExistente.setRoles(usuarioActualizado.getRoles());

        // No actualiza la contraseña
        usuarioRepositorio.save(usuarioExistente);
    }
}
