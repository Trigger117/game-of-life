package com.game.gameoflife.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.game.gameoflife.game.GridUtils.COLS;
import static com.game.gameoflife.game.GridUtils.ROWS;

@Component
public class GameLogic {

    private final Random random = new Random();

    public Game createRandomGame() {
        char[][] grid = new char[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = randomInitialChar();
            }
        }

        return new Game(0, grid);
    }

    public Game createEmptyGame() {
        char[][] grid = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = ' ';
            }
        }
        return new Game(0, grid);
    }

    private char randomInitialChar() {
        int x = random.nextInt(10); // 0..9

        if (x < 2) return '@';      // 20%
        if (x < 4) return '%';      // 20%
        if (x < 6) return '#';      // 20%
        if (x < 8) return 'O';      // 20%
        return ' ';                 // 20% vuoto
    }

    /**
     * Applica un singolo step secondo le regole del gioco.
     */
    public Game step(Game game) {
        char[][] original = copyGrid(game.getGrid());
        char[][] next = new char[ROWS][COLS];

        // inizializza next copiando solo elementi statici # e O
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                char ch = original[r][c];
                if (ch == '#' || ch == 'O') {
                    next[r][c] = ch;
                } else {
                    next[r][c] = ' ';
                }
            }
        }

        // raccogli posizioni di @ e %
        List<int[]> movers = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (original[r][c] == '@' || original[r][c] == '%') {
                    movers.add(new int[]{r, c});
                }
            }
        }

        // ordine casuale
        Collections.shuffle(movers, random);

        // direzioni possibili
        int[][] dirs = new int[][]{
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        for (int[] pos : movers) {
            int r = pos[0];
            int c = pos[1];
            char entity = original[r][c];

            // sceglie una direzione casuale
            int[] dir = dirs[random.nextInt(dirs.length)];
            int nr = r + dir[0];
            int nc = c + dir[1];

            boolean inBounds = nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS;
            char target = inBounds ? original[nr][nc] : 'X';

            if (entity == '@') {
                handleAtMove(original, next, r, c, nr, nc, inBounds, target);
            } else if (entity == '%') {
                handlePercentMove(original, next, r, c, nr, nc, inBounds, target);
            }
        }

        int newTurn = game.getTurnNumber() + 1;

        // ogni 20 turni aggiungi un nuovo pezzo @, %, #
        if (newTurn % 20 == 0) {
            addRandomPiece(next);
        }

        return new Game(newTurn, next);
    }

    private void handleAtMove(char[][] original, char[][] next,
                              int r, int c, int nr, int nc,
                              boolean inBounds, char target) {

        // fuori griglia o in cella vietata: resta fermo
        if (!inBounds || target == 'O' || target == '@' || target == '%') {
            placeIfEmpty(next, r, c, '@');
            return;
        }

        char destNext = next[nr][nc];

        // NUOVA LOGICA (collisione: entrambi vivi)
        if (destNext == ' ' || destNext == '#') {
            // mi sposto normalmente
            placeOverride(next, nr, nc, '@');
        } else if (destNext == '@') {
            // collisione tra due @ → resto nella cella originale
            placeIfEmpty(next, r, c, '@');
        } else if (destNext == '%') {
            return;
        }

    /*   VECCHIA LOGICA (COMPORTAMENTO ORIGINALE)
    if (!inBounds || target == 'O' || target == '@' || target == '%') {
        placeIfEmpty(next, r, c, '@');
        return;
    }
    if (target == '#') {
        placeOverride(next, nr, nc, '@'); // mangia #
        return;
    }
    // destinazione era vuota → mi sposto
    if (next[nr][nc] == ' ') {
        placeOverride(next, nr, nc, '@');
    } else {
        // qualcuno è già lì → sparisco
    }
    */
    }

    private void handlePercentMove(char[][] original, char[][] next,
                                   int r, int c, int nr, int nc,
                                   boolean inBounds, char target) {

        // fuori griglia o cella vietata
        if (!inBounds || target == 'O' || target == '#') {
            placeIfEmpty(next, r, c, '%');
            return;
        }

        char destNext = next[nr][nc];

        // NUOVA LOGICA (collisione: entrambi vivi)
        if (destNext == ' ' || destNext == '@') {
            // mi sposto (se era @ lo mangio)
            placeOverride(next, nr, nc, '%');
        } else if (destNext == '%') {
            // collisione → resto nella cella originale
            placeIfEmpty(next, r, c, '%');
        }

    /*   VECCHIA LOGICA (COMPORTAMENTO ORIGINALE)
    if (!inBounds || target == 'O' || target == '#') {
        placeIfEmpty(next, r, c, '%');
        return;
    }
    if (target == '@') {
        placeOverride(next, nr, nc, '%'); // mangia @
        return;
    }
    if (next[nr][nc] == ' ') {
        placeOverride(next, nr, nc, '%');
    } else {
        // qualcuno è già lì → sparisco
    }
    */
    }

    private void addRandomPiece(char[][] grid) {
        List<int[]> empties = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == ' ') {
                    empties.add(new int[]{r, c});
                }
            }
        }
        if (empties.isEmpty()) return;

        int[] spot = empties.get(random.nextInt(empties.size()));
        char[] options = new char[]{'@', '%', '#'};
        char piece = options[random.nextInt(options.length)];
        grid[spot[0]][spot[1]] = piece;
    }

    private void placeIfEmpty(char[][] grid, int r, int c, char ch) {
        if (grid[r][c] == ' ') {
            grid[r][c] = ch;
        }
    }

    private void placeOverride(char[][] grid, int r, int c, char ch) {
        grid[r][c] = ch;
    }

    private char[][] copyGrid(char[][] src) {
        char[][] dst = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            System.arraycopy(src[r], 0, dst[r], 0, COLS);
        }
        return dst;
    }
}
