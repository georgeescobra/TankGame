package src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Shield extends PowerUp {
    private boolean status;
    private BufferedImage temp;

    public Shield(boolean stat){
        this.status = false;
    }
    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public boolean setStatus(boolean toSet) {
        return this.status = toSet;
    }

    public BufferedImage getShieldImg(){

        try {temp = ImageIO.read(new File("resources/Shield.png"));}catch(IOException e){}
        return temp;
    }
}