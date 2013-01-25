package net.bytten.zosoko.player;

import java.awt.event.KeyEvent;

import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Coords;

public class PuzzleController {
    
    PlayingPuzzle puzzle;
    
    public PlayingPuzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(PlayingPuzzle puzzle) {
        this.puzzle = puzzle;
    }
    
    private boolean insideMap(Coords pos) {
        return !(pos.x < 0 || pos.y < 0 ||
                pos.x >= puzzle.getSize().width() ||
                pos.y >= puzzle.getSize().height());
    }
    
    private boolean validPlayerPos(Coords pos) {
        if (insideMap(pos) && puzzle.get(pos.x, pos.y) == Tile.WALL)
            return false;
        if (puzzle.isBounded()) {
            return insideMap(pos);
        } else {
            return !(pos.x < -1 || pos.y < -1 ||
                    pos.x > puzzle.getSize().width() ||
                    pos.y > puzzle.getSize().height());
        }
    }
    
    private void move(int dx, int dy) {
        Coords pos = puzzle.getPlayerPosition(),
               newPos = pos.add(dx,dy);
        
        if (!validPlayerPos(newPos))
            return;
        
        puzzle.setPlayerPosition(newPos);
    }
    
    public void key(int key) {
        if (puzzle == null) return;
        
        switch (key) {
        case KeyEvent.VK_LEFT:
            move(-1, 0);
            break;
        case KeyEvent.VK_RIGHT:
            move(1, 0);
            break;
        case KeyEvent.VK_UP:
            move(0, -1);
            break;
        case KeyEvent.VK_DOWN:
            move(0, 1);
            break;
        }
    }
}
