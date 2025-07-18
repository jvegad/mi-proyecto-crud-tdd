package com.tdd;

import java.util.List;
import java.util.Objects;

public class CursoService {

    private int capacidadMaxima;
    private int alumnosInscritos;

    // Constructor vacío para calcularPromedio
    public CursoService() {}

    // Constructor para verificar cupos
    public CursoService(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.alumnosInscritos = 0;
    }

    public void setAlumnosInscritos(int inscritos) {
        this.alumnosInscritos = inscritos;
    }

    public boolean verificarCupos() {
        return alumnosInscritos < capacidadMaxima;
    }

    public double calcularPromedio(List<Double> notas) {
        Objects.requireNonNull(notas, "La lista de notas no puede ser null");

        if (notas.isEmpty()) {
            throw new IllegalArgumentException("La lista de notas no puede estar vacía");
        }

        return notas.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
    }
}
