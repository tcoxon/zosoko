package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2I;
import net.bytten.gameutil.Vec2I;

class MappedPuzzle implements IPuzzle {
    
    IPuzzleMap map;

    public MappedPuzzle(IPuzzleMap map) {
        this.map = map;
    }
    
    @Override
    public List<Vec2I> getBoxStartPositions() {
        return new ArrayList<Vec2I>(0);
    }

    @Override
    public Vec2I getPlayerStartPosition() {
        return new Vec2I(0,0);
    }

    @Override
    public Rect2I getBounds() {
        return map.getBounds();
    }

    @Override
    public Tile getTile(int x, int y) {
        return map.getTile(x, y);
    }

    @Override
    public boolean isPlayerBounded() {
        return map.isPlayerBounded();
    }

    public void setMap(IPuzzleMap map) {
        this.map = map;
    }

    @Override
    public int getWidth() {
        return map.getWidth();
    }

    @Override
    public int getHeight() {
        return map.getHeight();
    }
    
}