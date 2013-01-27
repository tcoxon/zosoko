package net.bytten.zosoko.generator;

import java.util.Set;

import net.bytten.zosoko.util.Coords;

public class PlayerCloud {

    // This could be made a lot smarter by working out the minimum cuts of the
    // map and then when a box partitions the graph, the PlayerCloud is the
    // partition the player is in.
    protected Set<Coords> coords;
    
    public PlayerCloud(Set<Coords> coords) {
        this.coords = coords;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerCloud) {
            PlayerCloud other = (PlayerCloud)o;
            if (coords.size() == 0) return other.coords.size() == 0;
            return other.coords.contains(coords.iterator().next());
        }
        return false;
    }

}
