package net.bytten.zosoko.generator;

import java.util.Collection;

import net.bytten.zosoko.IPuzzle;

public interface IPuzzleGenerator {

    public void generate();
    public Collection<IPuzzle> getPuzzleSet();
    
}
