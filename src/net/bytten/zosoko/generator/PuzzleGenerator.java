package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.IPuzzleMap;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;
import net.bytten.zosoko.util.ILogger;

public class PuzzleGenerator implements IPuzzleGenerator, ILogger {
    
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

    int width, height, boxes;
    boolean bounded;
    Integer goalAttemptLimit;
    
    TemplateMap templateMap;
    PuzzleMap puzzleMap;
    IPuzzle puzzle;
    Random rand;
    
    TemplateMapFiller mapFiller;
    MapConstraints mapConstraints;
    
    ILogger logger;
    
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
    
    // null => no limit
    public void setGoalAttemptLimit(Integer val) {
        goalAttemptLimit = val;
    }
    
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
    
    protected int tryGoals(List<Coords> goals) {
        // TODO find the farthest state from the goal and score it
        return 1;
    }
    
    @Override
    public void generate() {
        int attempts = 0;
        while (true) {
            try {
        
                templateMap = new TemplateMap(bounded, width, height);
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
                
                GoalSupplier goalSupplier = new GoalSupplier(rand, boxes,
                        puzzleMap, goalAttemptLimit, false);
                int goalAttempts = 0;
                int bestGoalScore = 0;
                List<Coords> bestGoals = null;
                while (goalSupplier.hasMore()) {
                    List<Coords> goals = goalSupplier.next();
                    
                    int score = tryGoals(goals);
                    if (score > bestGoalScore) {
                        bestGoals = goals;
                    }
                    
                    ++goalAttempts;
                }
                log("Goal experiments: "+goalAttempts);
                if (bestGoals == null)
                    throw new RetryException();
                
                for (Coords goal: bestGoals) {
                    puzzleMap.setTile(goal.x, goal.y, Tile.GOAL);
                }
                
                log("Full reattempts: "+attempts);
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

    @Override
    public void log(String msg) {
        if (logger != null) logger.log(msg);
    }

}
