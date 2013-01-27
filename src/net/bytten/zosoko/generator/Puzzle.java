package net.bytten.zosoko.generator;

import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

public class Puzzle implements IPuzzle {
    
    protected IPuzzleMap map;
    protected List<Coords> boxes;
    protected Coords player;

    public Puzzle(IPuzzleMap puzzleMap, List<Coords> boxes, Coords player) {
        this.map = puzzleMap;
        this.boxes = boxes;
        this.player = player;
    }

    @Override
    public Bounds getBounds() {
        return map.getBounds();
    }

    @Override
    public int getWidth() {
        return map.getWidth();
    }

    @Override
    public int getHeight() {
        return map.getHeight();
    }

    @Override
    public Tile getTile(int x, int y) {
        return map.getTile(x,y);
    }

    @Override
    public List<Coords> getBoxStartPositions() {
        return boxes;
    }

    @Override
    public Coords getPlayerStartPosition() {
        return player;
    }

    @Override
    public boolean isPlayerBounded() {
        return map.isPlayerBounded();
    }

}
