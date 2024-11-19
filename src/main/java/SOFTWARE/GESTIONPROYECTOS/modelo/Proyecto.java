package SOFTWARE.GESTIONPROYECTOS.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre clave no puede estar vacío")
    @Column(name = "nombre_clave", nullable = false)
    private String nombreClave;

    @NotBlank(message = "La denominación comercial no puede estar vacía")
    @Column(name = "denominacion_comercial", nullable = false)
    private String denominacionComercial;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // Formato para la fecha de inicio
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // Formato para la fecha de finalización
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoProyecto estado;

    @ManyToOne
    @JoinColumn(name = "coordinador_id", nullable = true)
    private Usuario coordinador;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas = new ArrayList<>();

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreClave() {
        return nombreClave;
    }

    public void setNombreClave(String nombreClave) {
        this.nombreClave = nombreClave;
    }

    public String getDenominacionComercial() {
        return denominacionComercial;
    }

    public void setDenominacionComercial(String denominacionComercial) {
        this.denominacionComercial = denominacionComercial;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public Usuario getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Usuario coordinador) {
        this.coordinador = coordinador;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    // Métodos personalizados

    /**
     * Calcula el progreso del proyecto basado en las tareas completadas.
     * @return Porcentaje de progreso (0 a 100)
     */
    public int calcularProgreso() {
        if (tareas == null || tareas.isEmpty()) {
            return 0; // Sin tareas, el progreso es 0%
        }

        long tareasCompletadas = tareas.stream()
                .filter(t -> t.getEstado() == EstadoTarea.COMPLETADA)
                .count();

        return (int) ((double) tareasCompletadas / tareas.size() * 100);
    }


    /**
     * Actualiza el estado del proyecto según el progreso.
     * Si todas las tareas están completadas, el estado pasa a "FINALIZADO".
     */
    public void actualizarEstado() {
        if (tareas != null && !tareas.isEmpty() &&
                tareas.stream().allMatch(t -> t.getEstado() == EstadoTarea.COMPLETADA)) {
            this.estado = EstadoProyecto.FINALIZADO;
        }
    }
}
