# Reporte Módulos 2 y 3: CRUD con TDD y Testing Ágil

## Integrantes
*   Viki Borda
*   Raúl Salas
*   Josselyn Vega

---

## 1. Resumen del Proyecto

Este proyecto consiste en el desarrollo de un sistema CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar las notas de un curso. La implementación se realizó siguiendo la metodología de **Test-Driven Development (TDD)**, asegurando que cada pieza de código funcional estuviera respaldada por una prueba automatizada antes de ser escrita.

El objetivo principal fue integrar los principios del **Testing Ágil** con la práctica de la automatización en Java, simulando un flujo de trabajo profesional utilizado en equipos Scrum.

*   **Tecnologías Utilizadas:**
    *   **Lenguaje:** Java (Versión 24)
    *   **Gestión de Dependencias:** Apache Maven
    *   **Framework de Pruebas:** JUnit 5
    *   **Simulación de Dependencias (Mocks):** Mockito
    *   **Medición de Cobertura:** JaCoCo

---

## 2. Instrucciones de Configuración y Ejecución

A continuación se detallan los pasos para clonar, compilar y probar el proyecto.

### Prerrequisitos
*   JDK 24 (o compatible) instalado.
*   Apache Maven instalado y configurado en el PATH del sistema.
*   Git instalado.

### Pasos para la Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/jvegad/mi-proyecto-crud-tdd
    cd mi-proyecto-crud-tdd
    ```

2.  **Compilar y ejecutar las pruebas:**
    El siguiente comando limpiará compilaciones anteriores, compilará el código y ejecutará todas las pruebas unitarias.
    ```bash
    mvn clean test
    ```
    Al finalizar, deberías ver un `BUILD SUCCESS` en la consola.

3.  **Generar el reporte de cobertura:**
    Para generar el reporte de cobertura de JaCoCo, se recomienda el siguiente comando que asegura la ejecución de las pruebas más recientes antes de generar el reporte:
    ```bash
    mvn clean verify
    ```
    El reporte HTML completo se encontrará en la siguiente ruta: `target/site/jacoco/index.html`

---

## 3. Documentación de los Ciclos TDD - Desarrollo Guiado por Pruebas (TDD) - CursoService

El desarrollo del proyecto siguió 12 ciclos TDD. A continuación se detalla el proceso completo que dio forma a las funcionalidades CRUD, refinó el código y manejó los casos de error.

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

---

## 4 Refactorización y Aplicación de Principios SOLID

En desarrollo
---

## 5 Plan de Testing Ágil (Sprint de Desarrollo del CRUD)

### Objetivo del Sprint

"Al final del Sprint, el equipo habrá desarrollado y probado un sistema CRUD completamente funcional para la gestión de notas, con una base de código cubierta por pruebas unitarias automatizadas y lista para ser integrada".

### Historias de Usuario

En desarrollo

### Criterios de Aceptación

En desarrollo

### Tipos de Pruebas a Realizar
*   **Pruebas Unitarias:** Se ejecutan continuamente durante el desarrollo usando JUnit y Mockito. Verifican que cada método de `CursoService` funcione correctamente de forma aislada. Estas son la base de nuestra estrategia y se crean siguiendo TDD.
*   **Pruebas de Integración:** Se puede realizar al final del desarrollo de una funcionalidad para verificar que `CursoService` interactúa correctamente con una base de datos real (en este caso, la simulación de SQL, copiando los SQL impresos en la consola, y probando en `https://sqliteonline.com/`).

### Definición de "Terminado" (Definition of Done)

Una Historia de Usuario se considera **"Terminada"** cuando:
*   El código está escrito y cumple con los Criterios de Aceptación.
*   Todas las pruebas unitarias asociadas pasan (`mvn test` exitoso).
*   La cobertura de código de la nueva funcionalidad es igual o superior al 80%.
*   El código ha sido refactorizado y no hay duplicación evidente.
*   El código ha sido subido al repositorio de Git en la rama principal.

---

## 6. Reflexión Personal

En desarrollo