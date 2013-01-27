package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

class MappedPuzzle implements IPuzzle {
    
    IPuzzleMap map;

    public MappedPuzzle(IPuzzleMap map) {
        this.map = map;
    }
    
    @Override
    public List<Coords> getBoxStartPositions() {
        return new ArrayList<Coords>(0);
    }

    @Override
    public Coords getPlayerStartPosition() {
        return new Coords(0,0);
    }

    @Override
    public Bounds getBounds() {
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