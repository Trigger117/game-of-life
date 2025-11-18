package com.game.gameoflife.service;

import com.game.gameoflife.dto.GameStateDto;
import com.game.gameoflife.game.Game;
import com.game.gameoflife.game.GameLogic;
import com.game.gameoflife.game.GridUtils;
import com.game.gameoflife.model.GameStateEntity;
import com.game.gameoflife.repository.GameStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameStateRepository repository;
    private final GameLogic gameLogic;

    @Transactional
    public GameStateDto getCurrentGame() {
        Game game = loadLatestOrCreate();
        return toDto(game);
    }

    @Transactional
    public GameStateDto newGame() {
        repository.deleteAll();
        Game game = gameLogic.createRandomGame();
        saveGame(game, false);
        return toDto(game);
    }

    @Transactional
    public GameStateDto step() {
        Game current = loadLatestOrCreate();
        Game next = gameLogic.step(current);
        saveGame(next, false); // salva ogni step
        return toDto(next);
    }

    @Transactional
    public void saveSnapshot() {
        Game game = loadLatestOrCreate();
        saveGame(game, true); // salvataggio marcato
    }

    @Transactional
    public Optional<GameStateDto> loadLastSaved() {
        return repository.findTopBySavedOrderByIdDesc(true)
                .map(entity -> {
                    // ricostruisco il Game dal salvataggio
                    Game game = new Game(
                            entity.getTurnNumber(),
                            GridUtils.stringToGrid(entity.getGridString())
                    );
                    saveGame(game, false);
                    return toDto(game);
                });
    }


    @Transactional
    public GameStateDto reset() {
        repository.deleteAll();
      //  Game empty = gameLogic.createEmptyGame();
        // NON salviamo niente, griglia vuota e DB pulito
      //  saveGame(empty, false);
        Game newGame = gameLogic.createRandomGame();
        saveGame(newGame,false);
        return toDto(newGame);
    }

    // --- private helpers ---

    private Game loadLatestOrCreate() {
        return repository.findTopByOrderByIdDesc()
                .map(e -> new Game(
                        e.getTurnNumber(),
                        GridUtils.stringToGrid(e.getGridString())
                ))
                .orElseGet(() -> {
                    Game g = gameLogic.createRandomGame();
                    saveGame(g, false);
                    return g;
                });
    }

    private GameStateEntity saveGame(Game game, boolean saved) {
        GameStateEntity entity = new GameStateEntity();
        entity.setTurnNumber(game.getTurnNumber());
        entity.setGridString(GridUtils.gridToString(game.getGrid()));
        entity.setSaved(saved);
        return repository.save(entity);
    }

    private GameStateDto toDto(Game game) {
        List<String> rows = Arrays.stream(game.getGrid())
                .map(String::new)
                .toList();
        return new GameStateDto(game.getTurnNumber(), rows);
    }
}
