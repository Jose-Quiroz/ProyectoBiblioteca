CREATE DATABASE BIBLIOTECARIO
GO
 
USE BIBLIOTECARIO
GO

CREATE TABLE USUARIO
(
    ID_Usuario VARCHAR(20) PRIMARY KEY NOT NULL,
    Nombre NVARCHAR(150) NOT NULL,
    Correo NVARCHAR(150) NOT NULL
);
GO

-- ============================================
--   TABLA: LIBRO
--   Equivalente a la clase Libro
-- ============================================
CREATE TABLE Libro
(
    Codigo VARCHAR(20) PRIMARY KEY NOT NULL,
    Titulo NVARCHAR(200) NOT NULL,
    Autor  NVARCHAR(150) NOT NULL,
    Anio INT NOT NULL,
    Disponible BIT NOT NULL DEFAULT 1    -- true = disponible
);
GO

-- ============================================
--   TABLA: PRESTAMO
--   Equivalente a la clase Prestamo
-- ============================================
CREATE TABLE Prestamo (
    ID_Prestamo INT IDENTITY(1,1) PRIMARY KEY,
    CodigoLibro VARCHAR(20) NOT NULL,
    ID_Usuario VARCHAR(20) NOT NULL,
    FechaPrestamo DATE NOT NULL,
    FechaDevolucion DATE NOT NULL,
    CONSTRAINT FK_Prestamo_Libro FOREIGN KEY (CodigoLibro)
    REFERENCES Libro(Codigo),
    CONSTRAINT FK_Prestamo_Usuario FOREIGN KEY (ID_Usuario)
    REFERENCES Usuario(ID_Usuario)
);
GO

INSERT INTO Usuario (ID_Usuario, Nombre, Correo) VALUES
('U001', 'Carlos Mejía', 'carlos.mejia@gmail.com'),
('U002', 'Ana Torres', 'ana.torres@gmail.com'),
('U003', 'Luis Paredes', 'luis.paredes@hotmail.com'),
('U004', 'María Silva', 'maria.silva@gmail.com'),
('U005', 'Jorge Ríos', 'jorge.rios@gmail.com'),
('U006', 'Elena Huamán', 'elena.huaman@hotmail.com'),
('U007', 'Pedro Sánchez', 'pedro.sanchez@gmail.com'),
('U008', 'Valeria Gutiérrez', 'valeria.gt@gmail.com'),
('U009', 'Rocío Alvarado', 'rocio.alvarado@gmail.com'),
('U010', 'Ander Ramos', 'ander.ramos@gmail.com');

INSERT INTO Libro (Codigo, Titulo, Autor, Anio, Disponible) VALUES
('L001', 'Programación en Java', 'Herbert Schildt', 2018, 1),
('L002', 'Estructuras de Datos', 'Robert Lafore', 2016, 1),
('L003', 'Cálculo I', 'James Stewart', 2015, 1),
('L004', 'Fundamentos de Física', 'Halliday & Resnick', 2014, 1),
('L005', 'Base de Datos SQL', 'Elmasri & Navathe', 2020, 1),
('L006', 'Inteligencia Artificial', 'Stuart Russell', 2021, 1),
('L007', 'Diseño de Software', 'Ian Sommerville', 2019, 1),
('L008', 'Compiladores', 'Aho & Ullman', 2017, 1),
('L009', 'Álgebra Lineal', 'Gilbert Strang', 2012, 1),
('L010', 'Redes de Computadoras', 'Andrew Tanenbaum', 2019, 1);

INSERT INTO Prestamo (CodigoLibro, ID_Usuario, FechaPrestamo, FechaDevolucion) VALUES
('L001', 'U001', '2025-01-10', '2025-01-17'),
('L002', 'U003', '2025-01-11', '2025-01-18'),
('L003', 'U005', '2025-01-12', '2025-01-19'),
('L004', 'U002', '2025-01-13', '2025-01-20'),
('L005', 'U006', '2025-01-14', '2025-01-21'),
('L006', 'U004', '2025-01-15', '2025-01-22'),
('L007', 'U009', '2025-01-16', '2025-01-23'),
('L008', 'U010', '2025-01-17', '2025-01-24'),
('L009', 'U007', '2025-01-18', '2025-01-25'),
('L010', 'U008', '2025-01-19', '2025-01-26');

SELECT * FROM USUARIO
SELECT * FROM Libro
SELECT * FROM Prestamo