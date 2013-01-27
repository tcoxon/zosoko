package net.bytten.zosoko.generator;

import net.bytten.zosoko.util.Direction;

public class ActionPath {
    
    protected int box;
    protected Direction pullDir;
    protected ActionPath previous;

    public ActionPath(ActionPath previous, int box, Direction pushDir) {
        this.previous = previous;
        this.box = box;
        this.pullDir = pushDir;
    }
    
    public int getBox() {
        return box;
    }

    public Direction getPullDir() {
        return pullDir;
    }
    
    public Direction getPushDir() {
        return pullDir.opposite();
    }
    
    public ActionPath getPrevious() {
        return previous;
    }
    
}
