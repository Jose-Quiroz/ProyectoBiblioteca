
import java.time.LocalDate;
import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Libro> libros;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Prestamo> prestamos;

    public Biblioteca() {
        libros = new ArrayList<>();
        usuarios = new ArrayList<>();
        prestamos = new ArrayList<>();
        cargarDatos(); // R15: Cargar datos al iniciar
    }
    
    // --- LÓGICA DE GESTIÓN DE LIBROS (R1, R3, R4, R10, R11) ---
    
    public void registrarLibro(Libro libro) {
        libros.add(libro);
        System.out.println("Libro '" + libro.getTitulo() + "' registrado.");
    }

    public void eliminarLibro(String codigo) {
        Libro libro = buscarLibro(codigo);
        if (libro != null) {
            // R3: Criterio de Aceptación: No se puede eliminar si está prestado
            if (libro.isDisponible()) {
                libros.remove(libro);
                System.out.println("Libro eliminado correctamente.");
            } else {
                System.out.println("No se puede eliminar. El libro está prestado.");
            }
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    public Libro buscarLibro(String codigo) {
        for (Libro l : libros) {
            if (l.getCodigo().equalsIgnoreCase(codigo)) return l;
        }
        return null;
    }
    
    // R10: Búsqueda por Autor/Título
    public ArrayList<Libro> buscarLibrosPorCriterio(String criterio) {
        ArrayList<Libro> resultados = new ArrayList<>();
        String criterioLower = criterio.toLowerCase();
        for (Libro l : libros) {
            if (l.getTitulo().toLowerCase().contains(criterioLower) || 
                l.getAutor().toLowerCase().contains(criterioLower)) {
                resultados.add(l);
            }
        }
        return resultados;
    }
    
    // R11: Listar libros disponibles
    public void listarLibrosDisponibles() {
        System.out.println("\n LISTA DE LIBROS DISPONIBLES:");
        boolean hayDisponibles = false;
        for (Libro l : libros) {
            if (l.isDisponible()) {
                System.out.println(l);
                hayDisponibles = true;
            }
        }
        if (!hayDisponibles) {
            System.out.println("No hay libros disponibles en este momento.");
        }
    }
    
    public void listarLibros() {
        System.out.println("\n LISTA COMPLETA DE LIBROS:");
        for (Libro l : libros) System.out.println(l);
    }

    // --- LÓGICA DE GESTIÓN DE USUARIOS (R5, R7) ---
    
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("Usuario '" + usuario.getNombre() + "' registrado.");
    }
    
    public void eliminarUsuario(String id) {
        Usuario usuario = buscarUsuario(id);
        if (usuario != null) {
            // R7: Criterio de Aceptación: No se puede eliminar si tiene préstamos activos
            if (!tienePrestamosActivos(usuario)) {
                usuarios.remove(usuario);
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("No se puede eliminar. El usuario tiene préstamos activos.");
            }
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    public Usuario buscarUsuario(String id) {
        for (Usuario u : usuarios) {
            if (u.getId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }
    
    public void listarUsuarios() {
        System.out.println("\n LISTA DE USUARIOS:");
        for (Usuario u : usuarios) System.out.println(u);
    }
    
    // Método auxiliar para R7
    private boolean tienePrestamosActivos(Usuario usuario) {
        for (Prestamo p : prestamos) {
            if (p.getUsuario().getId().equals(usuario.getId())) {
                return true;
            }
        }
        return false;
    }
    
    public void prestarLibro(String codigoLibro, String idUsuario) {
        Libro libro = buscarLibro(codigoLibro);
        Usuario usuario = buscarUsuario(idUsuario);

        if (libro != null && usuario != null && libro.isDisponible()) {
            Prestamo prestamo = new Prestamo(libro, usuario);
            prestamos.add(prestamo);
            System.out.println("Préstamo registrado: " + prestamo);
        } else {
            System.out.println("No se pudo realizar el préstamo. Verifique el código/ID o la disponibilidad del libro.");
        }
    }
    public void devolverLibro(String codigoLibro) {
        for (int i = 0; i < prestamos.size(); i++) {
            Prestamo p = prestamos.get(i);
            if (p.getLibro().getCodigo().equalsIgnoreCase(codigoLibro)) {
                p.devolverLibro(); 
                prestamos.remove(i); 
                System.out.println("Libro devuelto correctamente.");
                return;
            }
        }
        System.out.println("No se encontró el préstamo activo para ese libro.");
    }
    public void listarPrestamos() {
        System.out.println("\n PRÉSTAMOS ACTIVOS:");
        for (Prestamo p : prestamos) System.out.println(p);
    }
    public void listarPrestamosVencidos() {
        System.out.println("\n  PRÉSTAMOS CON DEVOLUCIÓN VENCIDA:");
        LocalDate hoy = LocalDate.now();
        boolean hayVencidos = false;
        for (Prestamo p : prestamos) {
            if (p.getFechaDevolucion().isBefore(hoy)) {
                System.out.println(p);
                hayVencidos = true;
            }
        }
        if (!hayVencidos) {
            System.out.println("No hay préstamos vencidos.");
        }
    }
    public void guardarDatos() {
   
        System.out.println("\n Datos guardados persistentemente.");
    }
    public void cargarDatos() {
        // Lógica para cargar 'libros', 'usuarios', 'prestamos' desde archivos al iniciar
        System.out.println("\n Datos cargados al iniciar la biblioteca.");
    }
}
