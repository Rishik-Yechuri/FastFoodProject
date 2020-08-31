import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.security.AccessController.getContext;

public class FastFoodProject {
    //declare variables for the UI
    static JFrame frame;
    static Canvas canvas;
    static BufferStrategy bufferStrategy;
    static BufferStrategy bufferStrategy2;
    //declare variables for the background thread
    static int tempNums = 0;
    static long previousTime = 0;
    static long currentTime = 0;
    static long previousTimeBack = 0;
    static long currentTimeBack = 0;
    static SandWichClass mainWich;
    static boolean keepRunning = true;
    static boolean isSpacePressed = false;
    static ArrayList<SpikeObjectClass> holdDangers;
    static ArrayList<SpikeObjectClass> onScreenDangers;
    static int frameStartLine = 0;

    public static void main(String[] args) {
        //set up stuff for the Canvas
        frame = new JFrame("Wich Jumper");
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(1000, 1000));
        panel.setLayout(null);
        canvas = new Canvas();
        //put a boundry to the square
        canvas.setBounds(0, 0, 1000, 1000);
        canvas.setIgnoreRepaint(true);
        panel.add(canvas);
        //set a key listener
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            //Check the key pressed and then call the methods
            public void keyPressed(KeyEvent evt) {
                processKey(evt);
            }

            public void keyReleased(KeyEvent evt) {
                processKeyRemoved(evt);
            }
        });
        //finish setting up canvas
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        bufferStrategy2 = canvas.getBufferStrategy();
        canvas.requestFocus();
        //Create SanwichClass
        mainWich = new SandWichClass();
        mainWich.moveWich(0, 964);
        //generate map
        holdDangers = new ArrayList<SpikeObjectClass>();
        for (int x = 0; x < 200; x++) {
            int temp = (Math.random() < 0.88) ? 1 : 2;
            if (temp == 1) {
                SpikeObjectClass spikeToAdd = new SpikeObjectClass(x * 50 + 13, 974);
                holdDangers.add(spikeToAdd);
                holdDangers.add(null);
                holdDangers.add(null);
                x = x + 2;
            } else {
                holdDangers.add(null);
            }
        }
        System.out.println("size " + holdDangers.size());
        //Create a background and foreground thread,then start them
        Thread back = new Thread(new BackgroundClass());
        Thread fore = new Thread(new Foregroundclass());
        back.start();
        fore.start();


    }

    public static class BackgroundClass implements Runnable {
        public void run() {
            while (keepRunning) {
                currentTimeBack = System.currentTimeMillis();
                long timePassed = currentTimeBack - previousTimeBack;
                if (timePassed >= 17) {
                    doBackgroundStuff();
                    previousTimeBack = currentTimeBack;
                } else {
                    try {
                        Thread.sleep(17 - timePassed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class Foregroundclass implements Runnable {
        public void run() {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            while (keepRunning) {
                try {
                    currentTime = System.currentTimeMillis();
                    long timePassed = currentTime - previousTime;
                    if (timePassed >= 17) {
                        clearScreen(g);
                        Paint();
                        //clearScreen(g);
                        previousTime = currentTime;
                    } else {
                        Thread.sleep(17 - timePassed);
                    }
                } catch (InterruptedException ex) {
                }
                //clearScreen(g);
            }
        }
    }

    public static void processKey(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            isSpacePressed = true;
        }
    }

    public static void processKeyRemoved(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            isSpacePressed = false;
        }
    }

    public static void Paint() throws InterruptedException {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        Paint(g);
        bufferStrategy.show();
    }

    public static void clearScreen(Graphics2D g) {
        g.clearRect(0, 0, 1000, 1000);
    }

    protected static void Paint(Graphics2D g) throws InterruptedException {
        Shape wich = new Rectangle(mainWich.xLocation, mainWich.yLocation, 64, 36);
        if (holdDangers != null) {
            for (int x = 0; x < holdDangers.size(); x++) {
                if (holdDangers.get(x) != null) {
                    SpikeObjectClass spike = holdDangers.get(x);
                    Shape spikeShape = spike.getShape();
                    g.draw(spikeShape);
                }
            }
        }
        g.draw(wich);
    }

    public static void doBackgroundStuff() {
        System.out.println("many" + tempNums);
        tempNums++;
        for (int x = 0; x < holdDangers.size(); x++) {
            if (holdDangers.get(x) != null) {
                holdDangers.get(x).move(holdDangers.get(x).xPosition - 4, holdDangers.get(x).yPosition);
                if (mainWich.wichCollided(holdDangers.get(x).getShape()) < 1) {
                    keepRunning = false;
                }
                if (holdDangers.get(x).xPosition < -30) {
                    holdDangers.remove(x);
                }
            }
        }
        frameStartLine += 4;
    }
}