package net.bytten.zosoko;

import java.util.List;

import net.bytten.gameutil.Coords;

public interface IPuzzle extends IPuzzleMap {
    
    public List<Coords> getBoxStartPositions();
    public Coords getPlayerStartPosition();
    
    // An unbounded puzzle allows the player (but not blocks) to move outside
    // of the grid area
    public boolean isPlayerBounded();
    
}
