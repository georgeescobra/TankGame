package src;

import java.awt.geom.AffineTransform;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Rectangle;
import java.nio.Buffer;
import javax.imageio.ImageIO;



public class Game extends JPanel{

    public static final int screenWidth = 1280;
    public static final int screenHeight = 720;

    private BufferedImage world;
    private BufferedImage lastWorld;
    public static Game newGame;
    private BufferedImage p2Cam;
    private BufferedImage p1Cam;
    protected Bullet bull;
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



            //this sets the frame
            this.jf.setLayout(new BorderLayout());
            this.jf.add(this);

            //loading the players
            try {

                this.tank1 = ImageIO.read(new File("resources/Tank1.png"));
                p1 = new Tank(40, 40, 0, 0, 0, this.tank1);
                p1.addTank(p1);

                tank2 = ImageIO.read(new File("resources/Tank2.png"));
                p2 = new Tank(1226, 648, 0, 0, 180, this.tank2);
                p2.addTank(p2);

            }catch(IOException e){
                System.out.println("***Unable to Load Players***");
            }


            this.lastWorld = this.world;
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
        //Creates a Graphics2D, which can be used to draw into this BufferedImage.
       // this.world = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        buffer = world.createGraphics();
        super.paintComponent(g2);


       // g2.drawImage(this.world,0,0,null);



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
                this.lastWorld = this.world;
                map1.updateMap(this.p1, buffer);
                this.p1.setUpdate(false);
            }
            g2.drawImage(this.lastWorld, 0 ,0 ,null);
            buffer.drawImage(this.p1.getShieldObj().getShieldImg().getScaledInstance(30, 30, Image.SCALE_SMOOTH), p1.getX() - p1.getH() / 2, p1.getY() - p1.getW() / 2, null);
        }

        if (this.p2.getShieldStatus()) {
            if(this.p2.getUpdate()) {
                this.lastWorld = this.world;
                map1.updateMap(this.p2, buffer);
                this.p2.setUpdate(false);
            }
            buffer.drawImage(this.p2.getShieldObj().getShieldImg().getScaledInstance(30, 30, Image.SCALE_SMOOTH), p2.getX() - p2.getH() / 2, p2.getY() - p2.getW() / 2, null);
        }

        if (this.p1.getWeaponUpgradeStatus()){
            if(this.p1.getUpdate()) {
                map1.updateMap(this.p1, buffer);
                this.lastWorld = this.world;
                this.p1.setUpdate(false);
            }
        }

        if (this.p2.getWeaponUpgradeStatus()){
            if(this.p2.getUpdate()) {
                map1.updateMap(this.p2, buffer);
                this.lastWorld = this.world;
                this.p2.setUpdate(false);
            }
        }

        if(p1.shootPressed()){
            p1.shoot();
            p1.drawBullet(buffer, g2, this.world);
            map1.updateMap(p1, buffer);
            this.lastWorld = this.world;
            p1.unToggleShootPressed();

        }

        if(p2.shootPressed()){
            p2.shoot();
            p2.drawBullet(buffer, g2, this.world);
            map1.updateMap(p2, buffer);
            this.lastWorld = this.world;
            p2.unToggleShootPressed();

        }


        buffer.drawImage(this.tank1.getScaledInstance( p1.getW() , p1.getH(), Image.SCALE_SMOOTH), rotation, null);
        buffer.drawImage(this.tank2.getScaledInstance( p2.getW() , p2.getH(), Image.SCALE_SMOOTH), rotation2, null);

        buffer.drawImage(p1.getHealthBar().getScaledInstance(p1.getW(), 20, Image.SCALE_SMOOTH), p1.getX(), p1.getY() + 6, null);
        buffer.drawImage(p2.getHealthBar().getScaledInstance(p2.getW(), 20, Image.SCALE_SMOOTH), p2.getX(), p2.getY() + 6, null);


        if(p1.getHealth() <= 0 || p2.getHealth() <= 0) {
            //show the game over screen
            try {
                BufferedImage over = ImageIO.read(new File("resources/game.png"));
                buffer.drawImage(over, 0, 0, null);
                g2.drawImage(this.world, 0, 0, null);
                return;
            } catch (IOException E) {

            }
        }


//        buffer.setColor(Color.blue);
//        buffer.drawRect(p1.getX(), p1.getY(), p1.getW() , p1.getH());
//
//        buffer.setColor(Color.RED);
//        buffer.drawRect(p2.getX(), p2.getY(), p2.getW() , p2.getH());
        g2.drawImage(this.world,0,0,null);
        BufferedImage minimap = this.world.getSubimage(0, 0, screenWidth, screenHeight);
        g2.scale(.2, .2);
        g2.drawImage(minimap, 2650,  2800, null);

    }


    public static void main(String[] args) {
        newGame = new Game();
        newGame.init();

        try{
            while(newGame.running){
                newGame.start();
                newGame.p1.update();
                newGame.p2.update();
                newGame.repaint();
                if(newGame.p1.getHealth() <= 0 || newGame.p2.getHealth() <= 0){
                    break;
                }
                System.out.println(newGame.p1);
                System.out.println(newGame.p2);
                Thread.sleep(1000/144);
            }
            System.out.println("GAAAAAAME OOOOOVERRR");


        }catch(InterruptedException ignored){

        }


    }

}