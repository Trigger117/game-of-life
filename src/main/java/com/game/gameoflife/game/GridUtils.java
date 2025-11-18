package com.game.gameoflife.game;

public class GridUtils {

    public static final int ROWS = 10;
    public static final int COLS = 10;

    public static char[][] stringToGrid(String s) {
        char[][] grid = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                grid[i][j] = s.charAt(index);
            }
        }
        return grid;
    }

    public static String gridToString(char[][] grid) {
        StringBuilder sb = new StringBuilder(ROWS * COLS);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }
}
