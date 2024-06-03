package minesweeper;
import java.util.Random;

public class MinesweeperGame {
    private final int rows;
    private final int cols;
    private final int[][] grid;
    private final boolean[][] mines;
    private final boolean[][] revealed; // Adăugat pentru a ține evidența celulelor dezvăluite

    public MinesweeperGame(int rows, int cols, int minesCount) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
        this.mines = new boolean[rows][cols];
        this.revealed = new boolean[rows][cols]; // Inițializat array-ul de dezvăluire
        placeMines(minesCount);
        calculateNumbers();
    }

    private void placeMines(int minesCount) {
        Random rand = new Random();
        for (int i = 0; i < minesCount; i++) {
            int r, c;
            do {
                r = rand.nextInt(rows);
                c = rand.nextInt(cols);
            } while (mines[r][c]);
            mines[r][c] = true;
        }
    }

    private void calculateNumbers() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!mines[r][c]) {
                    int count = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int rr = r + dr;
                            int cc = c + dc;
                            if (rr >= 0 && rr < rows && cc >= 0 && cc < cols && mines[rr][cc]) {
                                count++;
                            }
                        }
                    }
                    grid[r][c] = count;
                }
            }
        }
    }

    public int getValue(int row, int col) {
        return grid[row][col];
    }

    public boolean hasMine(int row, int col) {
        return mines[row][col];
    }

    public boolean revealCell(int row, int col) {
        if (!revealed[row][col]) {
            revealed[row][col] = true;
            return !mines[row][col];
        }
        return true; // Dacă celula a fost deja dezvăluită, returnează true
    }

    public boolean isWon() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!mines[r][c] && !revealed[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }
}
