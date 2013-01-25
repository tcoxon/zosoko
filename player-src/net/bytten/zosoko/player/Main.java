package net.bytten.zosoko.player;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.generator.IPuzzleGenerator;
import net.bytten.zosoko.generator.PuzzleGenerator;


public class Main extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;

    protected BufferedImage buffer;
    protected Graphics2D bufferG;
    protected Dimension bufferDim;
    
    protected IPuzzleGenerator puzzleGen;
    protected PlayingPuzzle puzzle;
    protected PuzzleController controller;
    protected Iterator<IPuzzle> puzzleIter;
    protected PuzzleRenderer puzzleRenderer;
    
    protected Thread generatorThread;
    protected Timer repaintTimer;
    
    protected String[] args;
    
    public Main(String[] args) {
        super();
        this.args = args;
        regenerate(getSeed(args));
        puzzleRenderer = new PuzzleRenderer();
        controller = new PuzzleController();
        
        repaintTimer = new Timer();
        repaintTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, 200);
    }
    
    protected IPuzzleGenerator makePuzzleGenerator(long seed) {
        // control generator with getArg()
        int width = getIntArg("width", 4), //2),
            height = getIntArg("height", 4), //1),
            boxes = getIntArg("boxes", 2);
        System.out.println("Puzzle seed: "+seed);
        return new PuzzleGenerator(
                new Random(seed),
                width, height, boxes,
                getArg("unbounded") == null);
        //return new TestPuzzleGenerator(getArg("unbounded") == null);
    }
    
    public void regenerate(final long seed) {
        if (generatorThread == null) {
            generatorThread = new Thread() {
                public void run() {
                    puzzleGen = makePuzzleGenerator(seed);
                    if (puzzleGen != null) {
                        puzzleGen.generate();
                        puzzleIter = puzzleGen.getPuzzleSet().iterator();
                        nextPuzzle();
                    }
                    generatorThread = null;
                }
            };
            generatorThread.start();
        }
    }
    
    @Override
    public void paint(Graphics g) {
        fixBuffer(g);
        
        bufferG.setColor(Color.WHITE);
        bufferG.fillRect(0, 0, bufferDim.width, bufferDim.height);
        
        if (puzzle != null) {
            puzzleRenderer.draw(bufferG, bufferDim, puzzle);
        }
        
        // Double-buffered drawing
        g.drawImage(buffer, 0, 0, this);
    }

    private void fixBuffer(Graphics g) {
        // If the size of the frame has changed, recreate the buffer
        if (!getSize().equals(bufferDim)) {
            bufferDim = new Dimension(getSize());
            buffer = new BufferedImage(bufferDim.width, bufferDim.height,
                    BufferedImage.TYPE_INT_ARGB);
            bufferG = buffer.createGraphics();
        }
    }


    @Override
    public void update(Graphics g) {
        // Call repaint directly to avoid "flashing"
        repaint();
    }
    
    public void nextPuzzle() {
        if (puzzleIter != null && puzzleIter.hasNext()) {
            puzzle = new PlayingPuzzle(puzzleIter.next());
            controller.setPuzzle(puzzle);
        } else {
            regenerate(new Random().nextLong());
        }
    }
    

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            nextPuzzle();
        } else {
            controller.key(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // main -------------------------------------------------------------------
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Zōsōko player");
        final Main panel = new Main(args);
        panel.setPreferredSize(new Dimension(640, 480));
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        frame.addKeyListener(panel);
        
        frame.setVisible(true);
    }
    
    private static long parseSeed(String seed) {
        try {
            return Long.parseLong(seed);
        } catch (NumberFormatException ex) {
            return seed.hashCode();
        }
    }
    
    private String getArg(String arg) {
        return getArg(arg, args);
    }
    
    private static String getArg(String arg, String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-"+arg)) {
                return args[i];
            } else if (args[i].startsWith("-"+arg+"=")) {
                return args[i].substring(2 + arg.length());
            }
        }
        return null;
    }
    
    private int getIntArg(String arg, int defaultVal) {
        return getIntArg(arg, defaultVal, args);
    }
    
    private static int getIntArg(String arg, int defaultVal, String[] args) {
        String strval = getArg(arg, args);
        if (strval == null) return defaultVal;
        try {
            return Integer.parseInt(strval);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    private static long getSeed(String[] args) {
        String val = getArg("seed", args);
        
        if (val == null) return new Random().nextLong();
        return parseSeed(val);
    }
}
