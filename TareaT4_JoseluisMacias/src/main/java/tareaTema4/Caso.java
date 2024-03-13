package tareaTema4;

import javax.persistence.*;
import java.util.Date;



@Entity
@Table(name = "CASOS")
public class Caso implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CASO")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_JUEZ")
    private Juez juez;

    @Column(name = "TIPO_DELITO")
    private String tipoDelito;

    @Column(name = "FECHA_INICIO")
    private Date fechaInicio;

    @Column(name = "ESTADO")
    private String estado;

    // Constructor vacío requerido por Hibernate
    public Caso() {
    }

    // Constructor con parámetros para crear objetos Caso fácilmente
    public Caso(Juez juez, String tipoDelito, Date fechaInicio, String estado) {
        this.juez = juez;
        this.tipoDelito = tipoDelito;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juez getJuez() {
        return juez;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public String getTipoDelito() {
        return tipoDelito;
    }

    public void setTipoDelito(String tipoDelito) {
        this.tipoDelito = tipoDelito;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
