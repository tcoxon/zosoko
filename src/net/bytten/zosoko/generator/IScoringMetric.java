package net.bytten.zosoko.generator;

public interface IScoringMetric {

    public int score(PuzzleState state, int siblings);
    
}
