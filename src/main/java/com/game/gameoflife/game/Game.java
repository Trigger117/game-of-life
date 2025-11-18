package com.game.gameoflife.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Game {

    private final int turnNumber;
    private final char[][] grid; // [10][10]
}
