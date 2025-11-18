package com.game.gameoflife.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameStateDto {

    private int turnNumber;
    // 10 stringhe, ognuna lunga 10 caratteri
    private List<String> grid;
}
