package com.tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CursoServiceTest {

    private CursoService servicio;

    @BeforeEach
    void setUp() {
        servicio = new CursoService();
    }

    @Test
    void agregarNota_deberiaAgregarNotaALista() {
        servicio.agregarNota(8.5);
        assertEquals(8.5, servicio.obtenerNota(0));
    }

    @Test
    void agregarNota_deberiaLanzarExcepcionSiNotaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(-1.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(11.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(null));
    }

    @Test
    void obtenerNota_deberiaRetornarNotaCorrecta() {
        servicio.agregarNota(6.0);
        servicio.agregarNota(9.0);

        assertEquals(6.0, servicio.obtenerNota(0));
        assertEquals(9.0, servicio.obtenerNota(1));
    }

    @Test
    void obtenerNota_deberiaLanzarExcepcionSiIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> servicio.obtenerNota(0));
    }

    @Test
    void obtenerTodasLasNotas_deberiaRetornarListaCompleta() {
        servicio.agregarNota(7.5);
        servicio.agregarNota(8.0);

        List<Double> notas = servicio.obtenerTodasLasNotas();

        assertEquals(2, notas.size());
        assertEquals(7.5, notas.get(0));
        assertEquals(8.0, notas.get(1));
    }

    @Test
    void actualizarNota_deberiaActualizarNotaCorrectamente() {
        servicio.agregarNota(5.0);
        servicio.actualizarNota(0, 9.0);

        assertEquals(9.0, servicio.obtenerNota(0));
    }

    @Test
    void actualizarNota_deberiaLanzarExcepcionSiIndiceOValorInvalido() {
        servicio.agregarNota(5.0);

        assertThrows(IndexOutOfBoundsException.class, () -> servicio.actualizarNota(1, 8.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, 11.0));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, null));
    }

    @Test
    void eliminarNota_deberiaRemoverNotaCorrectamente() {
        servicio.agregarNota(7.0);
        servicio.agregarNota(8.0);

        servicio.eliminarNota(0);

        assertEquals(1, servicio.obtenerTodasLasNotas().size());
        assertEquals(8.0, servicio.obtenerNota(0));
    }

    @Test
    void eliminarNota_deberiaLanzarExcepcionSiIndiceInvalido() {
        assertThrows(IndexOutOfBoundsException.class, () -> servicio.eliminarNota(0));
    }

    @Test
    void calcularPromedio_deberiaRetornarPromedioCorrecto() {
        servicio.agregarNota(10.0);
        servicio.agregarNota(8.0);
        servicio.agregarNota(6.0);

        assertEquals(8.0, servicio.calcularPromedio(), 0.01);
    }

    @Test
    void calcularPromedio_deberiaLanzarExcepcionSiListaVacia() {
        assertThrows(IllegalStateException.class, () -> servicio.calcularPromedio());
    }
}
