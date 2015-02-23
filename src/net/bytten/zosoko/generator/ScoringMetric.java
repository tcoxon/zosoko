package net.bytten.zosoko.generator;

import java.util.Random;

import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2dI;
import net.bytten.gameutil.Coords;
import net.bytten.gameutil.Direction;

public class ScoringMetric implements IScoringMetric {
    
    protected Random rand;
    
    public ScoringMetric(Random rand) {
        this.rand = rand;
    }

    protected int baseScore(PuzzleState state, int siblings) {
        return state.countPushes() - siblings
                + 4 * state.getBoxLines()
                - 12 * state.getBoxes().size()
                + rand.nextInt(300);
    }
    
    private boolean isTouchingWall(Coords box, PuzzleState state) {
        Rect2dI bounds = state.getMap().getBounds();
        for (Direction d: Direction.COMPASS_DIRECTIONS) {
            Coords neighbor = box.add(d.x,d.y);
            if (!bounds.contains(neighbor)) {
                if (state.getMap().isPlayerBounded())
                    return true;
            } else {
                if (state.getMap().getTile(neighbor.x, neighbor.y) == Tile.WALL)
                    return true;
            }
        }
        return false;
    }
    
    private boolean isTouchingBox(Coords box, PuzzleState state) {
        for (Direction d: Direction.COMPASS_DIRECTIONS) {
            Coords neighbor = box.add(d.x,d.y);
            if (state.getBoxes().contains(neighbor))
                return true;
        }
        return false;
    }
    
    private boolean isTouchingGoal(Coords goal, PuzzleState state) {
        for (Direction d: Direction.COMPASS_DIRECTIONS) {
            Coords neighbor = goal.add(d.x,d.y);
            if (state.getGoals().contains(neighbor))
                return true;
        }
        return false;
    }
    
    @Override
    public int score(PuzzleState state, int siblings) {
        int score = baseScore(state, siblings);
        
        // According to the paper, a trapped box is worth -100000 points.
        // TODO: What is a trapped box?
        
        // TODO: A box touching the player is worth 50, but we haven't decided
        // where the player will be yet...
        
        for (Coords box: state.getBoxes()) {
            if (isTouchingWall(box, state))
                score -= 150;
            if (isTouchingBox(box, state))
                score += 30;
        }
        for (Coords goal: state.getGoals()) {
            if (isTouchingGoal(goal, state))
                score += 30;
        }
        
        // Penalize boxes already on goals:
        for (Coords box: state.getBoxes()) {
            if (state.getGoals().contains(box))
                score -= 100000;
        }
        
        return score;
    }

}
