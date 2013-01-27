package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

public class PuzzleMap implements IPuzzleMap {

    protected boolean bounded;
    protected Tile[][] tiles;
    protected int width, height;
    
    public PuzzleMap(boolean bounded, int width, int height) {
        this.bounded = bounded;
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }
    
    public PuzzleMap(IPuzzleMap other) {
        this(other.isPlayerBounded(), other.getWidth(), other.getHeight());
        
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y)
                tiles[x][y] = other.getTile(x,y);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(width, height);
    }

    @Override
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
    
    public void setTile(int x, int y, Tile t) {
        tiles[x][y] = t;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public static List<Coords> getFloorTiles(IPuzzleMap map) {
        List<Coords> floors = new ArrayList<Coords>();
        
        for (int x = 0; x < map.getWidth(); ++x)
            for (int y = 0; y < map.getHeight(); ++y)
                if (map.getTile(x,y) == Tile.FLOOR)
                    floors.add(new Coords(x,y));
        
        return floors;
    }

    @Override
    public boolean isPlayerBounded() {
        return bounded;
    }
    
}
