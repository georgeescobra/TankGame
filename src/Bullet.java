package src;

public class Bullet {
    private int Direction;
    private boolean PowerUp;
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int R = 2;
    private int dmgToOtherTank = 10;

    public Bullet(int x, int y, Tank player){
        this.x = x;
        this.y = y;
        PowerUp = player.getWeaponUpgradeStatus();
        if(PowerUp){
            dmgToOtherTank = 20;
        }

    }

    public int getDmgToOtherTank(){
        return this.dmgToOtherTank;
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        x += vx;
        y += vy;

    }
    private void moveBackwards(){
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        x -= vx;
        y -= vy;
    }
}
