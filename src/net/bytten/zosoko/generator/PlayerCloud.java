package net.bytten.zosoko.generator;

import java.util.TreeSet;

import net.bytten.zosoko.IPuzzleMap;
import net.bytten.gameutil.Coords;

public class PlayerCloud implements Comparable<PlayerCloud> {

    // This could be made a lot smarter by working out the minimum cuts of the
    // map and then when a box partitions the graph, the PlayerCloud is the
    // partition the player is in.
    
    // Or maybe use A* to determine if two PlayerClouds' coords are in the
    // same partition (at the cost of making equals() more expensive).
    
    protected TreeSet<Coords> coords;
    
    public PlayerCloud(TreeSet<Coords> coords) {
        this.coords = coords;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerCloud) {
            PlayerCloud other = (PlayerCloud)o;
            if (coords.size() == 0) return other.coords.size() == 0;
            return other.coords.contains(getAnyCoords());
        }
        return false;
    }

    // Returns one of the coords in this cloud. Not guaranteed to return any
    // particular coordinates.
    public Coords getAnyCoords() {
        return coords.first();
    }

    @Override
    public int compareTo(PlayerCloud o) {
        if (equals(o)) return 0;
        if (coords.size() == 0) return -1;
        // Compare the top-left-most coordinates of each player's partition
        return coords.first().compareTo(o.coords.first());
    }

    public boolean canReach(Coords box) {
        return coords.contains(box);
    }

    public TreeSet<Coords> getCoordsSet() {
        return coords;
    }

    public boolean touchesEdge(IPuzzleMap map) {
        for (Coords pos: coords) {
            if (pos.x <= 0 || pos.y <= 0 || pos.x >= map.getWidth()-1 ||
                    pos.y >= map.getHeight()-1)
                return true;
        }
        return false;
    }
    
}
