package tareaTema4;

import javax.persistence.*;

@Entity
@Table(name = "JUECES")
public class Juez implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_JUEZ")
    private int id;

    @Column(name = "NOMBRE_JUEZ")
    private String nombre;

    @Column(name = "APELLIDO_JUEZ")
    private String apellido;

    @Column(name = "AREA_ESPECIALIZACION")
    private String areaEspecializacion;

    // Constructor vacío requerido por Hibernate
    public Juez() {
    }

    // Constructor con parámetros para crear objetos Juez fácilmente
    public Juez(String nombre, String apellido, String areaEspecializacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.areaEspecializacion = areaEspecializacion;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getAreaEspecializacion() {
        return areaEspecializacion;
    }

    public void setAreaEspecializacion(String areaEspecializacion) {
        this.areaEspecializacion = areaEspecializacion;
    }
}
