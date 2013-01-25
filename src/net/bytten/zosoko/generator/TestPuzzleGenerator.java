package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

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
        public Bounds getSize() {
            return new Bounds(6,3);
        }

        @Override
        public List<Coords> getBoxStartPositions() {
            List<Coords> result = new ArrayList<Coords>(2);
            result.add(new Coords(1,1));
            result.add(new Coords(3,0));
            return result;
        }

        @Override
        public Coords getPlayerStartPosition() {
            return new Coords(0,0);
        }

        @Override
        public Tile get(int x, int y) {
            if (x == 1 && y == 0) return Tile.GOAL;
            if (x == 4 && y == 1) return Tile.WALL;
            if (x == 5 && y == 2) return Tile.GOAL;
            return Tile.FLOOR;
        }

        @Override
        public boolean isBounded() {
            return bounded;
        }
        
    }
    
    protected boolean bounded;
    protected List<IPuzzle> puzzles;
    
    public TestPuzzleGenerator(boolean bounded) {
        this.bounded = bounded;
    }
    
    public TestPuzzleGenerator() {
        this(true);
    }
    
    @Override
    public void generate() {
        puzzles = new ArrayList<IPuzzle>(1);
        puzzles.add(new TestPuzzle());
    }

    @Override
    public Collection<IPuzzle> getPuzzleSet() {
        return puzzles;
    }

}