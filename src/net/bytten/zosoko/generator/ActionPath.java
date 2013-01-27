package net.bytten.zosoko.generator;

import net.bytten.zosoko.util.Coords;
import net.bytten.zosoko.util.Direction;

public class ActionPath {
    
    protected Coords box;
    protected Direction pushDir;
    protected ActionPath previous;

    public ActionPath(ActionPath previous, Coords box, Direction pushDir) {
        this.previous = previous;
        this.box = box;
        this.pushDir = pushDir;
    }

}
