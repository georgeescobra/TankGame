package src;

import java.awt.geom.AffineTransform;
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
    BufferedImage tank1;
    BufferedImage tank2;
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
                this.tank1 = ImageIO.read(new File("resources/Tank1.png"));
                p1 = new Tank(40, 40, 1, 1, 1, this.tank1);

                tank2 = ImageIO.read(new File("resources/Tank2.png"));
                this.p2 = new Tank(1226, 648, 1, 1, 180, this.tank2);

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

//        this.p1.drawImage(buffer);
//        this.p2.drawImage(buffer);
        g2.drawImage(world,0,0,null);

        //this got rid of trail
        //gotta learn how to do this with multiple paintComponents idk if this is correct way of doing it
        AffineTransform rotation = AffineTransform.getTranslateInstance(p1.getX(), p1.getY());
        rotation.rotate(Math.toRadians(p1.getAngle()), p1.getH() / 2.0, p1.getW() / 2.0);


        AffineTransform rotation2 = AffineTransform.getTranslateInstance(p2.getX(), p2.getY());
        rotation2.rotate(Math.toRadians(p2.getAngle()), p2.getH() / 2.0, p2.getW() / 2.0);
        g2.drawImage(this.tank1.getScaledInstance(16, 16, Image.SCALE_SMOOTH), rotation, null);
        g2.drawImage(this.tank2.getScaledInstance(16, 16, Image.SCALE_SMOOTH), rotation2, null);


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