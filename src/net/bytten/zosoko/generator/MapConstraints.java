package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2dI;
import net.bytten.gameutil.Vec2I;
import net.bytten.gameutil.Direction;

public class MapConstraints {
    
    protected boolean bounded;
    protected int boxes;
    
    public MapConstraints(boolean bounded, int boxes) {
        this.bounded = bounded;
        this.boxes = boxes;
    }
    
    protected void checkConnectivity(IPuzzleMap map, Set<Vec2I> floorTiles)
            throws RetryException {
        // The map should have one contiguous space of floor 
        Rect2dI bounds = map.getBounds();
        Set<Vec2I> unconnected = new TreeSet<Vec2I>(floorTiles);
        List<Vec2I> queue = new ArrayList<Vec2I>();
        
        if (unconnected.size() == 0) throw new RetryException();
        
        queue.add(unconnected.iterator().next());
        unconnected.remove(queue.get(0));
        
        while (!queue.isEmpty() && !unconnected.isEmpty()) {
            Vec2I current = queue.remove(0);
            
            for (Direction d: Direction.CARDINALS) {
                if (bounds.contains(current.add(d.x, d.y))) {
                    Vec2I neighbor = current.add(d.x,d.y);
                    if (map.getTile(neighbor.x, neighbor.y) == Tile.FLOOR &&
                            unconnected.contains(neighbor)) {
                        unconnected.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
        
        if (!unconnected.isEmpty()) throw new RetryException();
    }
    
    // Does the map contain a 4x3 or 3x4 space of floor tiles?
    protected boolean startsOpenSpace(Vec2I topLeft, Set<Vec2I> floorTiles) {
        for (int dx = 0; dx < 3; ++dx)
        for (int dy = 0; dy < 3; ++dy) {
            Vec2I xy = topLeft.add(dx,dy);
            if (!floorTiles.contains(xy))
                return false;
        }
        
        int dx = 3;
        boolean clear = true;
        for (int dy = 0; dy < 3; ++dy) {
            Vec2I xy = topLeft.add(dx,dy);
            if (!floorTiles.contains(xy))
                clear = false;
        }
        if (clear) return true;
        
        int dy = 3;
        for (dx = 0; dx < 3; ++dx) {
            Vec2I xy = topLeft.add(dx,dy);
            if (!floorTiles.contains(xy))
                return false;
        }
        
        return true;
    }
    
    protected void checkOpenSpaces(Set<Vec2I> floorTiles)
            throws RetryException {
        // Any map with a 3x4 or 4x3 space of open floor is discarded
        for (Vec2I xy: floorTiles) {
            if (startsOpenSpace(xy, floorTiles))
                throw new RetryException();
        }
    }
    
    protected void checkEnoughSpaces(IPuzzleMap map, Set<Vec2I> floorTiles)
            throws RetryException {
        // The map must have enough space for the player, the planned number of
        // boxes and goals and at least one empty space
        if (floorTiles.size() < 2 + boxes*2)
            throw new RetryException();
        
        // This requirement is not in the paper that this library is based on,
        // but seems to improve the results on small maps
        if (floorTiles.size() < map.getWidth()*map.getHeight()/2)
            throw new RetryException();
    }
    
    protected void checkSurroundedFloor(IPuzzleMap map, Set<Vec2I> floorTiles)
            throws RetryException {
        // if the map contains any floor tiles surrounded on three sides by
        // walls, it is discarded
        Rect2dI bounds = map.getBounds();
        for (Vec2I xy: floorTiles) {
            int walls = 0;
            for (Direction d: Direction.CARDINALS) {
                Vec2I neighbor = xy.add(d.x,d.y);
                if (!bounds.contains(neighbor)) {
                    ++walls;
                } else if (map.getTile(neighbor.x, neighbor.y) == Tile.WALL) {
                    ++walls;
                }
            }
            if (walls >= 3)
                throw new RetryException();
        }
    }
    
    protected void checkFloorOnEdge(IPuzzleMap map) throws RetryException {
        // if the player is unbounded, the edge of the map must be reachable
        for (int x = 0; x < map.getWidth(); ++x) {
            if (map.getTile(x, 0) == Tile.FLOOR ||
                    map.getTile(x, map.getHeight()-1) == Tile.FLOOR)
                return;
        }
        
        for (int y = 0; y < map.getHeight(); ++y) {
            if (map.getTile(0, y) == Tile.FLOOR ||
                    map.getTile(map.getWidth()-1, y) == Tile.FLOOR)
                return;
        }
        
        throw new RetryException();
    }
    
    public void check(IPuzzleMap map)
            throws RetryException {
        Set<Vec2I> floorTiles = new TreeSet<Vec2I>(
                PuzzleMap.getFloorTiles(map));
        checkEnoughSpaces(map, floorTiles);
        checkConnectivity(map, floorTiles);
        checkOpenSpaces(floorTiles);
        checkSurroundedFloor(map, floorTiles);
        if (!bounded)
            checkFloorOnEdge(map);
    }
    
}
