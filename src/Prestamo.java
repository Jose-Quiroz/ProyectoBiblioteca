
import java.time.LocalDate;
public class Prestamo {
    private Libro libro;
    private Usuario usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public Prestamo(Libro libro, Usuario usuario) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(7); 
        libro.setDisponible(false); 
    }
    public Libro getLibro() {
        return libro;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    public void devolverLibro() {
        libro.setDisponible(true); 
    }
    @Override
    public String toString() {
        return "Libro: " + libro.getTitulo() + " | Usuario: " + usuario.getNombre() +
                " | Devuelve hasta: " + fechaDevolucion;
    }
}
