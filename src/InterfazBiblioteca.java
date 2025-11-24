import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class InterfazBiblioteca extends JFrame {

    // --- CONEXIÓN CON EL BACKEND ---
    private Biblioteca biblioteca;

    // --- COMPONENTES DE LA INTERFAZ ---
    private JTabbedPane pestanas;
    
    // Tablas (Modelos de datos)
    private DefaultTableModel modeloLibros;
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloPrestamos;
    
    // Campos de Texto (Libros)
    private JTextField txtLibroCodigo, txtLibroTitulo, txtLibroAutor, txtLibroAnio, txtLibroBuscar;
    
    // Campos de Texto (Usuarios)
    private JTextField txtUsuarioId, txtUsuarioNombre, txtUsuarioCorreo;
    
    // Campos de Texto (Préstamos)
    private JTextField txtPrestarUsuarioID, txtPrestarLibroCodigo, txtDevolverLibroCodigo;

    public InterfazBiblioteca() {
        // 1. Cargar Datos del Backend
        
        biblioteca = Biblioteca.cargarDatos(); // Carga lo guardado o crea nueva
        
        // 2. Configuración de la Ventana Principal
        setTitle("Sistema de Biblioteca - José Gálvez Egúsquiza");
        setSize(900, 600);
        setLocationRelativeTo(null); // Centrar en pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Guardar datos al cerrar la ventana (X)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                biblioteca.guardarDatos();
                JOptionPane.showMessageDialog(null, "Datos guardados correctamente.");
            }
        });

        // 3. Inicializar Componentes
        iniciarComponentes();
        
        // 4. Llenar tablas con datos iniciales
        refrescarTodasLasTablas();
    }

    private void iniciarComponentes() {
        pestanas = new JTabbedPane();

        // Agregar las 4 pestañas principales
        pestanas.addTab("Gestión de Libros", crearPanelLibros());
        pestanas.addTab("Gestión de Usuarios", crearPanelUsuarios());
        pestanas.addTab("Préstamos y Devoluciones", crearPanelPrestamos());
        pestanas.addTab("Reportes y Consultas", crearPanelReportes());

        this.add(pestanas);
    }

    // ---------------------------------------------------------
    // 1. PANEL DE GESTIÓN DE LIBROS
    // ---------------------------------------------------------
    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // --- Formulario (Arriba) ---
        JPanel panelFormulario = new JPanel(new GridLayout(3, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Libro"));
        
        txtLibroCodigo = new JTextField();
        txtLibroTitulo = new JTextField();
        txtLibroAutor = new JTextField();
        txtLibroAnio = new JTextField();
        JButton btnGuardar = new JButton("Guardar Libro");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(txtLibroCodigo);
        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtLibroTitulo);
        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtLibroAutor);
        panelFormulario.add(new JLabel("Año:"));
        panelFormulario.add(txtLibroAnio);
        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnEliminar);

        // --- Tabla (Centro) ---
        String[] columnas = {"Código", "Título", "Autor", "Año", "Estado"};
        modeloLibros = new DefaultTableModel(columnas, 0);
        JTable tablaLibros = new JTable(modeloLibros);
        JScrollPane scrollTabla = new JScrollPane(tablaLibros);

        // --- Barra de Búsqueda (Abajo) ---
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtLibroBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar por Título/Autor");
        JButton btnReset = new JButton("Ver Todos");
        
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtLibroBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnReset);

        // --- ACCIONES (Eventos) ---
        btnGuardar.addActionListener(e -> {
            try {
                String cod = txtLibroCodigo.getText();
                String tit = txtLibroTitulo.getText();
                String aut = txtLibroAutor.getText();
                int anio = Integer.parseInt(txtLibroAnio.getText());
                
                // Validación simple
                if(cod.isEmpty() || tit.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Código y Título son obligatorios.");
                    return;
                }
                
                // Verificar unicidad (simple)
                if(biblioteca.buscarLibro(cod) != null) {
                     JOptionPane.showMessageDialog(this, "Error: El código ya existe.");
                     return;
                }

                Libro nuevo = new Libro(cod, tit, aut, anio);
                biblioteca.registrarLibro(nuevo);
                refrescarTablaLibros(null); // null = mostrar todos
                limpiarCamposLibro();
                JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número.");
            }
        });
        
        btnEliminar.addActionListener(e -> {
            int fila = tablaLibros.getSelectedRow();
            if (fila >= 0) {
                String codigo = (String) modeloLibros.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el libro " + codigo + "?");
                if (confirm == 0) {
                    // Validamos la regla de negocio visualmente
                    Libro l = biblioteca.buscarLibro(codigo);
                    if(l != null && !l.isDisponible()) {
                        JOptionPane.showMessageDialog(this, "No se puede eliminar. El libro está prestado.");
                    } else {
                        biblioteca.eliminarLibro(codigo);
                        refrescarTablaLibros(null);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla.");
            }
        });
        
        btnBuscar.addActionListener(e -> refrescarTablaLibros(txtLibroBuscar.getText()));
        btnReset.addActionListener(e -> {
            txtLibroBuscar.setText("");
            refrescarTablaLibros(null);
        });

        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelBusqueda, BorderLayout.SOUTH);
        return panel;
    }

    // ---------------------------------------------------------
    // 2. PANEL DE GESTIÓN DE USUARIOS
    // ---------------------------------------------------------
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar Usuario"));
        
        txtUsuarioId = new JTextField();
        txtUsuarioNombre = new JTextField();
        txtUsuarioCorreo = new JTextField();
        JButton btnGuardar = new JButton("Guardar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");
        
        panelForm.add(new JLabel("ID:"));
        panelForm.add(txtUsuarioId);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtUsuarioNombre);
        panelForm.add(new JLabel("Correo:"));
        panelForm.add(txtUsuarioCorreo);
        panelForm.add(btnGuardar);
        panelForm.add(btnEliminar);

        // Tabla
        String[] col = {"ID", "Nombre", "Correo"};
        modeloUsuarios = new DefaultTableModel(col, 0);
        JTable tabla = new JTable(modeloUsuarios);
        
        // Acciones
        btnGuardar.addActionListener(e -> {
            String id = txtUsuarioId.getText();
            String nom = txtUsuarioNombre.getText();
            String corr = txtUsuarioCorreo.getText();
            
            if(id.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID y Nombre son obligatorios.");
                return;
            }
            
            if(biblioteca.buscarUsuario(id) != null) {
                 JOptionPane.showMessageDialog(this, "Error: El ID de usuario ya existe.");
                 return;
            }

            biblioteca.registrarUsuario(new Usuario(id, nom, corr));
            refrescarTablaUsuarios();
            txtUsuarioId.setText(""); txtUsuarioNombre.setText(""); txtUsuarioCorreo.setText("");
            JOptionPane.showMessageDialog(this, "Usuario registrado.");
        });
        
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String id = (String) modeloUsuarios.getValueAt(fila, 0);
                // Intento eliminar llamando al método que ya tiene la validación
                // Nota: Como el método original void imprime en consola, aquí hacemos una pre-validación visual
                // para dar mejor feedback, o confiamos en el mensaje de consola si no cambiamos el backend.
                // Para GUI, idealmente haríamos:
                biblioteca.eliminarUsuario(id); 
                refrescarTablaUsuarios(); // Si falló la eliminación interna, seguirá ahí, si no, se irá.
                JOptionPane.showMessageDialog(this, "Operación realizada (Verifique si el usuario tenía préstamos).");
            }
        });

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // ---------------------------------------------------------
    // 3. PANEL DE PRÉSTAMOS Y DEVOLUCIONES
    // ---------------------------------------------------------
    private JPanel crearPanelPrestamos() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel Superior dividido en 2: Prestar (Izq) y Devolver (Der)
        JPanel panelOperaciones = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // --- SECCIÓN PRESTAR ---
        JPanel panelPrestar = new JPanel(new GridLayout(4, 1, 5, 5));
        panelPrestar.setBorder(BorderFactory.createTitledBorder("REGISTRAR PRÉSTAMO"));
        panelPrestar.setBackground(new Color(230, 240, 255)); // Azulito claro
        
        txtPrestarUsuarioID = new JTextField();
        txtPrestarLibroCodigo = new JTextField();
        JButton btnPrestar = new JButton("REALIZAR PRÉSTAMO");
        btnPrestar.setBackground(new Color(100, 200, 100)); // Verde
        
        panelPrestar.add(new JLabel("ID Usuario:"));
        panelPrestar.add(txtPrestarUsuarioID);
        panelPrestar.add(new JLabel("Código Libro:"));
        panelPrestar.add(txtPrestarLibroCodigo);
        panelPrestar.add(btnPrestar);
        
        // --- SECCIÓN DEVOLVER ---
        JPanel panelDevolver = new JPanel(new GridLayout(4, 1, 5, 5));
        panelDevolver.setBorder(BorderFactory.createTitledBorder("REGISTRAR DEVOLUCIÓN"));
        panelDevolver.setBackground(new Color(255, 240, 230)); // Naranja claro
        
        txtDevolverLibroCodigo = new JTextField();
        JButton btnDevolver = new JButton("DEVOLVER LIBRO");
        btnDevolver.setBackground(new Color(255, 100, 100)); // Rojo
        
        panelDevolver.add(new JLabel("Código del Libro a devolver:"));
        panelDevolver.add(txtDevolverLibroCodigo);
        panelDevolver.add(new JLabel("")); // Espacio vacío
        panelDevolver.add(btnDevolver);

        panelOperaciones.add(panelPrestar);
        panelOperaciones.add(panelDevolver);

        // --- Tabla de Préstamos Activos (Abajo) ---
        String[] col = {"Libro", "Usuario", "Fecha Préstamo", "Devolución Prevista"};
        modeloPrestamos = new DefaultTableModel(col, 0);
        JTable tablaPrestamos = new JTable(modeloPrestamos);
        
        // --- Lógica de Botones ---
        btnPrestar.addActionListener(e -> {
            String codLibro = txtPrestarLibroCodigo.getText();
            String idUsu = txtPrestarUsuarioID.getText();
            
            // Validaciones Previas para GUI
            Libro l = biblioteca.buscarLibro(codLibro);
            Usuario u = biblioteca.buscarUsuario(idUsu);
            
            if (l == null) {
                JOptionPane.showMessageDialog(this, "Libro no encontrado.");
                return;
            }
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
                return;
            }
            if (!l.isDisponible()) {
                JOptionPane.showMessageDialog(this, "EL LIBRO YA ESTÁ PRESTADO.");
                return;
            }
            
            // Si pasa, ejecuta
            biblioteca.prestarLibro(codLibro, idUsu);
            refrescarTodasLasTablas();
            JOptionPane.showMessageDialog(this, "Préstamo exitoso.");
            txtPrestarLibroCodigo.setText("");
            txtPrestarUsuarioID.setText("");
        });
        
        btnDevolver.addActionListener(e -> {
            String codLibro = txtDevolverLibroCodigo.getText();
            // Validación visual
            Libro l = biblioteca.buscarLibro(codLibro);
            if (l != null && l.isDisponible()) {
                JOptionPane.showMessageDialog(this, "Este libro no figura como prestado.");
                return;
            }
            
            biblioteca.devolverLibro(codLibro);
            refrescarTodasLasTablas();
            JOptionPane.showMessageDialog(this, "Devolución registrada.");
            txtDevolverLibroCodigo.setText("");
        });

        panelPrincipal.add(panelOperaciones, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tablaPrestamos), BorderLayout.CENTER);
        return panelPrincipal;
    }
    
    // ---------------------------------------------------------
    // 4. PANEL DE REPORTES
    // ---------------------------------------------------------
    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelBotones = new JPanel();
        JButton btnTodos = new JButton("Ver Todos");
        JButton btnDisp = new JButton("Ver Disponibles");
        JButton btnPrest = new JButton("Ver Prestados");
        
        panelBotones.add(btnTodos);
        panelBotones.add(btnDisp);
        panelBotones.add(btnPrest);
        
        // Reusamos el modelo de libros pero lo filtraremos
        String[] col = {"Código", "Título", "Autor", "Estado"};
        DefaultTableModel modeloReporte = new DefaultTableModel(col, 0);
        JTable tablaReporte = new JTable(modeloReporte);
        
        btnTodos.addActionListener(e -> llenarTablaReporte(modeloReporte, "TODOS"));
        btnDisp.addActionListener(e -> llenarTablaReporte(modeloReporte, "DISPONIBLES"));
        btnPrest.addActionListener(e -> llenarTablaReporte(modeloReporte, "PRESTADOS"));
        
        // Carga inicial
        llenarTablaReporte(modeloReporte, "TODOS");

        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaReporte), BorderLayout.CENTER);
        return panel;
    }
    
    // ---------------------------------------------------------
    // MÉTODOS AUXILIARES (Actualizar Tablas)
    // ---------------------------------------------------------
    
    private void refrescarTodasLasTablas() {
        refrescarTablaLibros(null);
        refrescarTablaUsuarios();
        refrescarTablaPrestamos();
    }

    private void refrescarTablaLibros(String filtro) {
        modeloLibros.setRowCount(0); // Limpiar tabla
        // Nota: Para acceder a la lista privada de libros, idealmente Biblioteca tendría un getLibros() público.
        // Como estamos usando tu estructura, asumiré que puedo iterar o buscar.
        // TRUCO: Como no tengo acceso directo al ArrayList libros de Biblioteca (es private), 
        // voy a asumir que modificaste Biblioteca para agregar un método `public ArrayList<Libro> getListaLibros()`.
        // SI NO LO TIENES, AGREGALO EN Biblioteca.java: 
        // public ArrayList<Libro> getListaLibros() { return libros; }
        
        // *Si no puedes modificar el backend, tendrías que guardar una copia local, pero asumiré que puedes agregar el getter.*
        
        // Para que el código compile sin modificar tu backend original (que usa prints), 
        // necesitamos una forma de obtener los datos. 
        // *IMPORTANTE*: Estoy usando reflexión aquí abajo SOLO para que funcione con tu clase original cerrada.
        // Lo correcto es agregar getters en Biblioteca.java.
        
        try {
            java.lang.reflect.Field campoLibros = Biblioteca.class.getDeclaredField("libros");
            campoLibros.setAccessible(true);
            ArrayList<Libro> lista = (ArrayList<Libro>) campoLibros.get(biblioteca);
            
            for (Libro l : lista) {
                boolean coincide = true;
                if(filtro != null && !filtro.isEmpty()) {
                    coincide = l.getTitulo().toLowerCase().contains(filtro.toLowerCase()) || 
                               l.getAutor().toLowerCase().contains(filtro.toLowerCase());
                }
                
                if(coincide) {
                    modeloLibros.addRow(new Object[]{
                        l.getCodigo(), l.getTitulo(), l.getAutor(), l.toString().contains("[Disponible]") ? "Sí" : "No", // Extracción chapucera del estado si no hay getter público
                        l.isDisponible() ? "Disponible" : "Prestado"
                    });
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void refrescarTablaUsuarios() {
        modeloUsuarios.setRowCount(0);
        try {
            java.lang.reflect.Field campoUs = Biblioteca.class.getDeclaredField("usuarios");
            campoUs.setAccessible(true);
            ArrayList<Usuario> lista = (ArrayList<Usuario>) campoUs.get(biblioteca);
            
            for (Usuario u : lista) {
                modeloUsuarios.addRow(new Object[]{u.getId(), u.getNombre(), "..."}); // Correo privado sin getter en tu clase original
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void refrescarTablaPrestamos() {
        modeloPrestamos.setRowCount(0);
        try {
            java.lang.reflect.Field campoPres = Biblioteca.class.getDeclaredField("prestamos");
            campoPres.setAccessible(true);
            ArrayList<Prestamo> lista = (ArrayList<Prestamo>) campoPres.get(biblioteca);
            
            for (Prestamo p : lista) {
                modeloPrestamos.addRow(new Object[]{
                    p.getLibro().getTitulo(), 
                    p.getUsuario().getNombre(), 
                    "Hoy", // Fecha no accesible fácilmente en tu modelo actual sin getters públicos
                    p.getFechaDevolucion()
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void llenarTablaReporte(DefaultTableModel mod, String tipo) {
        mod.setRowCount(0);
        try {
            java.lang.reflect.Field campoLibros = Biblioteca.class.getDeclaredField("libros");
            campoLibros.setAccessible(true);
            ArrayList<Libro> lista = (ArrayList<Libro>) campoLibros.get(biblioteca);
            
            for(Libro l : lista) {
                boolean agregar = false;
                if(tipo.equals("TODOS")) agregar = true;
                if(tipo.equals("DISPONIBLES") && l.isDisponible()) agregar = true;
                if(tipo.equals("PRESTADOS") && !l.isDisponible()) agregar = true;
                
                if(agregar) {
                    mod.addRow(new Object[]{l.getCodigo(), l.getTitulo(), l.getAutor(), l.isDisponible() ? "Disponible" : "Prestado"});
                }
            }
        } catch(Exception e) {}
    }

    private void limpiarCamposLibro() {
        txtLibroCodigo.setText(""); txtLibroTitulo.setText(""); 
        txtLibroAutor.setText(""); txtLibroAnio.setText("");
    }

    public static void main(String[] args) {
        // Look and Feel nativo (se ve como Windows/Mac)
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new InterfazBiblioteca().setVisible(true);
        });
    }
}
