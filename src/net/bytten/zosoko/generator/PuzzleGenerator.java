package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;
import net.bytten.zosoko.util.Direction;

public class PuzzleGenerator implements IPuzzleGenerator {
    
    protected class MappedPuzzle implements IPuzzle {
        
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
            return bounded;
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

    int width, height, boxes;
    boolean bounded;
    
    TemplateMap templateMap;
    PuzzleMap puzzleMap;
    IPuzzle puzzle;
    Random rand;
    
    public PuzzleGenerator(Random rand, int width, int height, int boxes,
            boolean bounded) {
        this.rand = rand;
        this.width = width;
        this.height = height;
        this.boxes = boxes;
        this.bounded = bounded;
    }
    
    protected boolean templateFits(Template template, TemplateTransform transform,
            int x, int y) {
        if (!template.check(templateMap, transform, new Coords(x,y)))
            return false;
        // check the neighbors already placed fit with this template...
        if (x > 0) {
            Template tpl = templateMap.getTemplate(x-1, y);
            TemplateTransform xfm = templateMap.getTransform(x-1, y);
            if (!tpl.check(templateMap, xfm, new Coords(x-1, y)))
                return false;
        }
        if (y > 0) {
            Template tpl = templateMap.getTemplate(x, y-1);
            TemplateTransform xfm = templateMap.getTransform(x, y-1);
            if (!tpl.check(templateMap, xfm, new Coords(x, y-1)))
                return false;
        }
        if (x > 0 && y > 0) {
            Template tpl = templateMap.getTemplate(x-1, y-1);
            TemplateTransform xfm = templateMap.getTransform(x-1, y-1);
            if (!tpl.check(templateMap, xfm, new Coords(x-1, y-1)))
                return false;
        }
        
        return true;
    }
    
    protected static class RetryException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    protected void fillTemplateMap() throws RetryException {
        for (int x = 0; x < (width+2)/3; ++x)
        for (int y = 0; y < (height+2)/3; ++y) {
            int attempts = 0;
            while (++attempts < 20) {
                Template template = Template.values()[
                        rand.nextInt(Template.values().length)];
                TemplateTransform transform = new TemplateTransform(rand);
                templateMap.put(x, y, template, transform);
                if (templateFits(template, transform, x, y)) {
                    break;
                }
            }
            if (attempts >= 20) throw new RetryException();
        }
    }
    
    protected Set<Coords> getFloorTiles(IPuzzleMap map) {
        Set<Coords> floors = new TreeSet<Coords>();
        
        for (int x = 0; x < map.getWidth(); ++x)
            for (int y = 0; y < map.getHeight(); ++y)
                if (map.getTile(x,y) == Tile.FLOOR)
                    floors.add(new Coords(x,y));
        
        return floors;
    }
    
    protected void checkConnectivity(IPuzzleMap map, Set<Coords> floorTiles)
            throws RetryException {
        // The map should have one contiguous space of floor 
        Bounds bounds = map.getBounds();
        Set<Coords> unconnected = new TreeSet<Coords>(floorTiles);
        List<Coords> queue = new ArrayList<Coords>();
        
        if (unconnected.size() == 0) throw new RetryException();
        
        queue.add(unconnected.iterator().next());
        unconnected.remove(queue.get(0));
        
        while (!queue.isEmpty() && !unconnected.isEmpty()) {
            Coords current = queue.remove(0);
            
            for (Direction d: Direction.values()) {
                if (bounds.contains(current.add(d.x, d.y))) {
                    Coords neighbor = current.add(d.x,d.y);
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
    protected boolean startsOpenSpace(Coords topLeft, Set<Coords> floorTiles) {
        for (int dx = 0; dx < 3; ++dx)
        for (int dy = 0; dy < 3; ++dy) {
            Coords xy = topLeft.add(dx,dy);
            if (!floorTiles.contains(xy))
                return false;
        }
        
        int dx = 3;
        boolean clear = true;
        for (int dy = 0; dy < 3; ++dy) {
            Coords xy = topLeft.add(dx,dy);
            if (!floorTiles.contains(xy))
                clear = false;
        }
        if (clear) return true;
        
        int dy = 3;
        for (dx = 0; dx < 3; ++dx) {
            Coords xy = topLeft.add(dx,dy);
            if (!floorTiles.contains(xy))
                return false;
        }
        
        return true;
    }
    
    protected void checkOpenSpaces(Set<Coords> floorTiles)
            throws RetryException {
        // Any map with a 3x4 or 4x3 space of open floor is discarded
        for (Coords xy: floorTiles) {
            if (startsOpenSpace(xy, floorTiles))
                throw new RetryException();
        }
    }
    
    protected void checkEnoughSpaces(Set<Coords> floorTiles)
            throws RetryException {
        // The map must have enough space for the player, the planned number of
        // boxes and goals and at least one empty space
        if (floorTiles.size() < 2 + boxes*2)
            throw new RetryException();
    }
    
    protected void checkSurroundedFloor(IPuzzleMap map, Set<Coords> floorTiles)
            throws RetryException {
        // if the map contains any floor tiles surrounded on three sides by
        // walls, it is discarded
        Bounds bounds = map.getBounds();
        for (Coords xy: floorTiles) {
            int walls = 0;
            for (Direction d: Direction.values()) {
                Coords neighbor = xy.add(d.x,d.y);
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
    
    protected void checkMapConstraints() throws RetryException {
        Set<Coords> floorTiles = getFloorTiles(puzzleMap);
        checkEnoughSpaces(floorTiles);
        checkConnectivity(puzzleMap, floorTiles);
        checkOpenSpaces(floorTiles);
        checkSurroundedFloor(puzzleMap, floorTiles);
        if (!bounded)
            checkFloorOnEdge(puzzleMap);
    }
    
    @Override
    public void generate() {
        int attempts = 0;
        while (true) {
            try {
        
                templateMap = new TemplateMap(width, height);
                puzzle = new MappedPuzzle(templateMap);
                fillTemplateMap();
                
                // Copy the current map into a more efficient-to-access
                // structure
                puzzleMap = new PuzzleMap(templateMap);
                ((MappedPuzzle)puzzle).setMap(puzzleMap);
                
                // Will throw a RetryException if it fails
                checkMapConstraints();
                
                return;
                
            } catch (RetryException e) {
                ++attempts;
                assert attempts < 200;
            }
        }
    }

    @Override
    public IPuzzle getPuzzle() {
        return puzzle;
    }

}
