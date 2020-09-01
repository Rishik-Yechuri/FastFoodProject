import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
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
    static long previousTime = 0;
    static long currentTime = 0;
    static long previousTimeBack = 0;
    static long currentTimeBack = 0;
    static SandWichClass mainWich;
    static boolean keepRunning = true;
    static boolean[] interactionInfo;
    static int jumpHeight = 0;
    static boolean isEmpty = false;
    static ArrayList<SpikeObjectClass> holdDangers;

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
        mainWich.moveWich(30, 955);
        //generate map
        holdDangers = new ArrayList<SpikeObjectClass>();
        /*holdDangers.add(null);
        holdDangers.add(null);*/
        for (int x = 3; x < 200; x++) {
            int temp = (Math.random() < 0.88) ? 1 : 2;
            if (temp == 1) {
                holdDangers.add(null);
                //x = x + 2;
            } else {
                SpikeObjectClass spikeToAdd = new SpikeObjectClass(x * 50 + 13, 974);
                holdDangers.add(spikeToAdd);
                holdDangers.add(null);
                holdDangers.add(null);
                x = x + 2;
            }
        }
        interactionInfo = new boolean[2];
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
                    if (timePassed >= 1) {
                        clearScreen(g);
                        Paint();
                        //clearScreen(g);
                        previousTime = currentTime;
                    } else {
                        Thread.sleep(1 - timePassed);
                    }
                } catch (InterruptedException ex) {
                }
                //clearScreen(g);
            }
        }
    }

    public static void processKey(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            interactionInfo[0] = true;
        }
    }

    public static void processKeyRemoved(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            interactionInfo[0] = false;
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
        Shape wich = new Rectangle(mainWich.xLocation, mainWich.yLocation, 80, 45);
        if (holdDangers != null) {
            for (int x = 0; x < holdDangers.size(); x++) {
                if (holdDangers.get(x) != null) {
                    SpikeObjectClass spike = holdDangers.get(x);
                    Polygon spikeShape = spike.getShape();
                    g.setColor(Color.BLACK);
                    g.fill(spikeShape);
                    // g.setColor(Color.YELLOW);
                    System.out.println("paint called draw:" + Arrays.toString(spikeShape.xpoints) + " y: " + Arrays.toString(spikeShape.ypoints));
                }
            }
        }
        Color color = new Color(168,141,17);
        g.setColor(color);
        g.fill(wich);
    }

    public static void doBackgroundStuff() {
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
        ArrayList<SpikeObjectClass> tempList = holdDangers;
        tempList.remove(null);
        if(tempList.size() == 0){
            keepRunning = false;
            frame.setTitle("You Won");
        }
        if(interactionInfo[0]){
            if(jumpHeight == 0){
                interactionInfo[1] = true;
                jumpHeight+=5;
            }else if(jumpHeight > 0 && jumpHeight < 120 && interactionInfo[1]){
                jumpHeight+=5;
            }else if(jumpHeight == 120){
                interactionInfo[1] = false;
                jumpHeight-=5;
            }else if(jumpHeight > 0 && !interactionInfo[1]){
                jumpHeight-=5;
            }
        }else if(jumpHeight > 0){
            if(jumpHeight > 0 && jumpHeight < 120 && interactionInfo[1]){
                jumpHeight+=5;
            }else if(jumpHeight == 120){
                interactionInfo[1] = false;
                jumpHeight-=5;
            }else if(jumpHeight > 0 && !interactionInfo[1]){
                jumpHeight-=5;
            }
        }
        mainWich.moveWich(mainWich.xLocation,955 - jumpHeight);
    }
}