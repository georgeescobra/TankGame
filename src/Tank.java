package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.geom.RectangularShape;
import java.awt.geom.Dimension2D;
import java.awt.Rectangle;
import java.io.IOException;

public class Tank {
    //make tanks start at opposite corners of the map [1][1] [21][39]
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int savex;
    private int savey;
    private Rectangle boundary;
    private Rectangle check;

    //size of tanks
    private int height = 16;
    private int width = 16;

    private final double R = 1.5;
    private final int rotationSpeed = 2;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private int health;
    private WeaponUpgrade bulletStrength;
    private Shield shield;

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
        boundary.setBounds(boundary.getBounds());
        health = 100;
        bulletStrength = new WeaponUpgrade(false);
        shield = new Shield(false);

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

        savex = x;
        savey = y;

        x -= vx;
        y -= vy;
        this.boundary.setLocation(x, y);
        this.boundary.setBounds(boundary.getBounds());

        //System.out.println(boundary.toString());
        checkBorder(savex, savey, x, y);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        savex = x;
        savey = y;

        x += vx;
        y += vy;

        this.boundary.setLocation(x, y);
        this.boundary.setBounds(boundary.getBounds());
        //System.out.println(boundary.toString());
        checkBorder(savex, savey, x, y);
    }




    private void checkBorder(int oldx, int oldy, int newx, int newy) {
        for(int i = 0; i < Map.mapA.size(); i++) {

            Map some = Map.mapA.get(i);
            check = new Rectangle(some.getWallBoundary());

            if(check.intersects(this.boundary)) {
                //checks if the object is a power up or not
                if (some.getKind() == 1 || some.getKind() == 2) {
                    //System.out.println("COLLIDING WITH OBJECT!! COLLIDING WITH OBJECT!! \n");
                    Point topLeftTank = new Point(this.boundary.getLocation());
                    Point topRightTank = new Point((int) this.boundary.getX(), (int) this.boundary.getY() + 16);

                    //top of box moving forward colliding with bottom of wall
                    //left of box moving to the left colliding with right of wall
                    if (check.contains(topLeftTank) || check.contains(topRightTank)) {
                        //have to check if it is moving upward or leftward
                        //upward: I want it to go back down
                        if (newx < oldx) {
                            x = oldx;
                        }
                        //leftward: I want it to move to the right
                        if (newy < oldy) {
                            y = oldy;
                        }

                    }

                    //left of box hitting right of wall moving rightward
                    //want it to move backward to the left
                    if (!check.contains(topLeftTank) && oldy < newy) {
                        y = oldy;
                    }

                    //bottom of box hitting top of wall moving downward
                    //want it to go back up
                    if (!check.contains(topLeftTank) && oldx < newx) {
                        x = oldx;
                    }

                }
                if (some.getKind() == 3 || some.getKind() == 4) {
                    //activating powerups
                    if (!this.shield.getStatus() || !this.bulletStrength.getStatus()) {
                        if (some.getKind() == 4) {
                            System.out.println("HSDHDFKJSKJFHSKJD");
                            this.shield.setStatus(true);
                            this.img = some.getImage();
                        }
                        if (some.getKind() == 3) {
                            this.bulletStrength.setStatus(true);
                        }
                    }

                }
            }



            }

//            if (x < 32) {
//                x = 32;
//            }
//            if (x > Game.screenWidth - 48) {
//                x = Game.screenWidth - 48;
//            }
//            if (y < 32) {
//                y = 32;
//            }
//            if (y > Game.screenHeight - 64) {
//                y = Game.screenHeight - 64;
//            }

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
    public boolean getShieldStatus(){
       return this.shield.getStatus();
    }

    public boolean getWeaponUpgradeStatus(){
        return this.bulletStrength.getStatus();
    }

    public BufferedImage getImg(){
        return this.img;
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
