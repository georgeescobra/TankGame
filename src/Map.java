package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Map extends JPanel{

    private BufferedReader mapFile;
    private static final String map1 = "src/newMap.txt";
    protected static ArrayList<Rectangle> mapA;

    BufferedImage obj;
    private int x_cord;
    private int y_cord;
    private int kind;
    private Rectangle boundary;

    public Map() throws IOException{
        this.mapFile = new BufferedReader(new FileReader(map1));
    }

    public Map(int x, int y, int type, BufferedImage img){
        this.x_cord = x;
        this.y_cord = y;
        this.kind = type;
        this.obj = img;
    }

    /*
    This function is to convert the txtFile into an ArrayList
     */
    public void generateMap(Graphics2D b){
        try {
            mapA = new ArrayList<>();
            int x = 0;
            int y = 0;
            for(int i = 0; i < 32; i ++){
               String temp;
                while((temp = mapFile.readLine()) != null) {
                   //System.out.println(temp);
                    for (int j = 0; j < temp.length(); j++) {
                        char c = temp.charAt(j);
                        //this is to draw the actual walls
                        loadMap(x, y, Character.getNumericValue(c), b);
                        x += 32;
                    }
                    //these are the x and y coordinates for the walls
                    //need to reset 0
                    y += 32;
                    x = 0;
                    System.out.println(mapA);
                }
            }


        }catch(IOException e) {
            System.out.println("***Unable to Parse Map\n" + e);
        }
    }

    public void loadMap(int x, int y, int type, Graphics2D b) {
        if(type == 3){
            try {
                //TODO: needs an indicator for tank and adds more damage and one shots breakable walls
                this.obj = ImageIO.read(new File("resources/PowerUp.png"));
                Map powerUp = new Map(x, y, type, this.obj);
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                b.drawImage(this.obj.getScaledInstance(32, 32, Image.SCALE_SMOOTH), x, y, null);
                b.draw(boundary);
            }catch(IOException e){
                System.out.println("***Unable to Generate Weapon PowerUp***");
            }
        }else if(type == 1){
            try {
                //TODO: need to make borders for this so the tank doesn't collide with it
                this.obj = ImageIO.read(new File("resources/Wall1.gif"));
                Map unbreakableWall = new Map(x, y, type, this.obj);
                b.drawImage(this.obj, x, y, null);
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                b.draw(boundary);
            }catch(IOException e){
                System.out.println("***Unable To Generate Unbreakable Wall***");
            }

        }else if(type == 2){
            try {
                //TODO: needs to make borders and also add health to this
                this.obj = ImageIO.read(new File("resources/Wall2.gif"));
                Map breakableWall = new Map(x, y, type, this.obj);
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                b.drawImage(this.obj, x, y, null);
                b.draw(boundary);
            }catch(IOException e){
                System.out.println("***Unable To Generate breakable Wall***");
            }
        }else if(type == 4){
            //TODO: Prolly make this smaller to fit around the tank when it walks over it
            try {
                this.obj = ImageIO.read(new File("resources/Shield.png"));
                Map shield = new Map(x, y, type, this.obj);
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                b.draw(boundary);
                b.drawImage(this.obj.getScaledInstance(32, 32, Image.SCALE_SMOOTH), x, y, null);
            }catch(IOException e){
                System.out.println("***Unable To Generate Shield PowerUp***");

            }
        }
        if(type != 0){
            mapA.add(boundary);

        }
    }

    public int getX(){
        return this.x_cord;
    }

    public int getY(){
        return this.y_cord;
    }

    public int getKind(){
        return this.kind;
    }

    public Rectangle getWallBoundary(){return this.boundary;}

    public BufferedImage getImg(){
        return this.obj;
    }


}