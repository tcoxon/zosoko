package net.bytten.zosoko;

import java.util.List;

import net.bytten.gameutil.Vec2I;

public interface IPuzzle extends IPuzzleMap {
    
    public List<Vec2I> getBoxStartPositions();
    public Vec2I getPlayerStartPosition();
    
    // An unbounded puzzle allows the player (but not blocks) to move outside
    // of the grid area
    public boolean isPlayerBounded();
    
}
