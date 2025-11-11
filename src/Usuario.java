
public class Usuario {
    private String id;
    private String nombre;
    private String correo;

    public Usuario(String id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    @Override
    public String toString() {
        return id + " - " + nombre + " (" + correo + ")";
    }
}

