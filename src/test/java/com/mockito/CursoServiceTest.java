package com.mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyDouble;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private ServicioValidacion validadorMock;
    @InjectMocks
    private CursoService servicio;

    @BeforeAll
    static void simulaBD() {
        System.out.println("CREATE TABLE notas (id INTEGER PRIMARY KEY AUTOINCREMENT,valor REAL NOT NULL);");
    }

    /*@BeforeEach
    void setUp() {
        servicio = new CursoService();
    }*/
    @Test
    void agregarNota_deberiaAgregarNotaALista() {
        System.out.println("/*Test N°1 --------------------*/");
        Double notaValida = 8.5;
        doNothing().when(validadorMock).validar(notaValida);
        servicio.agregarNota(notaValida);
        assertEquals(notaValida, servicio.obtenerNota(0));
        verify(validadorMock).validar(notaValida);
    }

    @Test
    void agregarNota_deberiaLanzarExcepcionSiNotaInvalida() {
        System.out.println("/*Test N°2 --------------------*/");
        Double notaInvalida = -1.0;
        doThrow(new IllegalArgumentException("Nota inválida")).when(validadorMock).validar(notaInvalida);
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(notaInvalida));
        verify(validadorMock).validar(notaInvalida);
    }

    @Test
    void obtenerNota_deberiaRetornarNotaCorrecta() {
        System.out.println("/*Test N°3 --------------------*/");
        doNothing().when(validadorMock).validar(anyDouble());
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
        doNothing().when(validadorMock).validar(anyDouble());
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
        doNothing().when(validadorMock).validar(anyDouble());
        servicio.agregarNota(5.0);
        servicio.actualizarNota(0, 9.0);

        assertEquals(9.0, servicio.obtenerNota(0));
    }

    @Test
    void actualizarNota_deberiaLanzarExcepcionSiIndiceOValorInvalido() {
        System.out.println("/*Test N°7 --------------------*/");
        doNothing().when(validadorMock).validar(5.0);
        servicio.agregarNota(5.0);
        // Probar índice inválido
        assertThrows(IndexOutOfBoundsException.class, () -> servicio.actualizarNota(1, 8.0));
        // Probar valor inválido
        doThrow(new IllegalArgumentException()).when(validadorMock).validar(-1.0);
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarNota(0, -1.0));
    }

    @Test
    void eliminarNota_deberiaRemoverNotaCorrectamente() {
        System.out.println("/*Test N°8 --------------------*/");
        doNothing().when(validadorMock).validar(anyDouble());
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
        doNothing().when(validadorMock).validar(anyDouble());
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
