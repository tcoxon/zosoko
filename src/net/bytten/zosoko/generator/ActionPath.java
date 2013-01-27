package net.bytten.zosoko.generator;

import net.bytten.zosoko.util.Direction;

public class ActionPath {
    
    protected int box;
    protected Direction pushDir;
    protected ActionPath previous;

    public ActionPath(ActionPath previous, int box, Direction pushDir) {
        this.previous = previous;
        this.box = box;
        this.pushDir = pushDir;
    }

}
