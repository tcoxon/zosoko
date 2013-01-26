package net.bytten.zosoko.generator;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;

public class PuzzleMap implements IPuzzleMap {

    protected Tile[][] tiles;
    protected int width, height;
    
    public PuzzleMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }
    
    public PuzzleMap(IPuzzleMap other) {
        this(other.getWidth(), other.getHeight());
        
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

}
