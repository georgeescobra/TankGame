package src;

import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Camera {
    private int x;
    private int y;
    private Graphics2D use;

    public Camera(Tank player, Graphics2D b){
        this.x = player.getX();
        this.y = player.getY();
        this.use = b;
    }


    public int getX(){ return this.x; }
    public int getY(){return this.y;}
    public void setX(int x){ this.x = x;}
    public void setY(int y){this.y = y;}

}
