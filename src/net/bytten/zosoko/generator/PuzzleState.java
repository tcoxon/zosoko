package net.bytten.zosoko.generator;

import java.util.List;

import net.bytten.zosoko.util.Coords;

public class PuzzleState implements Comparable<PuzzleState> {

    protected PuzzleMap map;
    protected List<Coords> goals;
    
    protected PlayerCloud player;
    protected List<Coords> boxes;
    
    protected int score;
    protected List<PuzzleAction> path;
    
    public PuzzleState(PuzzleMap map, List<Coords> goals) {
        this.map = map;
        this.goals = goals;
    }
    
    public void computeScore() {
        // TODO
    }

    // Slightly magic because it will set the path for each of the objects to
    // the shortest path when they compare equal.
    @Override
    public int compareTo(PuzzleState o) {
        // TODO Auto-generated method stub
        if (equals(o))
            return 0;
        return 1;
    }

    // Slightly magic because it will set the path for each of the objects to
    // the shortest path when they compare equal.
    @Override
    public boolean equals(Object o) {
        if (o instanceof PuzzleState) {
            PuzzleState other = (PuzzleState)o;
            if (player.equals(other.player) &&
                    boxes.equals(other.boxes)) {
                if (score < other.score) {
                    other.score = score;
                    other.path = path;
                } else if (other.score < score) {
                    score = other.score;
                    path = other.path;
                }
                return true;
            }
        }
        return false;
    }

    public PlayerCloud getPlayer() {
        return player;
    }

    public void setPlayer(PlayerCloud player) {
        this.player = player;
    }

    public List<Coords> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Coords> boxes) {
        this.boxes = boxes;
    }

}
