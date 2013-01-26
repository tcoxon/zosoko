package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

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
    
    TemplateMapFiller mapFiller;
    MapConstraints mapConstraints;
    
    public PuzzleGenerator(Random rand, int width, int height, int boxes,
            boolean bounded) {
        this.rand = rand;
        this.width = width;
        this.height = height;
        this.boxes = boxes;
        this.bounded = bounded;
        
        mapFiller = new TemplateMapFiller(rand);
        mapConstraints = new MapConstraints(bounded, boxes);
    }
    
    @Override
    public void generate() {
        int attempts = 0;
        while (true) {
            try {
        
                templateMap = new TemplateMap(width, height);
                puzzle = new MappedPuzzle(templateMap);
                
                // Fill the map with random 3x3 templates (randomly rotated and
                // flipped).
                mapFiller.fill(templateMap);
                
                // Copy the current map into a more efficient-to-access
                // structure
                puzzleMap = new PuzzleMap(templateMap);
                ((MappedPuzzle)puzzle).setMap(puzzleMap);
                
                // This check will throw a RetryException if the map does not
                // meet certain criteria.
                mapConstraints.check(puzzleMap);
                
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
