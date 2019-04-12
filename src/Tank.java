package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

public class Tank {
    //make tanks start at opposite corners of the map [1][1] [21][39]
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private Rectangle boundary;

    //size of tanks
    private int height = 16;
    private int width = 16;

    private final int R = 2;
    private final int rotationSpeed = 4;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        //this is 18 so when the tank rotates it is still within the rectangle
        //got this from using pythagorean theorem on the tank
        boundary = new Rectangle(x, y, height, width);

    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }



    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }


    }

    private void rotateLeft() {
        this.angle -= this.rotationSpeed;
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        this.boundary.setLocation(x, y);
        //System.out.println(boundary.toString());
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        this.boundary.setLocation(x, y);
        //System.out.println(boundary.toString());
        checkBorder();
    }




    private void checkBorder() {
        if(this.getRectangle().isEmpty()){
            if (x < 32) {
                x = 32;
            }
            if (x > Game.screenWidth - 48) {
                x = Game.screenWidth - 48;
            }
            if (y < 32) {
                y = 32;
            }
            if (y > Game.screenHeight - 64) {
                y = Game.screenHeight - 64;
            }

        }
        if(!this.getRectangle().isEmpty())
            System.out.print("COLLIDE");


    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getAngle(){
        return this.angle;
    }
    public int getH(){
        return this.height;
    }
    public int getW(){
        return this.width;
    }
    public Rectangle getRectangle(){return this.boundary;}
    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

//    void drawImage(Graphics g) {
//        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
//        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawImage(this.img.getScaledInstance(16, 16, Image.SCALE_SMOOTH), rotation, null);
//    }

}
