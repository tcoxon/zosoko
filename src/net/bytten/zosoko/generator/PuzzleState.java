package net.bytten.zosoko.generator;

import java.util.List;

import net.bytten.zosoko.util.Coords;

public class PuzzleState implements Comparable<PuzzleState> {
    
    public static class Builder {
        protected PuzzleState state;
        
        public Builder(PuzzleMap map, List<Coords> goals) {
            state = new PuzzleState(map, goals);
        }
        
        public Builder setPlayer(PlayerCloud player) {
            state.player = player;
            return this;
        }

        public Builder setPlayer(Coords player) {
            // the floor partition the given coords are in depends on where
            // the boxes are.
            assert state.boxes != null;
            state.player = new PlayerCloud(PuzzleMap.getPlayerSpacePartition(
                    state.map, state.boxes, player));
            return this;
        }

        public Builder setBoxes(List<Coords> boxes) {
            state.boxes = boxes;
            return this;
        }
        
        public Builder setPath(ActionPath path) {
            state.path = path;
            return this;
        }
        
        public PuzzleState build() {
            assert state.boxes != null && state.player != null;
            return state;
        }

    }

    protected PuzzleMap map;
    protected List<Coords> goals;
    
    protected PlayerCloud player;
    protected List<Coords> boxes;
    
    protected ActionPath path;
    
    protected PuzzleState(PuzzleMap map, List<Coords> goals) {
        this.map = map;
        this.goals = goals;
    }
    
    // Slightly magic because it will set the path for each of the objects to
    // the shortest path when they compare equal.
    @Override
    public int compareTo(PuzzleState o) {
        if (equals(o))
            return 0;
        assert boxes.size() == o.boxes.size();
        for (int i = 0; i < boxes.size(); ++i) {
            Coords box = boxes.get(i),
                   obox = o.boxes.get(i);
            int cmp = box.compareTo(obox);
            if (cmp != 0) return cmp;
        }
        return player.compareTo(o.player);
    }

    // Slightly magic because it will set the path for each of the objects to
    // the shortest path when they compare equal.
    @Override
    public boolean equals(Object o) {
        if (o instanceof PuzzleState) {
            PuzzleState other = (PuzzleState)o;
            if (boxes.equals(other.boxes) && player.equals(other.player)) {
                if (getBoxLines() < other.getBoxLines()) {
                    other.path = path;
                } else if (other.getBoxLines() < getBoxLines()) {
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

    public List<Coords> getBoxes() {
        return boxes;
    }

    public int getBoxLines() {
        return path == null ? 0 : path.getBoxLines();
    }

    public List<Coords> getGoals() {
        return goals;
    }

    public PuzzleMap getMap() {
        return map;
    }

    public ActionPath getPath() {
        return path;
    }

}
