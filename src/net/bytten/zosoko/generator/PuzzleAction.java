package net.bytten.zosoko.generator;

import net.bytten.zosoko.util.Coords;
import net.bytten.zosoko.util.Direction;

public class PuzzleAction {
    
    protected Coords box;
    protected Direction pushDir;

    public PuzzleAction(Coords box, Direction pushDir) {
        this.box = box;
        this.pushDir = pushDir;
    }

}
