package com.sportpulse.domain.model;

import lombok.Value;

/**
 * Value Object that represents a sports team
 * Immutable and without identity
 */
@Value
public class Team {
    String name;
    String code;
    
    public Team(String name, String code) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del equipo no puede estar vacío");
        }
        this.name = name.trim();
        this.code = code.trim().toUpperCase();
    }
} 