package com.game.gameoflife.repository;

import com.game.gameoflife.model.GameStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameStateRepository extends JpaRepository<GameStateEntity, Long> {

    // ultimo stato in generale (per fare step)
    Optional<GameStateEntity> findTopByOrderByIdDesc();

    // ultimo salvataggio esplicito (per load)
    Optional<GameStateEntity> findTopBySavedOrderByIdDesc(boolean saved);
}
