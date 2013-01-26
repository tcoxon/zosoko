package net.bytten.zosoko.generator;

import net.bytten.zosoko.IPuzzle;

public interface IPuzzleGenerator {

    public void generate();
    public IPuzzle getPuzzle();
    
}
