package com.tdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CursoService {

    private final List<Double> notas;

    public CursoService() {
        this.notas = new ArrayList<>();
    }

    // CREATE - Agregar nueva nota
    public void agregarNota(Double nota) {
        // TEST: agregarNota_deberiaAgregarNotaALista
        if (nota == null || nota < 0 || nota > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10");
        }
        notas.add(nota);
    }

    // READ - Obtener una nota por índice
    public Double obtenerNota(int index) {
        // TEST: obtenerNota_deberiaRetornarNotaCorrecta
        validarIndice(index);
        return notas.get(index);
    }

    // READ - Obtener todas las notas
    public List<Double> obtenerTodasLasNotas() {
        // TEST: obtenerTodasLasNotas_deberiaRetornarListaCompleta
        return new ArrayList<>(notas); // para evitar modificación externa
    }

    // UPDATE - Modificar una nota existente
    public void actualizarNota(int index, Double nuevaNota) {
        // TEST: actualizarNota_deberiaActualizarNotaCorrectamente
        validarIndice(index);
        if (nuevaNota == null || nuevaNota < 0 || nuevaNota > 10) {
            throw new IllegalArgumentException("La nueva nota debe estar entre 0 y 10");
        }
        notas.set(index, nuevaNota);
    }

    // DELETE - Eliminar una nota por índice
    public void eliminarNota(int index) {
        // TEST: eliminarNota_deberiaRemoverNotaCorrectamente
        validarIndice(index);
        notas.remove(index);
    }

    // Calcular promedio
    public double calcularPromedio() {
        // TEST: calcularPromedio_deberiaRetornarPromedioCorrecto
        if (notas.isEmpty()) {
            throw new IllegalStateException("No hay notas para calcular el promedio");
        }
        return notas.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
    }

    // Validar que el índice esté dentro de los límites
    private void validarIndice(int index) {
        if (index < 0 || index >= notas.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
    }
}
