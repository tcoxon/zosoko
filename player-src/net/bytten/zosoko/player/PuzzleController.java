package net.bytten.zosoko.player;

import java.awt.event.KeyEvent;
import java.util.List;

import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Coords;

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
                pos.x >= puzzle.getBounds().width() ||
                pos.y >= puzzle.getBounds().height());
    }
    
    private boolean validPlayerPos(Coords pos) {
        if (insideMap(pos) && puzzle.getTile(pos.x, pos.y) == Tile.WALL)
            return false;
        if (puzzle.isPlayerBounded()) {
            return insideMap(pos);
        } else {
            return !(pos.x < -1 || pos.y < -1 ||
                    pos.x > puzzle.getBounds().width() ||
                    pos.y > puzzle.getBounds().height());
        }
    }
    
    private boolean tryMoveBox(int box, int dx, int dy) {
        Coords pos = puzzle.getBoxPositions().get(box),
               newPos = pos.add(dx, dy);
        
        if (!insideMap(newPos) ||
                puzzle.getTile(newPos.x, newPos.y) == Tile.WALL ||
                puzzle.getBoxPositions().contains(newPos))
            return false;
        
        puzzle.getBoxPositions().set(box, newPos);
        return true;
    }
    
    private void move(int dx, int dy) {
        Coords pos = puzzle.getPlayerPosition(),
               newPos = pos.add(dx,dy);
        
        if (!validPlayerPos(newPos))
            return;
        
        List<Coords> boxes = puzzle.getBoxPositions();
        for (int box = 0; box < boxes.size(); ++box) {
            if (boxes.get(box).equals(newPos)) {
                if (!tryMoveBox(box, dx, dy))
                    // The box is blocked; player cannot move it
                    return;
            }
        }
        
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
