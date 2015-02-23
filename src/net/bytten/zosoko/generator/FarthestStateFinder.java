package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Coords;
import net.bytten.gameutil.Direction;

public class FarthestStateFinder {
    
    public static class Result {
        public final PuzzleState startState;
        public final int siblings;
        
        public Result(PuzzleState startState, int siblings) {
            this.startState = startState;
            this.siblings = siblings;
        }
    }
    
    protected PuzzleMap map;
    protected boolean bounded;
    protected Integer depthLimit;

    public FarthestStateFinder(boolean bounded) {
        this.bounded = bounded;
    }
    
    protected PuzzleState chooseBest(Set<PuzzleState> states) {
        int bestScore = 0;
        PuzzleState best = null;
        for (PuzzleState state: states) {
            if (state.getBoxLines() > bestScore || best == null) {
                bestScore = state.getBoxLines();
                best = state;
            }
        }
        return best;
    }
    
    protected Set<PuzzleState> makeStartSet(List<Coords> goals)
            throws RetryException {
        Set<PuzzleState> states = new TreeSet<PuzzleState>();
        // TODO put player into each available space partition. At the moment
        // it only places the player into one area
        Coords player;
        if (!bounded) {
            player = PuzzleMap.getAnyBorderNonWallTile(map);
            if (player == null) throw new RetryException();
        } else {
            player = PuzzleMap.getAnyFloorTile(map);
        }
        states.add(new PuzzleState.Builder(map, goals)
            .setBoxes(goals)
            .setPath(null)
            .setPlayer(player)
            .build());
        return states;
    }
    
    protected PuzzleState derive(PuzzleState state, int box, Direction pull) {
        List<Coords> boxes = state.getBoxes(),
                newBoxes = new ArrayList<Coords>(boxes);
        Coords newBoxPos = boxes.get(box).add(pull.x, pull.y),
               playerPos = newBoxPos.add(pull.x, pull.y);
        newBoxes.set(box, newBoxPos);
        
        ActionPath action = new ActionPath(state.getPath(), box, pull);
        return new PuzzleState.Builder(state.getMap(), state.getGoals())
            .setPath(action)
            .setBoxes(newBoxes)
            .setPlayer(playerPos)
            .build();
    }
    
    protected Set<PuzzleState> expand(PuzzleState state) {
        Set<PuzzleState> states = new TreeSet<PuzzleState>();
        
        PlayerCloud player = state.getPlayer();
        for (int boxnum = 0; boxnum < state.getBoxes().size(); ++boxnum) {
            Coords box = state.getBoxes().get(boxnum);
            if (player.canReach(box)) {
                for (Direction d: Direction.COMPASS_DIRECTIONS) {
                    // Since we're working backwards from the goal, the player
                    // _pulls_ boxes
                    Coords nextBoxPos = box.add(d.x,d.y),
                           nextPlayerPos = nextBoxPos.add(d.x,d.y);
                    if (!player.canReach(nextBoxPos)) continue;
                    if (state.getBoxes().contains(nextPlayerPos) ||
                            state.getBoxes().contains(nextBoxPos) ||
                            !state.getMap().getBounds().contains(nextBoxPos) ||
                            !state.getMap().getBounds().contains(nextPlayerPos) ||
                            state.getMap().getTile(nextBoxPos.x, nextBoxPos.y)
                                    != Tile.FLOOR ||
                            state.getMap().getTile(nextPlayerPos.x, nextPlayerPos.y)
                                    == Tile.WALL) {
                        continue;
                    }
                    
                    states.add(derive(state, boxnum, d));
                }
            }
        }
        
        return states;
    }
    
    protected Set<PuzzleState> expand(Set<PuzzleState> states) {
        Set<PuzzleState> newStates = new TreeSet<PuzzleState>();
        for (PuzzleState state: states) {
            newStates.addAll(expand(state));
        }
        return newStates;
    }
    
    // Known as 'Try' in the paper...
    protected Set<PuzzleState> deepen(Set<PuzzleState> startSet,
            Set<PuzzleState> prevResults, int depth) {
        
        Set<PuzzleState> resultSet = expand(prevResults);
        Set<PuzzleState> tempSet = startSet;
        for (int i = 1; i < depth; ++i) {
            resultSet.removeAll(tempSet);
            tempSet = expand(tempSet);
        }
        return resultSet;
        
    }

    public Result go(PuzzleMap map, List<Coords> goals) throws RetryException {
        this.map = map;
        for (Coords goal: goals) {
            map.setTile(goal.x, goal.y, Tile.GOAL);
        }
        try {
            
            Set<PuzzleState> startSet = makeStartSet(goals);
            Set<PuzzleState> resultSet = startSet, prevSet = null;
            for (int depth = 1; resultSet.size() > 0 &&
                    (depthLimit == null || depth <= depthLimit); ++depth) {
                prevSet = resultSet;
                resultSet = deepen(startSet, resultSet, depth);
            }
            
            if (!bounded) {
                Set<PuzzleState> newSet = new TreeSet<PuzzleState>(prevSet);
                for (PuzzleState state: prevSet) {
                    if (!state.player.touchesEdge(map)) {
                        newSet.remove(state);
                    }
                }
                prevSet = newSet;
            }
            
            if (prevSet == null || prevSet.size() == 0)
                throw new RetryException();
            return new Result(chooseBest(prevSet), prevSet.size()-1);
        } finally {
            // Clear up afterwards: remove the goals
            for (Coords goal: goals) {
                map.setTile(goal.x, goal.y, Tile.FLOOR);
            }
        }
    }

    public void setDepthLimit(Integer depthLimit) {
        this.depthLimit = depthLimit;
    }

}
