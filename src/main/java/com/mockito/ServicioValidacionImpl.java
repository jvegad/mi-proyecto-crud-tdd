package com.mockito;

/*Se crea esta clase para incorporar Mockito */
public class ServicioValidacionImpl implements ServicioValidacion {

    @Override
    public void validar(Double nota) {
        if (nota == null || nota < 0 || nota > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10");
        }
    }
}
