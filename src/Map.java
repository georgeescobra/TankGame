package src;

import java.io.*;
import java.util.ArrayList;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Map extends JPanel{

    private BufferedReader mapFile;
    private static final String map1 = "src/newMap.txt";
    protected static ArrayList<Map> mapA;
    private Map collidable;

    BufferedImage obj;
    private int x_cord;
    private int y_cord;
    private int kind;
    private Rectangle boundary;
    private BufferedImage image;
    private Graphics2D use;

    public Map() throws IOException{
        this.mapFile = new BufferedReader(new FileReader(map1));
    }

    public Map(int x, int y, int type, Rectangle bound, BufferedImage img){
        this.x_cord = x;
        this.y_cord = y;
        this.kind = type;
        this.boundary = bound;
        this.image = img;

    }

    /*
    This function is to convert the txtFile into an ArrayList
     */
    public void generateMap(Graphics2D b){
        try {
            use = b;
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
                this.obj = ImageIO.read(new File("resources/PowerUp.png"));
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                b.drawImage(this.obj.getScaledInstance(32, 32, Image.SCALE_SMOOTH), x, y, null);
                collidable = new Map(x, y, type, boundary, this.obj);
            }catch(IOException e){
                System.out.println("***Unable to Generate Weapon PowerUp***");
            }
        }else if(type == 1){
            try {
                this.obj = ImageIO.read(new File("resources/Wall1.gif"));
                b.drawImage(this.obj, x, y, null);
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                collidable = new Map(x, y, type, boundary, this.obj);
            }catch(IOException e){
                System.out.println("***Unable To Generate Unbreakable Wall***");
            }

        }else if(type == 2){
            try {
                this.obj = ImageIO.read(new File("resources/Wall2.gif"));
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                b.drawImage(this.obj, x, y, null);
                collidable = new Map(x, y, type, boundary, this.obj);
            }catch(IOException e){
                System.out.println("***Unable To Generate breakable Wall***");
            }
        }else if(type == 4){
            try {
                this.obj = ImageIO.read(new File("resources/Shield.png"));
                boundary = new Rectangle(x, y, 32, 32);
                boundary.setBounds(boundary);
                collidable = new Map(x, y, type, boundary, this.obj);
                b.drawImage(this.obj.getScaledInstance(32, 32, Image.SCALE_SMOOTH), x, y, null);
            }catch(IOException e){
                System.out.println("***Unable To Generate Shield PowerUp***");

            }
        }

        if(type != 0){
            mapA.add(collidable);

        }
    }

    public void updateMap(Tank player, Graphics2D buffer, Graphics2D uni, BufferedImage world ){
        for(int i = 0; i < Map.mapA.size(); i++) {
            if(player.getImg().equals(Map.mapA.get(i).getImage())){
                Map.mapA.remove(i);
            }
        }
        try {
            BufferedImage bg = ImageIO.read(new File("resources/Background.bmp"));
            for (int i = 0; i < Game.screenWidth; i += 320) {
                for (int j = 0; j < Game.screenHeight; j += 240) {
                    buffer.drawImage(bg, i, j, null);
                }
            }
        }catch(IOException e){
            System.out.println("***Can't regenerate Map***");
        }

        uni.drawImage(world, 0 ,0, null );

        for(int j = 0; j < Map.mapA.size(); j++){
            if(Map.mapA.get(j).getKind() != 0){
                if(Map.mapA.get(j).getKind() == 3 || Map.mapA.get(j).getKind() == 4){
                    buffer.drawImage(Map.mapA.get(j).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH), Map.mapA.get(j).getX(), Map.mapA.get(j).getY(), null);
                }else {
                    buffer.drawImage(Map.mapA.get(j).getImage(), Map.mapA.get(j).getX(), Map.mapA.get(j).getY(), null);
                }
            }
        }
        uni.drawImage(world, 0 ,0, null );
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

    public BufferedImage getImage(){return this.image;}


}