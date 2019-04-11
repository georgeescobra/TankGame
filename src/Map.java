package src;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Map extends JPanel{

    private BufferedReader mapFile;
    private static final String map1 = "src/newMap.txt";
    private ArrayList<List<Integer>> base;
    private ArrayList<Integer> mapA;

    private Map unbreakableWall;
    BufferedImage wall;
    private Map breakableWall;
    private Game send = new Game();
    private int x_cord;
    private int y_cord;
    private int kind;

    public Map() throws IOException{
        this.mapFile = new BufferedReader(new FileReader(map1));
    }

    public Map(int x, int y, int type, BufferedImage img){
        this.x_cord = x;
        this.y_cord = y;
        this.kind = type;
        this.wall = img;
    }

    /*
    This function is to convert the txtFile into an ArrayList
     */
    public void generateMap(Graphics2D b){
        try {
            base = new ArrayList<>();
            int x = 0;
            int y = 0;
            for(int i = 0; i < 32; i ++){
               String temp;
                while((temp = mapFile.readLine()) != null) {
                    mapA = new ArrayList<>();
                   //System.out.println(temp);
                    for (int j = 0; j < temp.length(); j++) {
                        char c = temp.charAt(j);
                        mapA.add(Character.getNumericValue(c));
                        //this is to draw the actual walls
                        loadMap(x, y, Character.getNumericValue(c), b);
                        x += 32;
                    }
                    //these are the x and y coordinates for the walls
                    //need to reset 0
                    y += 32;
                    x = 0;
                    System.out.println(mapA);
                    base.add(mapA);
                }
            }

        }catch(IOException e) {
            System.out.println("***Unable to Parse Map\n" + e);
        }
    }

    public void loadMap(int x, int y, int type, Graphics2D b) {
        if(type == 0){

        }else if(type == 1){
            try {
                this.wall = ImageIO.read(new File("resources/Wall1.gif"));
                this.unbreakableWall = new Map(x, y, type, this.wall);
                b.drawImage(this.wall, x, y, null);
            }catch(IOException e){
                System.out.println("***Unable To Generate Unbreakable Wall***");
            }

        }else if(type == 2){
            try {
                this.wall = ImageIO.read(new File("resources/Wall2.gif"));
                this.breakableWall = new Map(x, y, type, this.wall);
                b.drawImage(this.wall, x, y, null);
            }catch(IOException e){
                System.out.println("***Unable To Generate breakable Wall***");
            }
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

    public BufferedImage getImg(){
        return this.wall;
    }


}