package net.bytten.zosoko;

import net.bytten.gameutil.Rect2I;

public interface IPuzzleMap {
    
    public Rect2I getBounds();
    public int getWidth();
    public int getHeight();
    
    public boolean isPlayerBounded();
    
    public Tile getTile(int x, int y);

}
