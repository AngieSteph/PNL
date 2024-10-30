package com.pnl.municipio.validator;

import com.pnl.municipio.entity.Denuncia;
import com.pnl.municipio.exception.ValidateException;

public class DenunciaValidator {
    
    public static void validate(Denuncia registro) {
        if (registro.getTitulo() == null || registro.getTitulo().trim().isEmpty()) {
            throw new ValidateException("El título es requerido");
        }
        if (registro.getTitulo().length() > 100) {
            throw new ValidateException("El título no debe exceder los 100 caracteres");
        }
        if (registro.getUbicacion() == null || registro.getUbicacion().trim().isEmpty()) {
            throw new ValidateException("La ubicación es requerida");
        }
        if (registro.getUbicacion().length() > 150) {
            throw new ValidateException("La ubicación no debe exceder los 150 caracteres");
        }
        if (registro.getEstado() == null || registro.getEstado().trim().isEmpty()) {
            throw new ValidateException("El estado es requerido");
        }
        if (registro.getCiudadano() == null || registro.getCiudadano().trim().isEmpty()) {
            throw new ValidateException("El nombre del ciudadano es requerido");
        }
        if (registro.getCiudadano().length() > 100) {
            throw new ValidateException("El nombre del ciudadano no debe exceder los 100 caracteres");
        }
        if (registro.getTelefonoCiudadano() == null || registro.getTelefonoCiudadano().trim().isEmpty()) {
            throw new ValidateException("El teléfono del ciudadano es requerido");
        }
        if (registro.getTelefonoCiudadano().length() > 15) {
            throw new ValidateException("El teléfono del ciudadano no debe exceder los 15 caracteres");
        }
    }
}