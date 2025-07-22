# Desarrollo Guiado por Pruebas (TDD) - CursoService

## Ciclo 1: Crear la funcionalidad de agregar una nota (el "camino feliz")

🔴 **RED: Escribir una prueba que falle**

El primer paso es probar la funcionalidad más básica: agregar una nota y verificar que se haya guardado. La prueba fallará porque ni la clase `CursoService` ni el método `agregarNota` existen todavía.

```java
// En CursoServiceTest.java
@Test
void agregarNota_deberiaAgregarNotaALista() {
    CursoService servicio = new CursoService();
    servicio.agregarNota(8.5);
    assertEquals(8.5, servicio.obtenerNota(0));
}
```

**Resultado**: Error de compilación. `CursoService cannot be resolved to a type`.

🟢 **GREEN: Escribir el código MÍNIMO para que la prueba pase**

```java
// En CursoService.java
import java.util.ArrayList;
import java.util.List;

public class CursoService {
    private final List<Double> notas = new ArrayList<>();

    public void agregarNota(Double nota) {
        notas.add(nota);
    }

    public Double obtenerNota(int index) {
        return notas.get(index);
    }
}
```

**Resultado**: La prueba ahora pasa. ¡Estamos en verde!

🔵 **REFACTOR: Mejorar el código sin romper la prueba**

El código actual es muy simple. No hay duplicación ni complejidad que requiera una mejora inmediata.

**Cambio**: No se necesita refactorización en este ciclo. La prueba sigue pasando.

---

## Ciclo 2: Probar qué pasa al obtener una nota con un índice inválido

🔴 **RED: Escribir una prueba que falle**

¿Qué debería pasar si intentamos obtener una nota de una lista vacía? Debería lanzar una excepción.

```java
// En CursoServiceTest.java
@Test
void obtenerNota_deberiaLanzarExcepcionSiIndiceInvalido() {
    CursoService servicio = new CursoService();
    assertThrows(IndexOutOfBoundsException.class, () -> servicio.obtenerNota(0));
}
```

**Resultado**: La prueba pasa, pero por la razón "incorrecta". La excepción es lanzada directamente por `notas.get(index)`.

🟢 **GREEN: Escribir el código MÍNIMO para que la prueba pase**

```java
// En CursoService.java
public Double obtenerNota(int index) {
    validarIndice(index);
    return notas.get(index);
}

// Nuevo método privado
private void validarIndice(int index) {
    if (index < 0 || index >= notas.size()) {
        throw new IndexOutOfBoundsException("Índice fuera de rango");
    }
}
```

**Resultado**: La prueba sigue pasando, pero ahora la lógica de validación está bajo nuestro control.

🔵 **REFACTOR: Mejorar el código**

Hemos creado un método privado (`validarIndice`) que puede ser reutilizado por otras funciones.

**Cambio**: El código ya está refactorizado. La prueba sigue pasando.

---

## Ciclo 3: Validar que no se puedan agregar notas inválidas

🔴 **RED: Escribir una prueba que falle**

```java
// En CursoServiceTest.java
@Test
void agregarNota_deberiaLanzarExcepcionSiNotaInvalida() {
    CursoService servicio = new CursoService();
    assertThrows(IllegalArgumentException.class, () -> servicio.agregarNota(-1.0));
}
```

**Resultado**: La prueba falla. No se lanza ninguna excepción.

🟢 **GREEN: Escribir el código MÍNIMO para que la prueba pase**

```java
// En CursoService.java
public void agregarNota(Double nota) {
    if (nota == null || nota < 0 || nota > 10) {
        throw new IllegalArgumentException("La nota debe estar entre 0 y 10");
    }
    notas.add(nota);
}
```

**Resultado**: La prueba ahora pasa.

🔵 **REFACTOR: Mejorar el código**

Se anota una deuda técnica por duplicación futura en `actualizarNota`.

---

## Ciclo 4: Crear la funcionalidad de listar todas las notas

🔴 **RED: Escribir una prueba que falle**

```java
// En CursoServiceTest.java
@Test
void obtenerTodasLasNotas_deberiaRetornarListaCompleta() {
    CursoService servicio = new CursoService();
    servicio.agregarNota(7.5);
    servicio.agregarNota(8.0);

    List<Double> notas = servicio.obtenerTodasLasNotas();

    assertEquals(2, notas.size());
    assertEquals(7.5, notas.get(0));
}
```

**Resultado**: Error de compilación.

🟢 **GREEN: Escribir el código MÍNIMO para que la prueba pase**

```java
// En CursoService.java
public List<Double> obtenerTodasLasNotas() {
    return notas;
}
```

**Resultado**: La prueba pasa.

🔵 **REFACTOR: Mejorar el código (Encapsulación)**

```java
// En CursoService.java
public List<Double> obtenerTodasLasNotas() {
    return new ArrayList<>(notas);
}
```

**Cambio**: La prueba sigue pasando. Lista protegida.

---

## Ciclo 5: Crear la funcionalidad de actualizar una nota

🔴 **RED: Escribir una prueba que falle**

```java
// En CursoServiceTest.java
@Test
void actualizarNota_deberiaActualizarNotaCorrectamente() {
    CursoService servicio = new CursoService();
    servicio.agregarNota(5.0);
    servicio.actualizarNota(0, 9.0);
    assertEquals(9.0, servicio.obtenerNota(0));
}
```

**Resultado**: Error de compilación.

🟢 **GREEN: Escribir el código MÍNIMO para que la prueba pase**

```java
// En CursoService.java
public void actualizarNota(int index, Double nuevaNota) {
    notas.set(index, nuevaNota);
}
```

**Resultado**: La prueba pasa.

🔵 **REFACTOR: Mejorar el código (Reutilización)**

```java
public void actualizarNota(int index, Double nuevaNota) {
    validarIndice(index);
    if (nuevaNota == null || nuevaNota < 0 || nuevaNota > 10) {
        throw new IllegalArgumentException("La nueva nota debe estar entre 0 y 10");
    }
    notas.set(index, nuevaNota);
}
```

**Cambio**: Se reutiliza validación. La prueba sigue pasando.

---

## Ciclo 6: Eliminar una nota

🔴 **RED**: Escribir un test que agregue dos notas, elimine una y verifique el tamaño.

🟢 **GREEN**: Crear el método `eliminarNota(int index)`.

```java
public void eliminarNota(int index) {
    validarIndice(index);
    notas.remove(index);
}
```

🔵 **REFACTOR**: Ya se reutiliza `validarIndice`.

---

## Ciclo 7: Calcular el promedio de las notas

🔴 **RED**: Escribir un test con varias notas y esperar promedio.

🟢 **GREEN**: Crear el método `calcularPromedio()`.

```java
public double calcularPromedio() {
    return notas.stream()
        .mapToDouble(Double::doubleValue)
        .average()
        .orElse(0.0);
}
```

🔵 **REFACTOR**: Uso de Streams. Código legible.

---

## Ciclo 8: Validar el cálculo del promedio en una lista vacía

🔴 **RED**: Esperar excepción si no hay notas.

🟢 **GREEN**:

```java
public double calcularPromedio() {
    if (notas.isEmpty()) {
        throw new IllegalStateException("No hay notas");
    }
    return notas.stream()
        .mapToDouble(Double::doubleValue)
        .average()
        .orElse(0.0);
}
```

🔵 **REFACTOR**: No se necesita. Prueba pasa.

---

## Ciclos 9-12: Refactorización Mayor - Separación de Responsabilidades

### Ciclo 9 (🔴 RED)

- Crear interfaz `ServicioValidacion`
- Cambiar constructor de `CursoService` para recibir esta dependencia
- Todas las pruebas fallan: "rojo de refactorización"

```java
public interface ServicioValidacion {
    void validar(Double nota);
}
```

---

### Ciclo 10 (🟢 GREEN)

- Usar Mockito en pruebas
- Inyectar `ServicioValidacion` con `@Mock` y `@InjectMocks`
- Ajustar pruebas para usar `doNothing().when(...)`

```java
@ExtendWith(MockitoExtension.class)
class CursoServiceTest {
    @Mock ServicioValidacion validadorMock;
    @InjectMocks CursoService servicio;

    @Test
    void agregarNota_deberiaAgregarNotaALista() {
        doNothing().when(validadorMock).validar(8.5);
        servicio.agregarNota(8.5);
        assertEquals(8.5, servicio.obtenerNota(0));
    }
}
```

---

### Ciclo 11 (🔵 REFACTOR)

- Crear clase `ServicioValidacionImpl`
- Mover lógica de validación allí

```java
public class ServicioValidacionImpl implements ServicioValidacion {
    public void validar(Double nota) {
        if (nota == null || nota < 0 || nota > 10) {
            throw new IllegalArgumentException("Nota debe estar entre 0 y 10");
        }
    }
}
```

- `CursoService` ahora solo llama a `validador.validar(nota)`

---

### Ciclo 12: Prueba de comportamiento con `verify`

```java
@Test
void agregarNota_deberiaLlamarAlValidador() {
    doNothing().when(validadorMock).validar(7.5);
    servicio.agregarNota(7.5);
    verify(validadorMock).validar(7.5);
}
```

---

## Resultado Final

- `CursoService` cumple con SRP
- Código más limpio, probado y desacoplado
- Validación aislada, fácil de modificar o extender
