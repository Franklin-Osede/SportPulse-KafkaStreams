package com.sportpulse.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the Match aggregate following TDD
 * 
 * Domain invariants to test:
 * 1. Clock never goes backwards
 * 2. Score never decreases
 * 3. Valid states: NOT_STARTED → LIVE → FINISHED
 */
@DisplayName("Match Aggregate")
class MatchTest {

    @Test
    @DisplayName("should create match with correct initial state")
    void shouldCreateMatchWithInitialState() {
        // Given - Prepare test data
        Team homeTeam = new Team("Barcelona", "BAR");
        Team awayTeam = new Team("Real Madrid", "RMA");
        
        // When - Execute the action
        Match match = new Match(homeTeam, awayTeam);
        
        // Then - Verify the result
        assertThat(match.getHomeTeam()).isEqualTo(homeTeam);
        assertThat(match.getAwayTeam()).isEqualTo(awayTeam);
        assertThat(match.getStatus()).isEqualTo(MatchStatus.NOT_STARTED);
        assertThat(match.getCurrentMinute()).isEqualTo(0);
        assertThat(match.getScore().getHomeScore()).isEqualTo(0);
        assertThat(match.getScore().getAwayScore()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("should have unique ID when created")
    void shouldHaveUniqueIdWhenCreated() {
        // Given
        Team homeTeam = new Team("Barcelona", "BAR");
        Team awayTeam = new Team("Real Madrid", "RMA");
        
        // When
        Match match1 = new Match(homeTeam, awayTeam);
        Match match2 = new Match(homeTeam, awayTeam);
        
        // Then
        assertThat(match1.getId()).isNotNull();
        assertThat(match2.getId()).isNotNull();
        assertThat(match1.getId()).isNotEqualTo(match2.getId());
    }
    
    @Test
    @DisplayName("should start match correctly")
    void shouldStartMatchCorrectly() {
        // Given
        Team homeTeam = new Team("Barcelona", "BAR");
        Team awayTeam = new Team("Real Madrid", "RMA");
        Match match = new Match(homeTeam, awayTeam);
        
        // When
        match.startMatch();
        
        // Then
        assertThat(match.getStatus()).isEqualTo(MatchStatus.LIVE);
    }
    
    @Test
    @DisplayName("should not allow score to decrease")
    void shouldNotAllowScoreToDecrease() {
        // Given
        Team homeTeam = new Team("Barcelona", "BAR");
        Team awayTeam = new Team("Real Madrid", "RMA");
        Match match = new Match(homeTeam, awayTeam);
        match.startMatch();
        
        // When & Then
        match.updateScore(1, 0); // First goal
        assertThat(match.getScore().getHomeScore()).isEqualTo(1);
        
        // This should throw an exception
        assertThatThrownBy(() -> {
            match.updateScore(0, 0); // Try to decrease score
        }).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("no puede decrecer");
    }
} 