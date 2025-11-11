
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.registrarLibro(new Libro("L001", "El Principito", "Antoine de Saint-Exupéry", 1943));
        biblioteca.registrarLibro(new Libro("L002", "Cien años de soledad", "Gabriel García Márquez", 1967));
        biblioteca.registrarUsuario(new Usuario("U001", "Ana Pérez", "ana@correo.com"));
        biblioteca.registrarUsuario(new Usuario("U002", "Luis Gómez", "luis@correo.com"));
        
        int opcion = 0;
        do {
            System.out.println("\n=== MENÚ GESTIÓN DE BIBLIOTECA ===");
            System.out.println("1. Gestión de Libros (Registrar, Buscar, Listar)");
            System.out.println("2. Gestión de Usuarios (Registrar)");
            System.out.println("3. Préstamos y Devoluciones");
            System.out.println("4. Listados Especiales (Disponibles, Vencidos)");
            System.out.println("5. Salir y Guardar Datos");
            System.out.print("Seleccione una opción: ");
            
            if (scanner.hasNextInt()) {
                    
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea
                
                switch (opcion) {
                    case 1:
                        menuLibros(biblioteca, scanner);
                        break;
                    case 2:
                        menuUsuarios(biblioteca, scanner);
                        break;
                    case 3:
                        menuPrestamos(biblioteca, scanner);
                        break;
                    case 4:
                        menuListados(biblioteca);
                        break;
                    case 5:
                        biblioteca.guardarDatos(); 
                        System.out.println("¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer
            }
        } while (opcion != 5);
        
        scanner.close();
    }
    

    
    public static void menuLibros(Biblioteca biblioteca, Scanner scanner) {
        System.out.println("\n-- SUBMENÚ LIBROS --");
        System.out.println("1. Registrar Nuevo Libro ");
        System.out.println("2. Listar Todos los Libros");
        System.out.println("3. Buscar por Título/Autor ");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();
        
        if (op == 1) {
            System.out.print("Código: "); String c = scanner.nextLine();
            System.out.print("Título: "); String t = scanner.nextLine();
            System.out.print("Autor: "); String a = scanner.nextLine();
            System.out.print("Año: "); int an = scanner.nextInt();
            scanner.nextLine();
            biblioteca.registrarLibro(new Libro(c, t, a, an));
        } else if (op == 2) {
            biblioteca.listarLibros();
        } else if (op == 3) {
            System.out.print("Ingrese criterio de búsqueda (Título/Autor): ");
            String criterio = scanner.nextLine();
            for (Libro l : biblioteca.buscarLibrosPorCriterio(criterio)) {
                System.out.println(l);
            }
        }
    }

    public static void menuUsuarios(Biblioteca biblioteca, Scanner scanner) {
        System.out.println("\n-- SUBMENÚ USUARIOS --");
        System.out.println("1. Registrar Nuevo Usuario");
        System.out.println("2. Listar Todos los Usuarios");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();

        if (op == 1) {
            System.out.print("ID: "); String id = scanner.nextLine();
            System.out.print("Nombre: "); String n = scanner.nextLine();
            System.out.print("Correo: "); String co = scanner.nextLine();
            biblioteca.registrarUsuario(new Usuario(id, n, co));
        } else if (op == 2) {
            biblioteca.listarUsuarios();
        }
    }
    
    public static void menuPrestamos(Biblioteca biblioteca, Scanner scanner) {
        System.out.println("\n-- SUBMENÚ PRÉSTAMOS --");
        System.out.println("1. Registrar Préstamo ");
        System.out.println("2. Registrar Devolución");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        scanner.nextLine();
        
        if (op == 1) {
            System.out.print("Código del Libro a prestar: "); String cl = scanner.nextLine();
            System.out.print("ID del Usuario prestatario: "); String iu = scanner.nextLine();
            biblioteca.prestarLibro(cl, iu);
        } else if (op == 2) {
            System.out.print("Código del Libro a devolver: "); String cl = scanner.nextLine();
            biblioteca.devolverLibro(cl);
        }
    }
    
    public static void menuListados(Biblioteca biblioteca) {
        System.out.println("\n-- SUBMENÚ LISTADOS --");
        System.out.println("1. Libros Disponibles");
        System.out.println("2. Préstamos Activos ");
        System.out.println("3. Préstamos Vencidos ");
        System.out.print("Opción: ");
        Scanner tempScanner = new Scanner(System.in);
        int op = tempScanner.nextInt();
        
        if (op == 1) {
            biblioteca.listarLibrosDisponibles();
        } else if (op == 2) {
            biblioteca.listarPrestamos();
        } else if (op == 3) {
            biblioteca.listarPrestamosVencidos();
        }
    }      
}
