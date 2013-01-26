package net.bytten.zosoko.player;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

public class PuzzleRenderer {
    
    private Graphics2D g;
    private double scale;
    private IPuzzle puzzle;
    private Bounds bounds;
    
    private void drawFloor() {
    }
    
    private void drawWall() {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, (int)scale, (int)scale);
    }
    
    private void drawGoal() {
        g.setColor(Color.BLACK);
        g.drawString("GOAL", (int)scale/2, (int)scale/2);
    }
    
    private void drawTile(Tile tile, int x, int y) {
        AffineTransform origXfm = g.getTransform();
        g.translate((int)(x * scale), (int)(y * scale));
        switch (tile) {
        case FLOOR:
            drawFloor();
            break;
        case WALL:
            drawWall();
            break;
        case GOAL:
            drawGoal();
            break;
        }
        g.setTransform(origXfm);
    }
    
    private void drawMap() {
        for (int x = 0; x < bounds.width(); ++x)
        for (int y = 0; y < bounds.height(); ++y) {
            drawTile(puzzle.getTile(x,y), x,y);
        }
        
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x <= bounds.width(); ++x) {
            g.drawLine((int)(x * scale), 0, (int)(x * scale),
                    (int)(bounds.height() * scale));
        }
        for (int y = 0; y <= bounds.height(); ++y) {
            g.drawLine(0, (int)(y * scale), (int)(bounds.width() * scale),
                    (int)(y * scale));
        }
    }
    
    private void drawBox(Coords xy) {
        AffineTransform origXfm = g.getTransform();
        g.translate((int)(xy.x * scale), (int)(xy.y * scale));
        
        g.setColor(Color.BLUE);
        g.fillRect((int)(scale/4), (int)(scale/4), (int)(scale/2),
                (int)(scale/2));
        
        g.setTransform(origXfm);
    }
    
    private void drawPlayer(Coords xy) {
        AffineTransform origXfm = g.getTransform();
        g.translate((int)(xy.x * scale), (int)(xy.y * scale));
        
        g.setColor(Color.RED);
        g.fillOval((int)(scale/4), (int)(scale/4), (int)(scale/2),
                (int)(scale/2));
        
        g.setTransform(origXfm);
    }

    public void draw(Graphics2D g, Dimension dim, IPuzzle puzzle) {
        this.g = g;
        this.puzzle = puzzle;
        
        AffineTransform origXfm = g.getTransform();
        
        // Figure out scale & translation to draw the dungeon at
        synchronized (puzzle) {
            bounds = puzzle.getBounds();
            scale = Math.min(((double)dim.width) / (bounds.width() + 2),
                    ((double)dim.height) / (bounds.height() + 2));
            // move the graph into view
            g.translate(scale * (1 - bounds.left), scale * (1 - bounds.top));
            
            drawMap();
            
            if (puzzle instanceof PlayingPuzzle) {
                PlayingPuzzle ppuzzle = (PlayingPuzzle)puzzle;
                for (Coords xy: ppuzzle.getBoxPositions()) {
                    drawBox(xy);
                }
                drawPlayer(ppuzzle.getPlayerPosition());
            } else {
                for (Coords xy: puzzle.getBoxStartPositions()) {
                    drawBox(xy);
                }
                drawPlayer(puzzle.getPlayerStartPosition());
            }
        }
        
        g.setTransform(origXfm);
    }

}
