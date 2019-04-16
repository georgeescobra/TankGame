package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Health {
    private int health;
    private BufferedImage healthbar;
    public Health(int h){
        this.health = h;
        this.healthbar = setImage();
    }
    public int getHealth(){
        return this.health;
    }
    public void updateHealth(int dmg){
        this.health -= dmg;
        this.healthbar = setImage();
    }

    public BufferedImage setImage(){
        try {
            switch(this.health) {
                case 10:
                    this.healthbar = ImageIO.read(new File("resources/healthbar10.png"));
                    break;

                case 20:
                    this.healthbar = ImageIO.read(new File("resources/healthbar20.png"));
                    break;


                case 30:
                    this.healthbar = ImageIO.read(new File("resources/healthbar30.png"));
                    break;

                case 40:
                    this.healthbar = ImageIO.read(new File("resources/healthbar40.png"));
                    break;

                case 50:
                    this.healthbar = ImageIO.read(new File("resources/healthbar50.png"));
                    break;

                case 60:
                    this.healthbar = ImageIO.read(new File("resources/healthbar60.png"));
                    break;

                case 70:
                    this.healthbar = ImageIO.read(new File("resources/healthbar70.png"));
                    break;

                case 80:
                    this.healthbar = ImageIO.read(new File("resources/healthbar80.png"));
                    break;

                case 90:
                    healthbar = ImageIO.read(new File("resources/healthbar90.png"));
                    break;

                case 100:
                    this.healthbar = ImageIO.read(new File("resources/healthbar100.png"));
                    break;

                default:
                    this.healthbar = ImageIO.read(new File("resources/healthbar0.png"));
                    break;


            }

            }catch(IOException e){
                System.out.println("***CANNOT LOAD HEALTHBAR***");
            }

            return this.healthbar;

        }
        public BufferedImage getHealthBar(){
            return this.healthbar;
        }
    }


