package src;

import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;



public class Game extends JPanel{

    public static final int screenWidth = 1280;
    public static final int screenHeight = 720;

    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;
    private Tank p1;
    private Tank p2;
    private Thread thread;
    protected boolean running = false;


    private void init(){
        try {
            running = true;

            this.jf = new JFrame("Tank Wars");


            this.world = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
            //Got this from stackoverflow https://stackoverflow.com/questions/10391778/create-a-bufferedimage-from-file-and-make-it-type-int-argb
           Graphics2D back = this.world.createGraphics();

            //image for the background
            //I didn't want to stretch the image so I just repeated it
            BufferedImage bg = ImageIO.read(new File("resources/Background.bmp"));
            for(int i = 0; i < screenWidth; i+=320) {
                for (int j = 0; j < screenHeight; j += 240) {
                    back.drawImage(bg, i, j, null);
                }
            }


            //this generates the map
            //not sure if I was supposed to pass the Graphics2D object
            //could not find another way to make it work.
            try {
                Map map1 = new Map();
                map1.generateMap(back);
            }catch(IOException e) {
                System.out.println("***Unable to Generate Map***\n" + e);
            }

            //loading the players
            try {
                BufferedImage tank1 = ImageIO.read(new File("resources/Tank1.gif"));
                p1 = new Tank(48, 48, 1, 1, 1, tank1);
                back.drawImage(tank1, 48, 48, null);
                BufferedImage tank2 = ImageIO.read(new File("resources/Tank2.gif"));
                p2 = new Tank(1216, 688, 1, 1, 1, tank2);
                back.drawImage(tank1, 1216, 688, null);
            }catch(IOException e){
                System.out.println("***Unable to Load Players***");
            }

            //this sets the frame
            this.jf.setLayout(new BorderLayout());
            this.jf.add(this);

            TankControl tankC1 = new TankControl(p2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
            TankControl tankC2 = new TankControl(p1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

            this.jf.addKeyListener(tankC1);
            this.jf.addKeyListener(tankC2);
            this.jf.setSize(Game.screenWidth, Game.screenHeight);
            this.jf.setResizable(false);
            jf.setLocationRelativeTo(null);
            this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.jf.setVisible(true);

        }catch(IOException e){
            System.out.println("***Unable to Generate Game****\n" + e);
        }


    }

    public synchronized void start(){
        thread = new Thread(this.thread);
        thread.start();
        running = true;
    }

    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){

        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);

        this.p1.drawImage(buffer);
        this.p2.drawImage(buffer);
        g2.drawImage(world,0,0,null);

    }


    public static void main(String[] args) {
        Game newGame = new Game();
        newGame.init();

           try{
               while(newGame.running){
                   newGame.start();
                   newGame.p1.update();
                   newGame.p2.update();
                   newGame.repaint();
                   System.out.println(newGame.p1);
                   System.out.println(newGame.p2);
                   Thread.sleep(1000/144);
               }

           }catch(InterruptedException ignored){

           }


    }

}