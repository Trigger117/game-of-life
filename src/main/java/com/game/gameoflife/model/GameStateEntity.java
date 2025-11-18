package com.game.gameoflife.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_state")
@Getter
@Setter
@NoArgsConstructor
public class GameStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turn_number", nullable = false)
    private int turnNumber;

    // Stringa da 100 caratteri, row-major (riga per riga)
    @Column(name = "grid_string", nullable = false, length = 100)
    private String gridString;

    // true = salvataggio esplicito utente, false = stato "di passo"
    @Column(name = "saved", nullable = false)
    private boolean saved;
}
