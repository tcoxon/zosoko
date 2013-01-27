package net.bytten.zosoko.generator;

public class BoxLinesScoringMetric implements IScoringMetric {

    @Override
    public int score(PuzzleState state, int siblings) {
        return state.getBoxLines();
    }

}
