package com.mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CursoServiceTest {

    private CursoService servicio;

    @BeforeAll
    static void simulaBD() {
        System.out.println("CREATE TABLE notas (id INTEGER PRIMARY KEY AUTOINCREMENT,valor REAL NOT NULL);");
    }

    @BeforeEach
    void setUp() {
        servicio = new CursoService();
    }

    @Test
    void agregarNota_deberiaAgregarNotaALista() {
        System.out.println("/*Test N°1 --------------------*/");
        servicio.agregarNota(8.5);
        assertEquals(8.5, servicio.obtenerNota(0));
    }

    @Test
    void agregarNota_deberiaLanzarExcepcionSiNotaInvalida() {
        System.out.println("/*Test N°2 --------------------*/");
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(-1.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(11.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(null));
    }

    @Test
    void obtenerNota_deberiaRetornarNotaCorrecta() {
        System.out.println("/*Test N°3 --------------------*/");
        servicio.agregarNota(6.0);
        servicio.agregarNota(9.0);

        assertEquals(6.0, servicio.obtenerNota(0));
        assertEquals(9.0, servicio.obtenerNota(1));
    }

    @Test
    void obtenerNota_deberiaLanzarExcepcionSiIndiceInvalido() {
        System.out.println("/*Test N°4 --------------------*/");
        assertThrows(IndexOutOfBoundsException.class, () -> servicio.obtenerNota(0));
    }

    @Test
    void obtenerTodasLasNotas_deberiaRetornarListaCompleta() {
        System.out.println("/*Test N°5 --------------------*/");
        servicio.agregarNota(7.5);
        servicio.agregarNota(8.0);

        List<Double> notas = servicio.obtenerTodasLasNotas();

        assertEquals(2, notas.size());
        assertEquals(7.5, notas.get(0));
        assertEquals(8.0, notas.get(1));
    }

    @Test
    void actualizarNota_deberiaActualizarNotaCorrectamente() {
        System.out.println("/*Test N°6 --------------------*/");
        servicio.agregarNota(5.0);
        servicio.actualizarNota(0, 9.0);

        assertEquals(9.0, servicio.obtenerNota(0));
    }

    @Test
    void actualizarNota_deberiaLanzarExcepcionSiIndiceOValorInvalido() {
        System.out.println("/*Test N°7 --------------------*/");
        servicio.agregarNota(5.0); // Solo existe el índice 0
        assertThrows(IndexOutOfBoundsException.class, () -> servicio.actualizarNota(1, 8.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, 11.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, null));
    }

    @Test
    void eliminarNota_deberiaRemoverNotaCorrectamente() {
        System.out.println("/*Test N°8 --------------------*/");
        servicio.agregarNota(7.0); // Índice 0
        servicio.agregarNota(8.0); // Índice 1

        servicio.eliminarNota(0);
        assertEquals(1, servicio.obtenerTodasLasNotas().size());
        assertEquals(8.0, servicio.obtenerNota(0));
    }

    @Test
    void eliminarNota_deberiaLanzarExcepcionSiIndiceInvalido() {
        System.out.println("/*Test N°9 --------------------*/");
        assertThrows(IndexOutOfBoundsException.class, () -> servicio.eliminarNota(0));
    }

    @Test
    void calcularPromedio_deberiaRetornarPromedioCorrecto() {
        System.out.println("/*Test N°10 --------------------*/");
        servicio.agregarNota(10.0);
        servicio.agregarNota(8.0);
        servicio.agregarNota(6.0);

        assertEquals(8.0, servicio.calcularPromedio(), 0.01);
    }

    @Test
    void calcularPromedio_deberiaLanzarExcepcionSiListaVacia() {
        System.out.println("/*Test N°11 --------------------*/");
        assertThrows(IllegalStateException.class, () -> servicio.calcularPromedio());
    }
}
