package net.bytten.zosoko.generator;

import java.util.Random;

import net.bytten.gameutil.Coords;

public class TemplateMapFiller {

    protected Random rand;
    
    public TemplateMapFiller(Random rand) {
        this.rand = rand;
    }
    
    protected boolean templateFits(TemplateMap map, Template template,
            TemplateTransform transform, int x, int y) {
        if (!template.check(map, transform, new Coords(x,y)))
            return false;
        // check the neighbors already placed fit with this template...
        if (x > 0) {
            Template tpl = map.getTemplate(x-1, y);
            TemplateTransform xfm = map.getTransform(x-1, y);
            if (!tpl.check(map, xfm, new Coords(x-1, y)))
                return false;
        }
        if (y > 0) {
            Template tpl = map.getTemplate(x, y-1);
            TemplateTransform xfm = map.getTransform(x, y-1);
            if (!tpl.check(map, xfm, new Coords(x, y-1)))
                return false;
        }
        if (x > 0 && y > 0) {
            Template tpl = map.getTemplate(x-1, y-1);
            TemplateTransform xfm = map.getTransform(x-1, y-1);
            if (!tpl.check(map, xfm, new Coords(x-1, y-1)))
                return false;
        }
        
        return true;
    }
    
    public void fill(TemplateMap map) throws RetryException {
        
        
        for (int x = 0; x < (map.getWidth()+2)/3; ++x)
        for (int y = 0; y < (map.getHeight()+2)/3; ++y) {
            int attempts = 0;
            while (++attempts < 20) {
                Template template = Template.values()[
                        rand.nextInt(Template.values().length)];
                TemplateTransform transform = new TemplateTransform(rand);
                map.put(x, y, template, transform);
                if (templateFits(map, template, transform, x, y)) {
                    break;
                }
            }
            if (attempts >= 20) throw new RetryException();
        }
    }
        
    
}
