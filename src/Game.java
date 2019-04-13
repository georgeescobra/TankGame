package src;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.awt.Rectangle;
import javax.imageio.ImageIO;



public class Game extends JPanel{

    public static final int screenWidth = 1280;
    public static final int screenHeight = 704;

    private static BufferedImage world;
    private BufferedImage p2Cam;
    private BufferedImage p1Cam;
    private JFrame jf;
    private Tank p1;
    private Tank p2;
    private BufferedImage tank1;
    private BufferedImage tank2;
    private BufferedImage miniMap;
    private Thread thread;
    protected boolean running = false;
    private Rectangle followP1;
    private Map map1;
    private Graphics2D buffer;

    private JPanel panel1;
    private JPanel panel2;

    private void init(){
        try {
            running = true;

            this.jf = new JFrame("Tank Wars");



            this.world = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);


            //Got this from stackoverflow https://stackoverflow.com/questions/10391778/create-a-bufferedimage-from-file-and-make-it-type-int-argb
            buffer = this.world.createGraphics();


            //image for the background
            //I didn't want to stretch the image so I just repeated it
            BufferedImage bg = ImageIO.read(new File("resources/Background.bmp"));
            for(int i = 0; i < screenWidth; i+=320) {
                for (int j = 0; j < screenHeight; j += 240) {
                    buffer.drawImage(bg, i, j, null);
                }
            }


            //this generates the map
            //not sure if I was supposed to pass the Graphics2D object
            //could not find another way to make it work.
            try {
                map1 = new Map();
                map1.generateMap(buffer);
            }catch(IOException e) {
                System.out.println("***Unable to Generate Map***\n" + e);
            }

//            this.panel1 = new JPanel();
//            panel1.setLayout(new GridBagLayout());
//            Graphics2D camera1 = this.world.createGraphics();
//            p1Cam = this.world.getSubimage(0, 0, screenWidth/3, 300);
//            camera1.drawImage(p1Cam, 0 , 0, null);
//            this.jf.add(panel1);


            //this sets the frame
            this.jf.setLayout(new BorderLayout());
            this.jf.add(this);

            //loading the players
            try {
                this.tank1 = ImageIO.read(new File("resources/Tank1.png"));
                p1 = new Tank(40, 40, 1, 1, 1, this.tank1);

                // p1Cam = this.world.getSubimage(followP1);

                tank2 = ImageIO.read(new File("resources/Tank2.png"));
                p2 = new Tank(1226, 648, 1, 1, 180, this.tank2);
                //p1Cam = this.world.getSubimage(, , screenWidth/2, 450);

            }catch(IOException e){
                System.out.println("***Unable to Load Players***");
            }



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
        //buffer = world.createGraphics();
        super.paintComponent(g2);


        g2.drawImage(this.world,0,0,null);


        //g2.setColor(Color.YELLOW);
       // g2.drawImage(p2Cam, 0, 0, null);
        //g2.setColor(Color.BLUE);
//        g2.scale(1.5, 1.5);
//        g2.drawImage(this.p1Cam, 0, 0, null);



        //this got rid of trail
        //gotta learn how to do this with multiple paintComponents idk if this is correct way of doing it
        //rotates the tank
        AffineTransform rotation = AffineTransform.getTranslateInstance(p1.getX(), p1.getY());
        rotation.rotate(Math.toRadians(p1.getAngle()), p1.getH() / 2.0, p1.getW() / 2.0);
        AffineTransform rotation2 = AffineTransform.getTranslateInstance(p2.getX(), p2.getY());
        rotation2.rotate(Math.toRadians(p2.getAngle()), p2.getH() / 2.0, p2.getW() / 2.0);


        if (this.p1.getShieldStatus()) {
            if(this.p1.getUpdate()) {
                map1.updateMap(this.p1, buffer, g2, this.world);
                this.p1.setUpdate(false);
            }
            g2.drawImage(this.p1.getShieldObj().getShieldImg().getScaledInstance(30, 30, Image.SCALE_SMOOTH), p1.getX() - p1.getH() / 2, p1.getY() - p1.getW() / 2, null);
        }

        if (this.p2.getShieldStatus()) {
            if(this.p2.getUpdate()) {
                map1.updateMap(this.p2, buffer, g2, this.world);
                this.p2.setUpdate(false);
            }
            g2.drawImage(this.p2.getShieldObj().getShieldImg().getScaledInstance(30, 30, Image.SCALE_SMOOTH), p2.getX() - p2.getH() / 2, p2.getY() - p2.getW() / 2, null);
        }

        if (this.p1.getWeaponUpgradeStatus()){
            if(this.p1.getUpdate()) {
                map1.updateMap(this.p1, buffer, g2, this.world);
                this.p1.setUpdate(false);
            }
        }

        if (this.p2.getWeaponUpgradeStatus()){
            if(this.p2.getUpdate()) {
                map1.updateMap(this.p2, buffer, g2, this.world);
                this.p2.setUpdate(false);
            }
        }


        g2.drawImage(this.tank1.getScaledInstance( p1.getW() , p1.getH(), Image.SCALE_SMOOTH), rotation, null);
        g2.drawImage(this.tank2.getScaledInstance( p2.getW() , p2.getH(), Image.SCALE_SMOOTH), rotation2, null);


        g2.setColor(Color.blue);
        g2.drawRect(p1.getX(), p1.getY(), p1.getW() , p1.getH());

        g2.setColor(Color.RED);
        g2.drawRect(p2.getX(), p2.getY(), p2.getW() , p2.getH());


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