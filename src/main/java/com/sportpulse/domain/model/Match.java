package com.sportpulse.domain.model;

import lombok.Getter;
import java.util.UUID;

/**
 * Agregado principal que representa un partido deportivo
 * 
 * Invariantes de dominio:
 * 1. El reloj nunca retrocede: currentMinute solo puede crecer
 * 2. El marcador no decrece: homeScore y awayScore solo aumentan
 * 3. Estados válidos: NOT_STARTED → LIVE → FINISHED
 */
@Getter
public class Match {
    private final UUID id;
    private final Team homeTeam;
    private final Team awayTeam;
    private MatchStatus status;
    private int currentMinute;
    private Score score;
    
    public Match(Team homeTeam, Team awayTeam) {
        if (homeTeam == null) {
            throw new IllegalArgumentException("El equipo local no puede ser null");
        }
        if (awayTeam == null) {
            throw new IllegalArgumentException("El equipo visitante no puede ser null");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Los equipos no pueden ser iguales");
        }
        
        this.id = UUID.randomUUID();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.status = MatchStatus.NOT_STARTED;
        this.currentMinute = 0;
        this.score = new Score(0, 0);
    }
    
    /**
     * Inicia el partido
     * Invariante: solo se puede iniciar desde NOT_STARTED
     */
    public void startMatch() {
        if (!status.canTransitionTo(MatchStatus.LIVE)) {
            throw new IllegalStateException("No se puede iniciar un partido que no está en estado NOT_STARTED");
        }
        this.status = MatchStatus.LIVE;
    }
    
    /**
     * Finaliza el partido
     * Invariante: solo se puede finalizar desde LIVE
     */
    public void finishMatch() {
        if (!status.canTransitionTo(MatchStatus.FINISHED)) {
            throw new IllegalStateException("No se puede finalizar un partido que no está en estado LIVE");
        }
        this.status = MatchStatus.FINISHED;
    }
    
    /**
     * Actualiza el minuto actual
     * Invariante: el reloj nunca retrocede
     */
    public void updateMinute(int newMinute) {
        if (newMinute < this.currentMinute) {
            throw new IllegalArgumentException("El minuto no puede retroceder");
        }
        if (newMinute < 0) {
            throw new IllegalArgumentException("El minuto no puede ser negativo");
        }
        this.currentMinute = newMinute;
    }
    
    /**
     * Actualiza el marcador
     * Invariante: el marcador nunca decrece
     */
    public void updateScore(int newHomeScore, int newAwayScore) {
        this.score = this.score.updateScore(newHomeScore, newAwayScore);
    }
    
    /**
     * Aplica una actualización completa del partido
     * Método principal para mantener invariantes
     */
    public void applyFeedUpdate(int minute, int homeScore, int awayScore, MatchStatus newStatus) {
        // Validar transición de estado
        if (newStatus != null && !status.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException("Transición de estado inválida: " + status + " → " + newStatus);
        }
        
        // Actualizar minuto (invariante: nunca retrocede)
        updateMinute(minute);
        
        // Actualizar marcador (invariante: nunca decrece)
        updateScore(homeScore, awayScore);
        
        // Actualizar estado si es válido
        if (newStatus != null) {
            this.status = newStatus;
        }
    }
    
    /**
     * Verifica si el partido está en vivo
     */
    public boolean isLive() {
        return status == MatchStatus.LIVE;
    }
    
    /**
     * Verifica si el partido ha terminado
     */
    public boolean isFinished() {
        return status == MatchStatus.FINISHED;
    }
    
    /**
     * Obtiene el ganador del partido (solo si está finalizado)
     */
    public String getWinner() {
        if (!isFinished()) {
            return null;
        }
        return score.getWinner();
    }
} 