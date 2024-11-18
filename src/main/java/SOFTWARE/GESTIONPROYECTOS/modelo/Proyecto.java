package SOFTWARE.GESTIONPROYECTOS.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_clave", nullable = false)
    private String nombreClave;

    @Column(name = "denominacion_comercial", nullable = false)
    private String denominacionComercial;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // Formato para la fecha de inicio
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  // Formato para la fecha de finalización
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "coordinador_id", nullable = true)
    private Usuario coordinador;

    // Getters y setters

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Usuario coordinador) {
        this.coordinador = coordinador;
    }
}
