package com.sportpulse.domain.model;

/**
 * Enum que representa los estados posibles de un partido
 * Invariante: NOT_STARTED → LIVE → FINISHED (no se puede retroceder)
 */
public enum MatchStatus {
    NOT_STARTED("No iniciado"),
    LIVE("En vivo"),
    FINISHED("Finalizado");
    
    private final String description;
    
    MatchStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Verifica si la transición de estado es válida
     */
    public boolean canTransitionTo(MatchStatus newStatus) {
        return switch (this) {
            case NOT_STARTED -> newStatus == LIVE;
            case LIVE -> newStatus == FINISHED;
            case FINISHED -> false; // No se puede cambiar desde FINISHED
        };
    }
} 