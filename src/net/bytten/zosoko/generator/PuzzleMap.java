package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2dI;
import net.bytten.gameutil.Coords;
import net.bytten.gameutil.Direction;

public class PuzzleMap implements IPuzzleMap {

    protected boolean bounded;
    protected Tile[][] tiles;
    protected int width, height;
    protected Rect2dI bounds;
    
    public PuzzleMap(boolean bounded, int width, int height) {
        this.bounded = bounded;
        this.width = width;
        this.height = height;
        this.bounds = new Rect2dI(0, 0, width, height);
        tiles = new Tile[width][height];
    }
    
    public PuzzleMap(IPuzzleMap other) {
        this(other.isPlayerBounded(), other.getWidth(), other.getHeight());
        
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y)
                tiles[x][y] = other.getTile(x,y);
    }

    @Override
    public Rect2dI getBounds() {
        return new Rect2dI(0, 0, width, height);
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
    
    // Not guaranteed to return any particular floor tile
    public static Coords getAnyFloorTile(IPuzzleMap map) {
        for (int x = 0; x < map.getWidth(); ++x)
            for (int y = 0; y < map.getHeight(); ++y)
                if (map.getTile(x,y) == Tile.FLOOR)
                    return new Coords(x,y);
        return null;
    }

    @Override
    public boolean isPlayerBounded() {
        return bounded;
    }

    public static TreeSet<Coords> getPlayerSpacePartition(IPuzzleMap map,
            Collection<Coords> boxes, Coords containing) {
        Rect2dI bounds = map.getBounds();
        boolean searchedEdges = false;
        List<Coords> queue = new ArrayList<Coords>();
        TreeSet<Coords> visited = new TreeSet<Coords>();
        
        queue.add(containing);
        visited.add(containing);
        
        while (!queue.isEmpty()) {
            Coords current = queue.remove(0);

            if (boxes.contains(current))
                continue;
            
            for (Direction d: Direction.values()) {
                Coords neighbor = current.add(d.x,d.y);
                if (!bounds.contains(neighbor)) {
                    if (!searchedEdges && !map.isPlayerBounded()) {
                        // If the player is allowed outside of the map, all
                        // tiles around the edge are reachable
                        List<Coords> edgeCoords = new ArrayList<Coords>(
                                map.getWidth()*2 + map.getHeight()*2);
                        for (int x = 0; x < map.getWidth(); ++x) {
                            edgeCoords.add(new Coords(x, 0));
                            edgeCoords.add(new Coords(x, map.getHeight()-1));
                        }
                        for (int y = 0; y < map.getHeight(); ++y) {
                            edgeCoords.add(new Coords(0, y));
                            edgeCoords.add(new Coords(map.getWidth()-1, y));
                        }
                        for (Coords c: edgeCoords) {
                            if (map.getTile(c.x, c.y) != Tile.WALL &&
                                    !visited.contains(c)) {
                                queue.add(c);
                                visited.add(c);
                            }
                        }
                    }
                    searchedEdges = true;
                    continue;
                } else {
                    if (map.getTile(neighbor.x, neighbor.y) != Tile.WALL &&
                            !visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
        
        return visited;
    }

    public static Coords getAnyBorderNonWallTile(PuzzleMap map) {
        for (int x = 0; x < map.getWidth(); ++x)
            if (map.getTile(x, 0) != Tile.WALL)
                return new Coords(x, 0);
            else if (map.getTile(x, map.getHeight()-1) != Tile.WALL)
                return new Coords(x, map.getHeight()-1);
        
        for (int y = 0; y < map.getHeight(); ++y)
            if (map.getTile(0, y) != Tile.WALL)
                return new Coords(0, y);
            else if (map.getTile(map.getWidth()-1, y) != Tile.WALL)
                return new Coords(map.getWidth()-1, y);
        
        return null;
    }
    
}
