package net.bytten.zosoko.player;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2dI;
import net.bytten.gameutil.Coords;

public class PlayingPuzzle implements IPuzzle {

    IPuzzle puzzle;
    Coords playerPosition;
    List<Coords> boxPositions;
    
    public PlayingPuzzle(IPuzzle puzzle) {
        this.puzzle = puzzle;
        reset();
    }
    
    public void reset() {
        playerPosition = getPlayerStartPosition();
        boxPositions = new ArrayList<Coords>(getBoxStartPositions());
    }

    @Override
    public List<Coords> getBoxStartPositions() {
        return puzzle.getBoxStartPositions();
    }

    @Override
    public Coords getPlayerStartPosition() {
        return puzzle.getPlayerStartPosition();
    }

    @Override
    public Rect2dI getBounds() {
        return puzzle.getBounds();
    }

    @Override
    public Tile getTile(int x, int y) {
        return puzzle.getTile(x, y);
    }

    public Coords getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Coords playerPosition) {
        this.playerPosition = playerPosition;
    }

    public List<Coords> getBoxPositions() {
        return boxPositions;
    }

    @Override
    public boolean isPlayerBounded() {
        return puzzle.isPlayerBounded();
    }

    @Override
    public int getWidth() {
        return puzzle.getWidth();
    }

    @Override
    public int getHeight() {
        return puzzle.getHeight();
    }
    
    public boolean isWon() {
        // The puzzle is won when all boxes are on goal tiles
        for (Coords box: boxPositions) {
            if (puzzle.getTile(box.x, box.y) != Tile.GOAL)
                return false;
        }
        return boxPositions.size() > 0;
    }

}
