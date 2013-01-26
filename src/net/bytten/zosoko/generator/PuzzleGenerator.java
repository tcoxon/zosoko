package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

public class PuzzleGenerator implements IPuzzleGenerator {
    
    private class TemplatePuzzle implements IPuzzle {
        
        final TemplateMap map;

        public TemplatePuzzle(TemplateMap map) {
            this.map = map;
        }
        
        @Override
        public List<Coords> getBoxStartPositions() {
            return new ArrayList<Coords>(0);
        }

        @Override
        public Coords getPlayerStartPosition() {
            return new Coords(0,0);
        }

        @Override
        public Bounds getSize() {
            return new Bounds(map.getWidth()*3, map.getHeight()*3);
        }

        @Override
        public Tile get(int x, int y) {
            return map.getTile(x, y);
        }

        @Override
        public boolean isBounded() {
            return bounded;
        }
        
    }

    int width, height, boxes;
    boolean bounded;
    
    TemplateMap templateMap;
    IPuzzle puzzle;
    Random rand;
    
    public PuzzleGenerator(Random rand, int width, int height, int boxes,
            boolean bounded) {
        this.rand = rand;
        this.width = width;
        this.height = height;
        this.boxes = boxes;
        this.bounded = bounded;
    }
    
    private boolean templateFits(Template template, TemplateTransform transform,
            int x, int y) {
        if (!template.check(templateMap, transform, new Coords(x,y)))
            return false;
        // check the neighbors already placed fit with this template...
        if (x > 0) {
            Template tpl = templateMap.getTemplate(x-1, y);
            TemplateTransform xfm = templateMap.getTransform(x-1, y);
            if (!tpl.check(templateMap, xfm, new Coords(x-1, y)))
                return false;
        }
        if (y > 0) {
            Template tpl = templateMap.getTemplate(x, y-1);
            TemplateTransform xfm = templateMap.getTransform(x, y-1);
            if (!tpl.check(templateMap, xfm, new Coords(x, y-1)))
                return false;
        }
        if (x > 0 && y > 0) {
            Template tpl = templateMap.getTemplate(x-1, y-1);
            TemplateTransform xfm = templateMap.getTransform(x-1, y-1);
            if (!tpl.check(templateMap, xfm, new Coords(x-1, y-1)))
                return false;
        }
        
        return true;
    }

    private void fillTemplateMap() {
        for (int x = 0; x < width; ++x)
        for (int y = 0; y < height; ++y) {
            while (true) {
                Template template = Template.values()[
                        rand.nextInt(Template.values().length)];
                TemplateTransform transform = new TemplateTransform(rand);
                templateMap.put(x, y, template, transform);
                if (templateFits(template, transform, x, y)) {
                    break;
                }
            }
        }
    }
    
    @Override
    public void generate() {
        templateMap = new TemplateMap(width, height);
        puzzle = new TemplatePuzzle(templateMap);
        fillTemplateMap();
    }

    @Override
    public IPuzzle getPuzzle() {
        return puzzle;
    }

}
