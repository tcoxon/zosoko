package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2dI;
import net.bytten.gameutil.Coords;
import net.bytten.gameutil.Direction;

public class GoalSupplier {

    protected IPuzzleMap map;
    protected int attempts, boxes;
    protected Integer attemptLimit; // null => no limit
    protected boolean nonFloorAdjacentOnly;
    protected List<List<Coords>> spaces;
    protected int[] indices;
    
    public GoalSupplier(Random rand, int boxes, IPuzzleMap map,
            Integer attemptLimit, boolean nonFloorAdjacentOnly) {
        this.map = map;
        this.boxes = boxes;
        this.attemptLimit = attemptLimit;
        this.nonFloorAdjacentOnly = nonFloorAdjacentOnly;
        
        spaces = new ArrayList<List<Coords>>(boxes);
        List<Coords> floorTiles = getFloorTiles(map);
        for (int i = 0; i < boxes; ++i) {
            List<Coords> boxSpaces = new ArrayList<Coords>(floorTiles);
            Collections.shuffle(boxSpaces, rand);
            spaces.add(boxSpaces);
        }
        
        // indices into the space lists
        indices = new int[boxes];
        
        skipOverlaps();
    }
    
    protected List<Coords> getFloorTiles(IPuzzleMap map) {
        Rect2dI bounds = map.getBounds();
        List<Coords> tiles = PuzzleMap.getFloorTiles(map);
        if (nonFloorAdjacentOnly) {
            List<Coords> newTiles = new ArrayList<Coords>(tiles.size());
            for (Coords xy: tiles) {
                boolean nonfloorNeighbor = false;
                for (Direction d: Direction.COMPASS_DIRECTIONS) {
                    Coords neighbor = xy.add(d.x,d.y);
                    if (!bounds.contains(neighbor) ||
                            map.getTile(neighbor.x, neighbor.y) != Tile.FLOOR) {
                        nonfloorNeighbor = true;
                        break;
                    }
                }
                if (nonfloorNeighbor) {
                    newTiles.add(xy);
                }
            }
            tiles = newTiles;
        }
        return tiles;
    }
    
    public boolean hasMore() {
        if (attemptLimit != null && attempts >= attemptLimit)
            return false;
        
        int i = boxes-1;
        if (indices[i] >= spaces.get(i).size())
            return false;
        
        return true;
    }
    
    protected void incrementIndices() {
        for (int i = 0; i < boxes; ++i) {
            ++indices[i];
            if (i+1 < boxes && indices[i] >= spaces.get(i).size()) {
                indices[i] = 0;
            } else {
                return;
            }
        }
    }
    
    protected List<Coords> current() {
        List<Coords> goals = new ArrayList<Coords>(boxes);
        int i = 0;
        for (List<Coords> boxSpaces: spaces) {
            goals.add(boxSpaces.get(indices[i]));
            ++i;
        }
        return goals;
    }
    
    protected void skipOverlaps() {
        // Skip combinations that have multiple goals at the same coords
        while (hasMore() && new TreeSet<Coords>(current()).size() != boxes)
            incrementIndices();
    }
    
    public List<Coords> next() {
        List<Coords> goals = current();
        incrementIndices();
        skipOverlaps();
        attempts++;
        
        // the goals should not overlap
        assert new TreeSet<Coords>(goals).size() == boxes;
        return goals;
    }

}
