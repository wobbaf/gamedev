package org.example;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

class Board extends JPanel implements ActionListener, KeyListener {

    // controls the delay between each tick in ms
    private final int DELAY = 25;
    // controls the size of the board
    public static final int TILE_SIZE = 50;
    public static final int ROWS = 24;
    public static final int VISIBLE_ROWS = 12;
    public static final int COLUMNS = 36;
    public static final int VISIBLE_COLUMNS = 18;
    // controls how many coins appear on the board
    public static final int NUM_COINS = 25;
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    boolean isPlaybackCompleted;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;
    // objects that appear on the game board
    private Player player;

    private Point offset = new Point(0, 0);

    private Util util = new Util();
    private int coinCount = 0;
    public static Tile[][] tiles = new Tile[COLUMNS][ROWS];

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * VISIBLE_COLUMNS, TILE_SIZE * VISIBLE_ROWS));
//        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(0, 0, 0));

        // initialize the game state
        player = new Player();
        populateTiles();

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        player.tick();
        this.tick();

        // give the player points for collecting coins
        collectCoins();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.

        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (isTileInPlayerRadius(player.getPos().x, player.getPos().y, tile.getPos().x, tile.getPos().y, 12)) {
                    tile.setVisible(true);
                } else {
                    tile.setVisible(false);
                }
            }
        }
        drawBackground(g);
        drawScore(g);
        drawCoinCount(g);
        finishGame(g);
        player.draw(g, this, TILE_SIZE, TILE_SIZE, offset);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }

    private void drawBackground(Graphics g) {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                tiles[x][y].draw(g, this, offset, tiles[x][y].getVisible());
            }
        }
    }

    private Boolean isTileInPlayerRadius(int playerX, int playerY, int tileX, int tileY, int radius) {
        return (Math.pow(playerX - tileX, 2) + Math.pow(playerY - tileY, 2) < radius);
    }

    private void drawCoinCount(Graphics g) {
        // set the text to be displayed
        String text = "COINS LEFT: " + coinCount;
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(0, 204, 204));
        g2d.setFont(new Font("Lato", Font.BOLD, 18));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0, TILE_SIZE * (VISIBLE_ROWS - 1), TILE_SIZE * VISIBLE_COLUMNS, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = 50;
        // draw the string
        g2d.drawString(text, x, y);
    }

    private void drawScore(Graphics g) {
        // set the text to be displayed
        String text = "$" + player.getScore();
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(30, 201, 139));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0, TILE_SIZE * (VISIBLE_ROWS - 1), TILE_SIZE * VISIBLE_COLUMNS, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y);
    }

    private void populateTiles() {
        Random rand = new Random();

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                tiles[x][y] = new Tile(x, y);
                int randInt = rand.nextInt(100);
                if (randInt < 20) {
                    tiles[x][y].setHasCoin(true);
                    coinCount++;
                }

                if (randInt > 20 && randInt < 25) {
                    tiles[x][y].setHasObstacle(true);
                }
            }
        }
    }

    private void collectCoins() {
        // if the player is on the same tile as a coin, collect it
        if (tiles[player.getPos().x][player.getPos().y].getHasCoin()) {
            // give the player some points for picking this up
            player.addScore(100);
            tiles[player.getPos().x][player.getPos().y].setHasCoin(false);
            coinCount--;
            util.playSound("sounds/voldemort.wav");
        }
    }

    private void finishGame(Graphics g) {
        if (coinCount == 0) {

            // set the text to be displayed
            String text = "GAME OVER";
            // we need to cast the Graphics to Graphics2D to draw nicer text
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(
                    RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            // set the text color and font
            g2d.setColor(new Color(230, 101, 139));
            g2d.setFont(new Font("Lato", Font.BOLD, 72));
            // draw the score in the bottom center of the screen
            // https://stackoverflow.com/a/27740330/4655368
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            // the text will be contained within this rectangle.
            // here I've sized it to be the entire bottom row of board tiles
            Rectangle rect = new Rectangle(0, 0, TILE_SIZE * VISIBLE_COLUMNS, TILE_SIZE * VISIBLE_ROWS);
            // determine the x coordinate for the text
            int x = TILE_SIZE * COLUMNS / 4;
            // determine the y coordinate for the text
            // (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = TILE_SIZE * ROWS / 2;
            // draw the string
            g2d.drawString(text, x, y);
        }
    }

    private void tick() {

        if (player.getPos().x != COLUMNS && (player.getPos().x >= VISIBLE_COLUMNS - 1 + offset.x)) {
            offset.x++;
        }

        if (player.getPos().x != ROWS && (player.getPos().y >= VISIBLE_ROWS - 1 + offset.y)) {
            offset.y++;
        }

        if (player.getPos().x != 0 && (player.getPos().x <= 0 + offset.x)) {
            offset.x--;
        }

        if (player.getPos().y != 0 && (player.getPos().y <= 0 + offset.y)) {
            offset.y--;
        }
    }
}
