package org.example.maze;

import java.util.Random;

public class Maze {
    // Define class variables
    public static final int N = 1;
    public static final int S = 2;
    public static final int E = 4;
    public static final int W = 8;
    protected Random _random = null;
    protected Long _seed = null;
    protected int _w = 0;
    protected int _h = 0;

    public int[][] getGrid() {
        return _grid;
    }

    public void setGrid(int[][] _grid) {
        this._grid = _grid;
    }

    protected int[][] _grid = null;

    // Define class methods
    public static int DX(int direction) {
        return switch (direction) {
            case Maze.E -> +1;
            case Maze.W -> -1;
            case Maze.N, Maze.S -> 0;
            default -> -1;
        };
    }

    public static int DY(int direction) {
        return switch (direction) {
            case Maze.E, Maze.W -> 0;
            case Maze.N -> -1;
            case Maze.S -> 1;
            default -> -1;
        };
    }

    public static int OPPOSITE(int direction) {
        return switch (direction) {
            case Maze.E -> Maze.W;
            case Maze.W -> Maze.E;
            case Maze.N -> Maze.S;
            case Maze.S -> Maze.N;
            default -> -1;
        };
    }

    public Maze(int w, int h, long seed) {
        initialize(w, h);
        _random = new Random(seed);
        _seed = seed;
    }

    private void initialize(int w, int h) {
        _w = w;
        _h = h;
        _grid = new int[h][w];
        for (int j = 0; j < h; ++j) {
            for (int i = 0; i < w; ++i) {
                _grid[j][i] = 0;
            }
        }
    }
}
