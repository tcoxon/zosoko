package net.bytten.zosoko;

import net.bytten.zosoko.util.Bounds;

public interface IPuzzleMap {
    
    public Bounds getBounds();
    public int getWidth();
    public int getHeight();
    
    public boolean isPlayerBounded();
    
    public Tile getTile(int x, int y);

}
