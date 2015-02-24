package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.gameutil.Rect2I;
import net.bytten.gameutil.Vec2I;

public class TestPuzzleGenerator implements IPuzzleGenerator {

    protected class TestPuzzle implements IPuzzle {

        /* Test Puzzle 6x3
         * ########
         * #@. $  #
         * # $  # #
         * #     .#
         * ########
         */
        
        @Override
        public Rect2I getBounds() {
            return new Rect2I(0, 0, 6,3);
        }

        @Override
        public List<Vec2I> getBoxStartPositions() {
            List<Vec2I> result = new ArrayList<Vec2I>(2);
            result.add(new Vec2I(1,1));
            result.add(new Vec2I(3,0));
            return result;
        }

        @Override
        public Vec2I getPlayerStartPosition() {
            return new Vec2I(0,0);
        }

        @Override
        public Tile getTile(int x, int y) {
            if (x == 1 && y == 0) return Tile.GOAL;
            if (x == 4 && y == 1) return Tile.WALL;
            if (x == 5 && y == 2) return Tile.GOAL;
            return Tile.FLOOR;
        }

        @Override
        public boolean isPlayerBounded() {
            return bounded;
        }

        @Override
        public int getWidth() {
            return 6;
        }

        @Override
        public int getHeight() {
            return 3;
        }
        
    }
    
    protected boolean bounded;
    protected IPuzzle puzzle;
    
    public TestPuzzleGenerator(boolean bounded) {
        this.bounded = bounded;
    }
    
    public TestPuzzleGenerator() {
        this(true);
    }
    
    @Override
    public void generate() {
        puzzle = new TestPuzzle();
    }

    @Override
    public IPuzzle getPuzzle() {
        return puzzle;
    }

}
