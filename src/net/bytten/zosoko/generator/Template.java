package net.bytten.zosoko.generator;

import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Coords;

public enum Template {

    A(mktiles(
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    )),
    
    B(mktiles(
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    )),
    
    C(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(0, -1, 2, 2, Tile.FLOOR),
            mkreq(1, -1, 0, 2, Tile.FLOOR),
            mkreq(1, 0, 0, 0, Tile.FLOOR)
    )),
    
    D(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    )),
    
    E(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR)
    )),
    
    F(mktiles(
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.WALL)
    ), mkreqs(
            mkreq(0, -1, 1, 2, Tile.FLOOR),
            mkreq(-1, 0, 2, 1, Tile.FLOOR)
    )),
    
    G(mktiles(
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(-1, 0, 2, 1, Tile.FLOOR)
    )),
    
    H(mktiles(
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.WALL)
    ), mkreqs(
            mkreq(0, -1, 1, 2, Tile.FLOOR),
            mkreq(-1, 0, 2, 1, Tile.FLOOR),
            mkreq(0, 1, 1, 0, Tile.FLOOR)
    )),
    
    I(mktiles(
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.WALL),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.WALL)
    ), mkreqs(
            mkreq(0, -1, 1, 2, Tile.FLOOR),
            mkreq(-1, 0, 2, 1, Tile.FLOOR),
            mkreq(0, 1, 1, 0, Tile.FLOOR),
            mkreq(1, 0, 0, 1, Tile.FLOOR)
    )),
    
    J(mktiles( // TODO Template considered blocking
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.WALL),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL)
    ), mkreqs(
            mkreq(0, -1, 1, 2, Tile.FLOOR),
            mkreq(1, 0, 0, 1, Tile.FLOOR)
    )),
    
    K(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL)
    ), mkreqs(
            mkreq(-1, 0, 2, 1, Tile.FLOOR),
            mkreq(1, 0, 0, 1, Tile.FLOOR)
    )),
    
    L(mktiles(
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.WALL,  Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(1, 0, 0, 0, Tile.FLOOR),
            mkreq(1, 0, 0, 1, Tile.FLOOR)
    )),
    
    M(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL)
    )),
    
    N(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(0, 1, 0, 0, Tile.FLOOR),
            mkreq(-1, 1, 2, 0, Tile.FLOOR),
            mkreq(-1, 0, 2, 2, Tile.FLOOR)
    )),
    
    O(mktiles(
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR),
            mkrow(Tile.WALL,  Tile.FLOOR, Tile.WALL),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(0, -1, 0, 2, Tile.FLOOR),
            mkreq(0, -1, 2, 2, Tile.FLOOR),
            mkreq(0, 1, 0, 0, Tile.FLOOR),
            mkreq(0, 1, 2, 0, Tile.FLOOR)
    )),
    
    P(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(0, 1, 0, 0, Tile.FLOOR),
            mkreq(0, 1, 1, 0, Tile.FLOOR),
            mkreq(0, 1, 2, 0, Tile.FLOOR)
    )),
    
    Q(mktiles(
            mkrow(Tile.WALL,  Tile.WALL,  Tile.WALL),
            mkrow(Tile.FLOOR, Tile.WALL,  Tile.FLOOR),
            mkrow(Tile.FLOOR, Tile.FLOOR, Tile.FLOOR)
    ), mkreqs(
            mkreq(-1, 0, 2, 1, Tile.FLOOR),
            mkreq(1, 0, 0, 1, Tile.FLOOR),
            mkreq(0, 1, 0, 0, Tile.FLOOR),
            mkreq(0, 1, 1, 0, Tile.FLOOR)
    )),
    
    ;
    
    public final Tile[][] tiles;
    private final Require[] reqs;
    
    private Template(Tile[][] tiles, Require[] reqs) {
        this.tiles = tiles;
        this.reqs = reqs;
    }
    
    private Template(Tile[][] tiles) {
        this(tiles, mkreqs());
    }
    
    public boolean check(TemplateMap map, TemplateTransform xfm, Coords index) {
        for (Require req: reqs) {
            Coords neighborPos = index.add(xfm.apply(req.d));
            if (!map.getSize().contains(neighborPos)) return false;
            Template neighbor = map.getTemplate(neighborPos);
            if (neighbor == null) continue;
            
            Coords tilePos = map.getTransform(neighborPos).applyTile(req.s);
            if (neighbor.tiles[tilePos.x][tilePos.y] != req.mustBe)
                return false;
        }
        return true;
    }
    
    private static class Require {
        Coords d, // delta relative to the position of the template
               s; // position of the tile in neighboring template
        Tile mustBe;
    }
    
    private static Tile[] mkrow(Tile... tiles) {
        return tiles;
    }
    
    private static Tile[][] mktiles(Tile[]... rows) {
        return rows;
    }
    
    private static Require mkreq(int dx, int dy, int sx, int sy,
            Tile mustBe) {
        Require req = new Require();
        req.d = new Coords(dx, dy);
        req.s = new Coords(sx, sy);
        req.mustBe = mustBe;
        return req;
    }
    
    private static Require[] mkreqs(Require... reqs) {
        return reqs;
    }
}
