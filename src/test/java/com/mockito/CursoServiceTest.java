package com.tdd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class CursoServiceTest {

    @Test
    void testCalcularPromedio() {
        CursoService servicio = new CursoService();
        List<Double> notas = Arrays.asList(4.0, 5.0, 6.0);

        double resultado = servicio.calcularPromedio(notas);

        assertEquals(5.0, resultado);
    }

    @Test
    void testVerificarCupos() {
        CursoService servicio = new CursoService(3); // capacidad 3 alumnos

        servicio.setAlumnosInscritos(2);
        assertTrue(servicio.verificarCupos());

        servicio.setAlumnosInscritos(3);
        assertFalse(servicio.verificarCupos());
    }

    @Test
    void testCalcularPromedioListaVacia() {
        CursoService servicio = new CursoService();

        List<Double> notasVacias = List.of();

        assertThrows(IllegalArgumentException.class, () -> {
            servicio.calcularPromedio(notasVacias);
        });
    }

    @Test
    void testCalcularPromedioNulo() {
        CursoService servicio = new CursoService();

        assertThrows(NullPointerException.class, () -> {
            servicio.calcularPromedio(null);
        });
    }
}
