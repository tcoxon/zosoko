package net.bytten.zosoko.generator;

import net.bytten.zosoko.util.Direction;

public class ActionPath {
    
    protected int box, boxLines;
    protected Direction pullDir;
    protected ActionPath previous;

    public ActionPath(ActionPath previous, int box, Direction pushDir) {
        this.previous = previous;
        this.box = box;
        this.pullDir = pushDir;
        
        this.boxLines = computeBoxLines();
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
    
    protected int computeBoxLines() {
        // Box lines counts how many times the player pushes a box, but any
        // number of pushes of the same box in the same direction only count
        // as a single box line
        if (previous == null) {
            return 1;
        } else {
            if (previous.box == box && previous.pullDir == pullDir)
                return previous.boxLines;
            return previous.boxLines + 1;
        }
    }

    public int getBoxLines() {
        return boxLines;
    }
    
    public int countPushes() {
        if (previous == null) return 1;
        return previous.countPushes()+1;
    }

}
