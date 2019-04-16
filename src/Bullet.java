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
    private Rectangle check;
    private boolean hitWall;
    private boolean hitTank;
    private boolean hitSomething;
    private Tank belonging;

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
        this.hitSomething = false;
        this.angle = player.getAngle();
        this.belonging = player;

    }

    public int getDmgToOtherTank(){
        return this.dmgToOtherTank;
    }

    public void direction() {
        if(this.angle > 360) {
            this.angle -= 360;}
        if(this.angle < 0) {
            this.angle += 360;}
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        if(angle >= 0 && angle <= 90) {
            x += vx;
            y += vy;
        }
        if(angle > 90 && angle <= 180){
            x += vx;
            y -= vy;
        }
        if(angle > 180 && angle <= 270){
            x -= vx;
            y-= vy;
        }
        if(angle > 270 && angle <= 360){
            x -= vx;
            y += vy;
        }
        System.out.println("BULLET \n" + "x = " + this.x + " y = " + this.y + " angle = " + this.angle);

    }
    public void hasHit(){
        for(int i = 0; i < Map.mapA.size(); i++){
            Map some = Map.mapA.get(i);
            check = new Rectangle(some.getWallBoundary());
            if(check.intersects(this.boundary)){
                System.out.println("HIT");
               //this is if the bullet hits an breakable wall
                if(some.getKind() == 2){
                    Map.mapA.remove(i);
                    this.hitWall = true;
                    this.hitSomething = true;
                    return;
                }

            }
        }
        for(int i = 0; i < Tank.numTanks.size(); i++){
            int num = Tank.numTanks.indexOf(this.belonging);
            int other;
            other = (num == 1) ? 0  : 1;
            Tank otherTank = Tank.numTanks.get(other);
            check = otherTank.getRectangle();
            if(check.intersects(this.boundary)){
                //update health of tank
                    otherTank.updateHealth(this.dmgToOtherTank);
                this.hitTank = true;
                this.hitSomething = true;
                return;

            }
        }
        this.hitSomething = true;

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
    public boolean gethasHit(){
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
