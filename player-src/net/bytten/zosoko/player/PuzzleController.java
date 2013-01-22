package net.bytten.zosoko.player;

import java.awt.event.KeyEvent;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.util.Coords;

public class PuzzleController {
    
    PlayingPuzzle puzzle;
    
    public PlayingPuzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(PlayingPuzzle puzzle) {
        this.puzzle = puzzle;
    }
    
    private void move(int dx, int dy) {
        Coords pos = puzzle.getPlayerPosition(),
               newPos = pos.add(dx,dy);
        
        if (newPos.x < 0 || newPos.y < 0 ||
                newPos.x >= puzzle.getSize().width() ||
                newPos.y >= puzzle.getSize().height()) {
            return;
        }
        
        if (puzzle.get(newPos.x, newPos.y) == IPuzzle.Tile.WALL)
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
