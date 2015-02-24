package net.bytten.zosoko.generator;

import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2I;
import net.bytten.gameutil.Vec2I;

public class Puzzle implements IPuzzle {
    
    protected IPuzzleMap map;
    protected List<Vec2I> boxes;
    protected Vec2I player;

    public Puzzle(IPuzzleMap puzzleMap, List<Vec2I> boxes, Vec2I player) {
        this.map = puzzleMap;
        this.boxes = boxes;
        this.player = player;
    }

    @Override
    public Rect2I getBounds() {
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
    public List<Vec2I> getBoxStartPositions() {
        return boxes;
    }

    @Override
    public Vec2I getPlayerStartPosition() {
        return player;
    }

    @Override
    public boolean isPlayerBounded() {
        return map.isPlayerBounded();
    }

}
