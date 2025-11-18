package com.game.gameoflife.controller;

import com.game.gameoflife.dto.GameStateDto;
import com.game.gameoflife.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@CrossOrigin // se ti serve da altre origin
public class GameController {

    private final GameService gameService;

    @GetMapping
    public GameStateDto getCurrent() {
        return gameService.getCurrentGame();
    }

    @PostMapping("/new")
    public GameStateDto newGame() {
        return gameService.newGame();
    }

    @PostMapping("/step")
    public GameStateDto step() {
        return gameService.step();
    }

    @PostMapping("/save")
    public void save() {
        gameService.saveSnapshot();
    }

    @GetMapping("/load")
    public ResponseEntity<GameStateDto> load() {
        return gameService.loadLastSaved()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/reset")
    public GameStateDto reset() {
        return gameService.reset();
    }
}
