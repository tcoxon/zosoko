package net.bytten.zosoko.generator;

import java.util.List;
import java.util.Random;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Coords;
import net.bytten.zosoko.util.ILogger;

public class PuzzleGenerator implements IPuzzleGenerator, ILogger {
    
    int width, height, boxes;
    boolean bounded;
    Integer goalAttemptLimit;
    
    TemplateMap templateMap;
    PuzzleMap puzzleMap;
    IPuzzle puzzle;
    Random rand;
    
    TemplateMapFiller mapFiller;
    MapConstraints mapConstraints;
    FarthestStateFinder farthestStateFinder;
    
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
        // TODO depth-limiting options
        farthestStateFinder = new FarthestStateFinder(bounded);
    }
    
    // null => no limit
    public void setGoalAttemptLimit(Integer val) {
        goalAttemptLimit = val;
    }
    
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
    
    protected Coords choosePlayerPos(PuzzleState bestStartState)
            throws RetryException {
        for (Coords xy: bestStartState.getPlayer().getCoordsSet()) {
            if (!bestStartState.getBoxes().contains(xy))
                return xy;
        }
        throw new RetryException();
    }
    
    protected void logActions(ActionPath actions) {
        if (actions == null) return;
        log("        Push box #"+actions.getBox()+" "+actions.getPushDir());
        logActions(actions.getPrevious());
    }
    
    protected void logSolution(PuzzleState startState, Coords playerPos) {
        if (logger == null) return;
        log("Solution:");
        log("    Goals:");
        for (Coords goal: startState.getGoals()) {
            log("        "+goal.toString());
        }
        log("    Boxes:");
        for (Coords box: startState.getBoxes()) {
            log("        "+box.toString());
        }
        log("    Player: "+playerPos.toString());
        log("    Actions ("+startState.getBoxLines()+" box-lines):");
        logActions(startState.getPath());
    }
    
    protected int score(PuzzleState startState) {
        // TODO
        return startState.getBoxLines();
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
                int bestScore = 0;
                PuzzleState bestStartState = null;
                while (goalSupplier.hasMore()) {
                    List<Coords> goals = goalSupplier.next();
                    
                    PuzzleState farthestState = farthestStateFinder.go(
                            puzzleMap, goals);
                    if (farthestState != null) {
                        int score = score(farthestState);
                        if (score > bestScore || bestStartState == null) {
                            bestStartState = farthestState;
                            bestScore = score;
                        }
                    }
                    
                    ++goalAttempts;
                }
                log("Goal experiments: "+goalAttempts);
                if (bestStartState == null)
                    throw new RetryException();
                
                log("Final score: "+bestScore);
                
                for (Coords goal: bestStartState.getGoals()) {
                    puzzleMap.setTile(goal.x, goal.y, Tile.GOAL);
                }
                
                // DONE!
                Coords playerPos = choosePlayerPos(bestStartState);
                puzzle = new Puzzle(puzzleMap, bestStartState.getBoxes(),
                        playerPos);
                
                log("Full reattempts: "+attempts);
                
                logSolution(bestStartState, playerPos);
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

    public void setDepthLimit(Integer depthLimit) {
        farthestStateFinder.setDepthLimit(depthLimit);
    }

}
