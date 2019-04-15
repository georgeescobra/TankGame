package src;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet {
    private int Direction;
    private boolean PowerUp;
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int R = 7;
    private int dmgToOtherTank = 10;
    private Rectangle boundary;
    private boolean hitSomething;

    private int height = 20;
    private int width = 20;
    private BufferedImage img;

    public Bullet(int x, int y, BufferedImage bullet, Tank player){
        this.x = x;
        this.y = y;
        PowerUp = player.getWeaponUpgradeStatus();
        if(PowerUp){
            dmgToOtherTank = 20;
        }
        boundary = new Rectangle(x, y, height, width);
        boundary.setBounds(boundary.getBounds());
        this.img = bullet;
        hitSomething = false;
        this.angle = player.getAngle();

    }

    public int getDmgToOtherTank(){
        return this.dmgToOtherTank;
    }

    public void direction() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        x += vx;
        y += vy;

    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public BufferedImage getImg(){
        return this.img;
    }
    public boolean hasHitSomething(){
        return this.hitSomething;
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

}
