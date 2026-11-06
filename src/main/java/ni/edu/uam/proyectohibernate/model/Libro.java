package ni.edu.uam.proyectohibernate.model;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Libro", schema = "catalogos")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private  String titulo;

    @Column(name = "autor", nullable = false, length = 150)
    private String audtor;

    @Column(name = "anio_publicacion", nullable = false)
    private int aniopPublicacion;

    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    public Libro(String titulo, String audtor, int aniopPublicacion, BigDecimal precio) {
        this.titulo = titulo;
        this.audtor = audtor;
        this.aniopPublicacion = aniopPublicacion;
        this.precio = precio;
    }

    public Libro() {
    }

    public Libro(long id, String titulo, String audtor, int aniopPublicacion, BigDecimal precio) {
        this.id = id;
        this.titulo = titulo;
        this.audtor = audtor;
        this.aniopPublicacion = aniopPublicacion;
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAudtor() {
        return audtor;
    }

    public void setAudtor(String audtor) {
        this.audtor = audtor;
    }

    public int getAniopPublicacion() {
        return aniopPublicacion;
    }

    public void setAniopPublicacion(int aniopPublicacion) {
        this.aniopPublicacion = aniopPublicacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
