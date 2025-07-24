# Reporte Módulos 2 y 3: CRUD con TDD y Testing Ágil

## Integrantes
*   **Viki Borda** (GitHub: McKingston01)
*   **Raúl Salas** (GitHub: DevRSH)
*   **Josselyn Vega** (GitHub: jvegad)

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

### Simulación de Base de Datos SQL
Para cumplir con el requisito de interacción con SQL sin añadir la complejidad de una base de datos real, el sistema **simula las operaciones SQL imprimiendo las sentencias en la consola** cada vez que se ejecuta una operación (INSERT, SELECT, UPDATE, DELETE).

**Instrucciones de uso:**
1.  Ejecuta los tests con `mvn clean test`.
2.  Copia las sentencias SQL impresas en la consola.
3.  Pega las sentencias en un cliente de SQL online como **[SQLite Online](https://sqliteonline.com/)** para verificar su correcta sintaxis y lógica.

---

## 3. Documentación de los Ciclos TDD - Desarrollo Guiado por Pruebas (TDD) - CursoService

El desarrollo del proyecto siguió 12 ciclos TDD. A continuación se detalla el proceso completo que dio forma a las funcionalidades CRUD, refinó el código y manejó los casos de error.

<details>
<summary><strong>Haz clic para ver los 12 Ciclos TDD</strong></summary>

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

### Ciclo 12: (🔴🟢🔵 - Prueba de Comportamiento con `verify`)
-   Se escribió un nuevo test para verificar no un resultado, sino un **comportamiento**: que `CursoService` efectivamente llama al método `validar` de su dependencia. Se usó `verify(validadorMock).validar(nota)`. La prueba pasó inmediatamente, confirmando que la refactorización fue exitosa y la colaboración entre objetos funciona como se esperaba.

```java
@Test
void agregarNota_deberiaLlamarAlValidador() {
    doNothing().when(validadorMock).validar(7.5);
    servicio.agregarNota(7.5);
    verify(validadorMock).validar(7.5);
}
```

</details>

---

## Resultado Final

- `CursoService` cumple con SRP
- Código más limpio, probado y desacoplado
- Validación aislada, fácil de modificar o extender

---

## 4 Plan de Testing Ágil (Sprint de Desarrollo del CRUD)

### Objetivo del Sprint

"Al final del Sprint, el equipo habrá desarrollado y probado un sistema CRUD completamente funcional para la gestión de notas, con una base de código cubierta por pruebas unitarias automatizadas y lista para ser integrada".

### Historias de Usuario

<details>
<summary><strong>Haz clic para ver las Historias de Usuario</strong></summary>

#### HU-01: Listar todas las notas del curso
*   **Como** profesor,
*   **quiero** poder ver un listado completo de todas las notas que he agregado,
*   **para** tener una visión general y rápida del progreso de la clase.

#### HU-02: Consultar una nota específica por su posición
*   **Como** profesor,
*   **quiero** poder obtener una nota específica ingresando su posición en la lista,
*   **para** verificar o recordar un valor puntual sin tener que revisar el listado completo.

#### HU-03: Obtener el promedio de las notas
*   **Como** profesor,
*   **quiero** poder calcular el promedio de todas las notas ingresadas,
*   **para** evaluar el rendimiento general del curso de forma cuantitativa.

#### HU-04: Eliminar una nota específica
*   **Como** profesor,
*   **quiero** poder eliminar una nota específica de la lista,
*   **para** remover registros que fueron ingresados por error o que ya no son válidos.

#### HU-05: Manejar la eliminación de una nota inexistente
*   **Como** profesor,
*   **quiero** que el sistema me alerte si intento eliminar una nota que no existe,
*   **para** evitar errores inesperados y entender por qué la operación no se pudo completar.

#### HU-06: Agregar nueva nota de estudiante  
*   **Como** profesor del curso,  
*   **quiero** poder registrar nuevas notas de estudiantes en el sistema,  
*   **para** mantener actualizado el registro académico de cada alumno.  

#### HU-07: Actualizar nota existente  
*   **Como** profesor del curso,  
*   **quiero** poder modificar notas ya registradas,  
*   **para** corregir errores o actualizar evaluaciones.  

</details>

---

### Criterios de Aceptación

<details>
<summary><strong>Haz clic para ver los Criterios de Aceptación</strong></summary>

#### AC 1 (HU-01): Listado con notas existentes
*   **Dado** que he agregado varias notas al sistema (ej. 7.0, 4.0, 6.5).
*   **Cuando** solicito el listado de todas las notas.
*   **Entonces** el sistema debe devolver una lista que contenga exactamente esas tres notas.

#### AC 2 (HU-01): Listado cuando no hay notas
*   **Dado** que no he agregado ninguna nota al sistema.
*   **Cuando** solicito el listado de todas las notas.
*   **Entonces** el sistema debe devolver una lista vacía, sin generar ningún error.

#### AC 3 (HU-02): Consulta de una nota existente
*   **Dado** que existen las notas [6.0, 4.5, 7.0] en las posiciones 0, 1 y 2 respectivamente.
*   **Cuando** solicito la nota en la posición 1.
*   **Entonces** el sistema debe devolver el valor 4.5.

#### AC 4 (HU-02): Consulta con una posición inválida
*   **Dado** que existen 3 notas en el sistema (posiciones 0, 1, 2).
*   **Cuando** intento solicitar la nota en una posición que no existe (ej. posición 3 o posición -1).
*   **Entonces** el sistema debe informar de un error de "índice fuera de rango".

#### AC 5 (HU-03): Calcular promedio con notas existentes
*   **Dado** que he agregado las notas [7.0, 5.0, 6.0].
*   **Cuando** solicito el cálculo del promedio.
*   **Entonces** el sistema debe devolver el valor 6.0.

#### AC 6 (HU-03): Intentar calcular promedio sin notas
*   **Dado** que no hay ninguna nota registrada en el sistema.
*   **Cuando** intento solicitar el cálculo del promedio.
*   **Entonces** el sistema debe informar de un error indicando que la operación no se puede realizar porque no hay notas.

#### AC 7 (HU-04): Eliminación exitosa de una nota
*   **Dado** que tengo la lista de notas [5.0, 6.5, 4.0].
*   **Cuando** elijo eliminar la nota en la posición 1 (el 6.5).
*   **Entonces** la lista de notas resultante debe ser [5.0, 4.0].

#### AC 8 (HU-05): Intento de eliminación con índice fuera de rango
*   **Dado** que tengo una lista con 2 notas (en posiciones 0 y 1).
*   **Cuando** intento eliminar la nota en la posición 5.
*   **Entonces** el sistema debe notificarlo con un error de "índice fuera de rango" y la lista original no debe ser modificada.

#### AC 9 (HU-06): Validación de campos obligatorios
*   **Dado** que quiero registrar una nueva nota,
*   **Cuando** no lleno uno o más campos obligatorios (ID, nombre, asignatura o nota),
*   **Entonces** el sistema debe rechazar el registro y mostrar un mensaje indicando qué campos faltan.

#### AC 10 (HU-06): Validación del rango de nota
*   **Dado** que intento registrar una nota fuera del rango permitido (ej. menor a 0 o mayor a 5),
*   **Cuando** envío los datos,
*   **Entonces** el sistema debe rechazar la entrada y notificar el error con un mensaje claro.

#### AC 11 (HU-06): Registro exitoso
*   **Dado** que todos los datos son válidos,
*   **Cuando** registro una nueva nota,
*   **Entonces** el sistema debe guardarla correctamente, mostrar una confirmación y actualizar la lista de inmediato.

#### AC 12 (HU-06): Evitar notas duplicadas por asignatura
*   **Dado** que un estudiante ya tiene una nota registrada para una asignatura,
*   **Cuando** intento registrar otra nota para esa misma asignatura,
*   **Entonces** el sistema debe notificarme y sugerir actualizar la nota existente.

#### AC 13 (HU-07): Selección de nota para edición
*   **Dado** que tengo varias notas registradas,
*   **Cuando** quiero modificar una nota,
*   **Entonces** el sistema debe permitirme filtrarla por ID, estudiante o asignatura.

#### AC 14 (HU-07): Validación durante la edición
*   **Dado** que edito una nota existente,
*   **Cuando** ingreso un valor fuera del rango o dejo campos vacíos,
*   **Entonces** el sistema debe validar y mostrar los errores correspondientes sin guardar los cambios.

#### AC 15 (HU-07): Confirmación de cambios exitosos
*   **Dado** que edito una nota con datos válidos,
*   **Cuando** guardo los cambios,
*   **Entonces** el sistema debe actualizar la base de datos, mostrar un mensaje de éxito y reflejar la nota actualizada en la lista.

#### AC 16 (HU-07): Manejo de errores si la nota no existe
*   **Dado** que intento editar una nota con un ID que no corresponde a ningún registro,
*   **Cuando** realizo la operación,
*   **Entonces** el sistema debe informar que la nota no existe y no permitir la edición.

</details>

### Tipos de Pruebas a Realizar
*   **Pruebas Unitarias:** Se ejecutan continuamente durante el desarrollo usando JUnit y Mockito. Verifican que cada método de `CursoService` funcione correctamente de forma aislada. Estas son la base de nuestra estrategia y se crean siguiendo TDD.
*   **Pruebas de Integración:** Se realizan al final de una funcionalidad para verificar que los componentes colaboran correctamente. En este proyecto, se simulan probando las sentencias SQL generadas en un cliente externo.
*   **Pruebas de Aceptación:** Se pueden realizar al final del Sprint, ejecutando los Criterios de Aceptación de forma manual para validar que las Historias de Usuario se cumplieron por completo.

### Roles Involucrados en el Sprint
*   **Desarrolladores (Josselyn, Viki):** Responsables de escribir tanto las pruebas (RED) como el código de producción (GREEN), y de refactorizar (REFACTOR). También se encargan de la integración del código y de mantener el `BUILD` del proyecto siempre en verde.
*   **Product Owner (Simulado):** Responsable de definir y priorizar las Historias de Usuario y sus Criterios de Aceptación.
*   **QA / Tester (Rol asumido por los desarrolladores):** Responsables de las Pruebas de Aceptación al final del Sprint para dar el visto bueno a las funcionalidades.

### Definición de "Terminado" (Definition of Done)

Una Historia de Usuario se considera **"Terminada"** cuando:
*   El código está escrito y cumple con los Criterios de Aceptación.
*   Todas las pruebas unitarias asociadas pasan (`mvn test` exitoso).
*   La cobertura de código de la nueva funcionalidad es igual o superior al 80%.
*   El código ha sido refactorizado y no hay duplicación evidente.
*   El código ha sido subido al repositorio de Git en la rama principal.

---

## 5. Reflexión Personal

### Viki Borda
Reflexión Personal
Este proyecto me permitió comprender la esencia del Testing Ágil y TDD: desarrollar con confianza, asegurando cada funcionalidad mediante pruebas automatizadas desde el inicio. Aprendí que escribir tests primero (ciclo RED-GREEN-REFACTOR) no solo valida la lógica, sino que también guía un diseño más limpio y desacoplado. Las herramientas como JUnit, Mockito y JaCoCo fueron fundamentales para medir la calidad y simular dependencias, aunque inicialmente tuve dificultades para configurar los mocks y lograr una cobertura alta. Por ejemplo, al simular la base de datos con logs de SQL, debimos iterar varias veces para asegurar que las pruebas reflejaran el comportamiento real.
Trabajar con ciclos TDD fue al principio un desafío por la disciplina que exige (escribir pruebas antes que el código), pero luego noté sus ventajas: menos bugs y mayor mantenibilidad. Sin embargo, en etapas complejas (como validaciones cruzadas de notas), el proceso se volvió lento y requirió ajustes en los tests. Si repitiera el proyecto, mejoraría la planificación de los tests de integración (usando una BD real en lugar de logs) y dedicaría más tiempo a refactorizar, especialmente para reducir duplicación en las validaciones. Pese a los retos, esta experiencia reforzó que el TDD y el Testing Ágil son pilares para entregar software robusto y adaptable.

### Raúl Salas
En desarrollo

### Josselyn Vega
*   **¿Qué aprendiste sobre Testing Ágil y TDD durante el desarrollo?**
    Que TDD no es solo escribir pruebas, sino una metodología de diseño, que obliga a pensar primero en los requisitos y casos de uso antes de escribir líneas de código funcional. Esto resultó en un código más fácil de mantener.

*   **¿Qué dificultades enfrentaste y cómo las resolviste?**
    La compatibilidad entre las versiones recientes de Java (Java 24), Mockito y JaCoCo. Lo cual resolvimos actualizando las versiones de los plugins en el `pom.xml`.

*   **¿Cómo te sentiste trabajando con ciclos TDD?**
    El ciclo RED-GREEN-REFACTOR se siente lento, pero lo bueno es que cada cambio estaba respaldado por una red de pruebas, entonces uno puede refactorizar con confianza, sin miedo a romper funcionalidades existentes.

*   **¿Qué mejorarías si volvieras a realizar este proyecto?**
    Si volviera a hacerlo, planificaría mejor la refactorización y dedicaría más tiempo a configurar correctamente las versiones de las dependencias desde el inicio para evitar problemas de compatibilidad.