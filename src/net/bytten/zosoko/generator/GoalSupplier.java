package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2dI;
import net.bytten.gameutil.Vec2I;
import net.bytten.gameutil.Direction;

public class GoalSupplier {

    protected IPuzzleMap map;
    protected int attempts, boxes;
    protected Integer attemptLimit; // null => no limit
    protected boolean nonFloorAdjacentOnly;
    protected List<List<Vec2I>> spaces;
    protected int[] indices;
    
    public GoalSupplier(Random rand, int boxes, IPuzzleMap map,
            Integer attemptLimit, boolean nonFloorAdjacentOnly) {
        this.map = map;
        this.boxes = boxes;
        this.attemptLimit = attemptLimit;
        this.nonFloorAdjacentOnly = nonFloorAdjacentOnly;
        
        spaces = new ArrayList<List<Vec2I>>(boxes);
        List<Vec2I> floorTiles = getFloorTiles(map);
        for (int i = 0; i < boxes; ++i) {
            List<Vec2I> boxSpaces = new ArrayList<Vec2I>(floorTiles);
            Collections.shuffle(boxSpaces, rand);
            spaces.add(boxSpaces);
        }
        
        // indices into the space lists
        indices = new int[boxes];
        
        skipOverlaps();
    }
    
    protected List<Vec2I> getFloorTiles(IPuzzleMap map) {
        Rect2dI bounds = map.getBounds();
        List<Vec2I> tiles = PuzzleMap.getFloorTiles(map);
        if (nonFloorAdjacentOnly) {
            List<Vec2I> newTiles = new ArrayList<Vec2I>(tiles.size());
            for (Vec2I xy: tiles) {
                boolean nonfloorNeighbor = false;
                for (Direction d: Direction.CARDINALS) {
                    Vec2I neighbor = xy.add(d.x,d.y);
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
    
    protected List<Vec2I> current() {
        List<Vec2I> goals = new ArrayList<Vec2I>(boxes);
        int i = 0;
        for (List<Vec2I> boxSpaces: spaces) {
            goals.add(boxSpaces.get(indices[i]));
            ++i;
        }
        return goals;
    }
    
    protected void skipOverlaps() {
        // Skip combinations that have multiple goals at the same coords
        while (hasMore() && new TreeSet<Vec2I>(current()).size() != boxes)
            incrementIndices();
    }
    
    public List<Vec2I> next() {
        List<Vec2I> goals = current();
        incrementIndices();
        skipOverlaps();
        attempts++;
        
        // the goals should not overlap
        assert new TreeSet<Vec2I>(goals).size() == boxes;
        return goals;
    }

}
