package net.bytten.zosoko;

import net.bytten.gameutil.Rect2dI;

public interface IPuzzleMap {
    
    public Rect2dI getBounds();
    public int getWidth();
    public int getHeight();
    
    public boolean isPlayerBounded();
    
    public Tile getTile(int x, int y);

}
