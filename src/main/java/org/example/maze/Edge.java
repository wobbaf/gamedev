package org.example.maze;

/*********************************************************************************************
 * Encapsulates the x,y coord of where the edge starts, and the direction in which it points.
 *
 * @author psholtz
 *********************************************************************************************/
class Edge {
    private final int _x;
    private final int _y;
    private final int _direction;

    public Edge(int x, int y, int direction) {
        _x = x;
        _y = y;
        _direction = direction;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public int getDirection() {
        return _direction;
    }
}
