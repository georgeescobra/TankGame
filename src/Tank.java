package src;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tank {
    private int x;
    private int y;
    private float vx;
    private float vy;
    private int angle;
    //helper variables to know which direction the tank is moving
    private int savex;
    private int savey;

    //for collision
    private Rectangle boundary;
    private Rectangle check;

    //size of tanks
    private int height = 16;
    private int width = 16;

    private final double R = 1;
    private final int rotationSpeed = 2;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;

    private WeaponUpgrade bulletStrength;
    private Shield shield;
    private boolean update;
    private BufferedImage projectile;
    private Bullet bullet;
    private Health healthOfTank;

    protected static ArrayList<Tank> numTanks = new ArrayList<>();
    private  ArrayList<Bullet> ammo = new ArrayList<>();

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        boundary = new Rectangle(x, y, height, width);
        boundary.setBounds(boundary.getBounds());
        healthOfTank = new Health(100);
        bulletStrength = new WeaponUpgrade(false);
        shield = new Shield(false);
        update = false;

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

    void toggleShootPressed() {this.ShootPressed = true;}

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

    void unToggleShootPressed() {this.ShootPressed = false;}

    public boolean shootPressed(){return this.ShootPressed;}



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
        if(this.angle < 0) {
            this.angle += 360;
            this.angle -= this.rotationSpeed;
        }else{
            this.angle -= this.rotationSpeed;
        }
    }

    private void rotateRight() {
        if(this.angle > 360) {
            this.angle -= 360;
            this.angle += this.rotationSpeed;
        }else{
            this.angle += this.rotationSpeed;
        }
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
        checkBorder(savex, savey, x, y);
    }




    private void checkBorder(int oldx, int oldy, int newx, int newy) {
        for(int i = 0; i < Map.mapA.size(); i++) {

            Map some = Map.mapA.get(i);
            check = new Rectangle(some.getWallBoundary());

            if(check.intersects(this.boundary)) {
                //checks if the object is a power up or not
                if (some.getKind() == 1 || some.getKind() == 2) {
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
                        if (some.getKind() == 4 && !this.shield.getStatus()) {
                            this.shield.setStatus(true);
                            this.img = some.getImage();
                            this.update = true;
                        }
                        if (some.getKind() == 3 && !this.bulletStrength.getStatus()) {
                            this.bulletStrength.setStatus(true);
                            this.img = some.getImage();
                            this.update = true;
                        }


                }
            }

                   //for tank collision
                    int num = numTanks.indexOf(this);
                    int other;
                    other = (num == 1) ? 0  : 1;
                    check = numTanks.get(other).getRectangle();
                    if (check.intersects(this.boundary)) {
                        Point topLeftTank = new Point(this.boundary.getLocation());
                        Point topRightTank = new Point((int) this.boundary.getX(), (int) this.boundary.getY() + 16);

                        //top of box moving forward colliding with bottom of TANK
                        //left of box moving to the left colliding with right of TANK

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

                        //left of box hitting right of TANK moving rightward
                        //want it to move backward to the left
                        if (!check.contains(topLeftTank) && oldy < newy) {
                            y = oldy;
                        }

                        //bottom of box hitting top of TNAK moving downward
                        //want it to go back up
                        if (!check.contains(topLeftTank) && oldx < newx) {
                            x = oldx;
                        }



            }



            }

            }
    public void shoot() {
        try{
            projectile = ImageIO.read(new File("resources/Bullet.png"));
            bullet = new Bullet(this.getX() + (this.getW() / 2), this.getY() + (this.getH() / 2), projectile, this);
            this.ammo.add(bullet);
        }catch(IOException e){
            System.out.println("***ERROR SHOOTING SHOT***");
        }
    }

    public void drawBullet(Graphics2D buffer, Graphics2D g2, BufferedImage world){
        for(int i = 0; i < ammo.size(); i++) {
            Bullet bull = ammo.get(i);
                bull.direction();
                double rotated = Math.toRadians(bull.getAngle());
                AffineTransform tx = AffineTransform.getRotateInstance(rotated, bull.getW() / 2, bull.getH() / 2);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                buffer.drawImage(op.filter(bull.getImg(), null), bull.getX(), bull.getY(), null);
                g2.drawImage(world, 0 ,0, null);

            ammo.remove(i);
        }

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
    public void setUpdate(boolean up){this.update = up;}
    public boolean getUpdate(){return this.update;}

    public boolean getWeaponUpgradeStatus(){
        return this.bulletStrength.getStatus();
    }

    public BufferedImage getImg(){
        return this.img;
    }

    public Shield getShieldObj(){
        return this.shield;
    }

    public void addTank(Tank p){
        numTanks.add(p);
    }

    public void updateHealth(int j){
        this.healthOfTank.updateHealth(j);
    }
    public int getHealth(){
        return this.healthOfTank.getHealth();
    }
    public BufferedImage getHealthBar(){return this.healthOfTank.getHealthBar();}
    public void setShieldStatus(boolean s){
        this.shield.setStatus(s);
    }

    public Rectangle getRectangle(){return this.boundary;}


    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


}
