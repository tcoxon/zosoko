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
    
    protected void checkOpenSpaces(IPuzzleMap map) throws RetryException {
        
    }
    
    protected void checkEnoughSpaces(IPuzzleMap map) throws RetryException {
        
    }
    
    protected void checkSurroundedFloor(IPuzzleMap map) throws RetryException {
        
    }
    
    protected void checkMapConstraints() throws RetryException {
        Set<Coords> floorTiles = getFloorTiles(puzzleMap);
        checkConnectivity(puzzleMap, floorTiles);
        checkOpenSpaces(puzzleMap);
        checkEnoughSpaces(puzzleMap);
        checkSurroundedFloor(puzzleMap);
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
