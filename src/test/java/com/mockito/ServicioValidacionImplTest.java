package com.mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*Se crea este test para aumentar el % de cobertura del reporte de Jacoco */
class ServicioValidacionImplTest {

    private ServicioValidacionImpl validador;

    @BeforeEach
    void setUp() {
        validador = new ServicioValidacionImpl();
    }

    @Test
    void validar_deberiaLanzarExcepcionParaNotaNula() {
        // Probamos la primera rama del 'if': nota == null
        assertThrows(IllegalArgumentException.class, () -> {
            validador.validar(null);
        });
    }

    @Test
    void validar_deberiaLanzarExcepcionParaNotaNegativa() {
        // Probamos la segunda rama del 'if': nota < 0
        assertThrows(IllegalArgumentException.class, () -> {
            validador.validar(-5.0);
        });
    }

    @Test
    void validar_deberiaLanzarExcepcionParaNotaMayorADiez() {
        // Probamos la tercera rama del 'if': nota > 10
        assertThrows(IllegalArgumentException.class, () -> {
            validador.validar(11.0);
        });
    }

    @Test
    void validar_noDeberiaLanzarExcepcionParaNotaValida() {
        // Probamos el "camino feliz": cuando ninguna condición del 'if' se cumple.
        // Se espera que el método simplemente termine sin lanzar errores.
        assertDoesNotThrow(() -> {
            validador.validar(0.0);
        });
        assertDoesNotThrow(() -> {
            validador.validar(5.5);
        });
        assertDoesNotThrow(() -> {
            validador.validar(10.0);
        });
    }
}
