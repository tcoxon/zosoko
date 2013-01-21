package net.bytten.zosoko;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.bytten.zosoko.util.Bounds;

public class TestPuzzleGenerator implements IPuzzleGenerator {

    protected static class TestPuzzle implements IPuzzle {

        /* Test Puzzle 6x3
         * ########
         * #@. $  #
         * # $  # #
         * #     .#
         * ########
         */
        
        @Override
        public Bounds size() {
            return new Bounds(6,3);
        }
        
    }
    
    protected List<IPuzzle> puzzles;
    
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
