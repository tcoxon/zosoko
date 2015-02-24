package net.bytten.zosoko.player;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2I;
import net.bytten.gameutil.Vec2I;

public class PlayingPuzzle implements IPuzzle {

    IPuzzle puzzle;
    Vec2I playerPosition;
    List<Vec2I> boxPositions;
    
    public PlayingPuzzle(IPuzzle puzzle) {
        this.puzzle = puzzle;
        reset();
    }
    
    public void reset() {
        playerPosition = getPlayerStartPosition();
        boxPositions = new ArrayList<Vec2I>(getBoxStartPositions());
    }

    @Override
    public List<Vec2I> getBoxStartPositions() {
        return puzzle.getBoxStartPositions();
    }

    @Override
    public Vec2I getPlayerStartPosition() {
        return puzzle.getPlayerStartPosition();
    }

    @Override
    public Rect2I getBounds() {
        return puzzle.getBounds();
    }

    @Override
    public Tile getTile(int x, int y) {
        return puzzle.getTile(x, y);
    }

    public Vec2I getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Vec2I playerPosition) {
        this.playerPosition = playerPosition;
    }

    public List<Vec2I> getBoxPositions() {
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
        for (Vec2I box: boxPositions) {
            if (puzzle.getTile(box.x, box.y) != Tile.GOAL)
                return false;
        }
        return boxPositions.size() > 0;
    }

}
