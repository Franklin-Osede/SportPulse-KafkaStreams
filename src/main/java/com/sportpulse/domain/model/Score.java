package com.sportpulse.domain.model;

import lombok.Value;

/**
 * Value Object que representa el marcador de un partido
 * Inmutable y con invariantes de dominio
 */
@Value
public class Score {
    int homeScore;
    int awayScore;
    
    public Score(int homeScore, int awayScore) {
        if (homeScore < 0) {
            throw new IllegalArgumentException("El marcador local no puede ser negativo");
        }
        if (awayScore < 0) {
            throw new IllegalArgumentException("El marcador visitante no puede ser negativo");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
    
    /**
     * Crea un nuevo Score con el marcador actualizado
     * Invariante: el marcador nunca decrece
     */
    public Score updateScore(int newHomeScore, int newAwayScore) {
        if (newHomeScore < this.homeScore) {
            throw new IllegalArgumentException("El marcador local no puede decrecer");
        }
        if (newAwayScore < this.awayScore) {
            throw new IllegalArgumentException("El marcador visitante no puede decrecer");
        }
        return new Score(newHomeScore, newAwayScore);
    }
    
    /**
     * Verifica si el partido está empatado
     */
    public boolean isDraw() {
        return homeScore == awayScore;
    }
    
    /**
     * Obtiene el ganador (null si está empatado)
     */
    public String getWinner() {
        if (homeScore > awayScore) {
            return "HOME";
        } else if (awayScore > homeScore) {
            return "AWAY";
        }
        return null;
    }
} 