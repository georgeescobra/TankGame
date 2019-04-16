package src;

public class Health {
    private int health;
    public Health(int h){
        this.health = h;
    }
    public int getHealth(){
        return this.health;
    }
    public void updateHealth(int dmg){
        this.health -= dmg;
    }
}
