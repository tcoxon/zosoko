package net.bytten.zosoko.generator;

import java.util.Random;

import net.bytten.zosoko.util.Coords;

// FIXME This is a mess. Replace with matrix math please
public class TemplateTransform {
    
    private final boolean flip;
    private final int rot;

    public TemplateTransform(boolean flip, int rot) {
        this.flip = flip;
        this.rot = rot;
        assert rot >= 0 && rot < 4;
    }
    
    public TemplateTransform(Random r) {
        this(r.nextBoolean(), r.nextInt(4));
    }
    
    public Coords apply(Coords xy) {
        if (flip) xy = new Coords(-xy.x, xy.y);
        switch (rot) {
        default:
            assert false;
        case 0:
            return xy;
        case 1:
            return new Coords(-xy.y, xy.x);
        case 2:
            return new Coords(-xy.x, -xy.y);
        case 3:
            return new Coords(xy.y, -xy.x);
        }
    }
    
    public Coords unapply(Coords xy) {
        switch (rot) {
        default:
            assert false;
        case 0:
            break;
        case 1:
            xy = new Coords(xy.y, -xy.x);
            break;
        case 2:
            xy = new Coords(-xy.x, -xy.y);
            break;
        case 3:
            xy = new Coords(-xy.y, xy.x);
            break;
        }
        if (flip) xy = new Coords(-xy.x, xy.y);
        return xy;
    }
    
    public Coords applyTile(Coords xy) {
        return apply(xy.add(-1,-1)).add(1,1);
    }

    public Coords unapplyTile(Coords xy) {
        return unapply(xy.add(-1,-1)).add(1,1);
    }

}
