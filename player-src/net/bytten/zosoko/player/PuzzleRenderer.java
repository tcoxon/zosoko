package net.bytten.zosoko.player;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.util.Bounds;

public class PuzzleRenderer {

    public void draw(Graphics2D g, Dimension dim, IPuzzle puzzle) {
        AffineTransform origXfm = g.getTransform();
        
        // Figure out scale & translation to draw the dungeon at
        synchronized (puzzle) {
            Bounds bounds = puzzle.size();
            double scale = Math.min(((double)dim.width) / bounds.width(),
                    ((double)dim.height) / bounds.height());
            // move the graph into view
            g.translate(-scale * bounds.left, -scale * bounds.top);
            
            // TODO
        }
        
        g.setTransform(origXfm);
    }

}
