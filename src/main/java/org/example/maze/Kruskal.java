package org.example.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Kruskal extends Maze {

    private List<List<Tree>> _sets;
    private Stack<Edge> _edges;

    public Kruskal(int w, int h, long seed) {
        super(w, h, seed);
        initialize();
    }

    private void initialize() {
        _sets = new ArrayList<>();
        for (int y = 0; y < _h; ++y) {
            List<Tree> tmp = new ArrayList<>();
            for (int x = 0; x < _w; ++x) {
                tmp.add(new Tree());
            }
            _sets.add(tmp);
        }

        _edges = new Stack<>();
        for (int y = 0; y < _h; ++y) {
            for (int x = 0; x < _w; ++x) {
                if (y > 0) {
                    _edges.add(new Edge(x, y, Maze.N));
                }
                if (x > 0) {
                    _edges.add(new Edge(x, y, Maze.W));
                }
            }
        }
        shuffle(_edges);
        carvePassages();
    }

    private void carvePassages() {
        while (!_edges.isEmpty()) {
            Edge tmp = _edges.pop();
            int x = tmp.getX();
            int y = tmp.getY();
            int direction = tmp.getDirection();
            int dx = x + Maze.DX(direction), dy = y + Maze.DY(direction);

            Tree set1 = (_sets.get(y)).get(x);
            Tree set2 = (_sets.get(dy)).get(dx);

            if (!set1.connected(set2)) {
                set1.connect(set2);
                _grid[y][x] |= direction;
                _grid[dy][dx] |= Maze.OPPOSITE(direction);
            }
        }
    }

    private void shuffle(List<Edge> args) {
        for (int i = 0; i < args.size(); ++i) {
            int pos = _random.nextInt(args.size());
            Edge tmp1 = args.get(i);
            Edge tmp2 = args.get(pos);
            args.set(i, tmp2);
            args.set(pos, tmp1);
        }
    }
}

