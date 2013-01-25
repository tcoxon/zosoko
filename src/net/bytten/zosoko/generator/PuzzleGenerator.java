package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.Collection;
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

    private void generateTemplateMap() {
        templateMap = new TemplateMap(width, height);
        for (int x = 0; x < width; ++x)
        for (int y = 0; y < height; ++y) {
            while (true) {
                Template template = Template.values()[
                        rand.nextInt(Template.values().length)];
                TemplateTransform transform = new TemplateTransform(rand);
                if (template.check(templateMap, transform, new Coords(x,y))) {
                    templateMap.put(x, y, template, transform);
                    break;
                }
            }
        }
    }
    
    @Override
    public void generate() {
        generateTemplateMap();
        puzzle = new TemplatePuzzle(templateMap);
    }

    @Override
    public Collection<IPuzzle> getPuzzleSet() {
        List<IPuzzle> puzzles = new ArrayList<IPuzzle>(1);
        if (puzzle != null)
            puzzles.add(puzzle);
        return puzzles;
    }

}
