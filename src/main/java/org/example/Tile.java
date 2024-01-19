package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import static org.example.Board.*;

public class Tile {

    private Point pos;
    private Boolean hasCoin = false;
    // image that represents the coin's position on the board
    private BufferedImage image;


    private Boolean hasObstacle = false;

    private Boolean hasPlayer = false;

    private Boolean isVisible = false;

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    private Boolean visited = false;

    public Boolean getHasLeftWall() {
        return hasLeftWall;
    }

    public void setHasLeftWall(Boolean hasLeftWall) {
        this.hasLeftWall = hasLeftWall;
    }

    public Boolean getHasRightWall() {
        return hasRightWall;
    }

    public void setHasRightWall(Boolean hasRightWall) {
        this.hasRightWall = hasRightWall;
    }

    public Boolean getHasCeiling() {
        return hasCeiling;
    }

    public void setHasCeiling(Boolean hasCeiling) {
        this.hasCeiling = hasCeiling;
    }

    private Boolean hasLeftWall = false;
    private Boolean hasRightWall = false;
    private Boolean hasCeiling = false;

    public Boolean getHasFloor() {
        return hasFloor;
    }

    public void setHasFloor(Boolean hasFloor) {
        this.hasFloor = hasFloor;
    }

    private Boolean hasFloor = false;

    public Boolean getHasObstacle() {
        return hasObstacle;
    }

    public void setHasObstacle(Boolean hasObstacle) {
        this.hasObstacle = hasObstacle;
    }

    public Tile(int x, int y) {
        pos = new Point(x, y);
    }

    public Boolean getHasCoin() {
        return hasCoin;
    }

    public void setHasCoin(Boolean hasCoin) {
        this.hasCoin = hasCoin;
    }

    public Boolean getHasPlayer() {
        return hasPlayer;
    }

    public void setHasPlayer(Boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Point getPos() {
        return pos;
    }

    private void loadImage(String path) {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer, Point offset, Boolean drawVisible) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        if(drawVisible) {
            g.setColor(new Color(180, 180, 180));
            g.fillRect(
                    (pos.x - offset.x) * TILE_SIZE,
                    (pos.y - offset.y) * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
            );
            g.setColor(new Color(214, 214, 214));
            if(visited){
                g.setColor(new Color(165,207,227));
            }

            if(pos.x == 0 && pos.y == 0){
                g.setColor(new Color(171,202,101));
            }

            if(pos.x == COLUMNS-1 && pos.y == ROWS-1){
                g.setColor(new Color(234,153,153));
            }
            g.fillRect(
                    (pos.x - offset.x) * TILE_SIZE + 2,
                    (pos.y - offset.y) * TILE_SIZE + 2,
                    TILE_SIZE - 2,
                    TILE_SIZE - 2
            );

            if(hasLeftWall){
                g.setColor(Color.black);
                g.fillRect(
                        (pos.x - offset.x) * TILE_SIZE,
                        (pos.y - offset.y) * TILE_SIZE,
                        3,
                        TILE_SIZE
                );
            }


            if(hasFloor){
                g.setColor(Color.black);
                g.fillRect(
                        (pos.x - offset.x) * TILE_SIZE,
                        (pos.y - offset.y) * TILE_SIZE + (TILE_SIZE-3),
                        TILE_SIZE,
                        3
                );
            }


            if(hasRightWall){
                g.setColor(Color.black);
                g.fillRect(
                        (pos.x - offset.x) * TILE_SIZE + (TILE_SIZE-3),
                        (pos.y - offset.y) * TILE_SIZE,
                        3,
                        TILE_SIZE
                );
            }


            if(hasCeiling){
                g.setColor(Color.black);
                g.fillRect(
                        (pos.x - offset.x) * TILE_SIZE,
                        (pos.y - offset.y) * TILE_SIZE,
                        TILE_SIZE,
                        3
                );
            }

            if(hasCoin){
                loadImage("images/coin.png");
                g.drawImage(
                        image,
                        (pos.x - offset.x) * Board.TILE_SIZE + 10,
                        (pos.y - offset.y) * Board.TILE_SIZE + 10,
                        TILE_SIZE - 20,
                        TILE_SIZE - 20,
                        observer
                );
            }

            if(hasObstacle){
                loadImage("images/obstacle.png");
                g.drawImage(
                        image,
                        (pos.x - offset.x) * Board.TILE_SIZE,
                        (pos.y - offset.y) * Board.TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE,
                        observer
                );
            }
        } else {
            if((pos.x + pos.y)%4 ==1) {
                g.setColor(new Color(10, 10, 10));
            } else if((pos.x + pos.y)%4 ==2) {
                g.setColor(new Color(20, 20, 20));
            } else if((pos.x + pos.y)%4 ==3) {
                g.setColor(new Color(30, 30, 30));
            } else {
                g.setColor(new Color(40, 40, 40));
            }
            g.fillRect(
                    (pos.x - offset.x) * TILE_SIZE,
                    (pos.y - offset.y) * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
            );
        }
    }
}
