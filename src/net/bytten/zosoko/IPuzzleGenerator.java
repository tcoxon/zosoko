package net.bytten.zosoko;

import java.util.Collection;

public interface IPuzzleGenerator {

    public void generate();
    public Collection<IPuzzle> getPuzzleSet();
    
}
