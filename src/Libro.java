
public class Libro {
    private String codigo;
    private String titulo;
    private String autor;
    private int anio;
    private boolean disponible;

    public Libro(String codigo, String titulo, String autor, int anio) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.disponible = true;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isDisponible() {
        return disponible;
    }

    // Setters (Para el requerimiento R2: Modificar Libro)
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return codigo + " - " + titulo + " (" + autor + ", " + anio + ") " +
                (disponible ? "[Disponible]" : "[Prestado]");
    }
}
