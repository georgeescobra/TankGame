package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.geom.RectangularShape;
import java.awt.geom.Dimension2D;
import java.awt.Rectangle;


public class Tank {
    //make tanks start at opposite corners of the map [1][1] [21][39]
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int savex;
    private int savey;
    private Rectangle boundary;
    private Rectangle check;
    private Rectangle intersect;

    //size of tanks
    private int height = 16;
    private int width = 16;

    private final int R = 2;
    private final int rotationSpeed = 2;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        //this is 18 so when the tank rotates it is still within the rectangle
        //got this from using pythagorean theorem on the tank
        boundary = new Rectangle(x, y, height, width);
        boundary.setBounds(boundary.getBounds());

    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }



    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }


    }

    private void rotateLeft() {
        this.angle -= this.rotationSpeed;
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        savex = x;
        savey = y;

        x -= vx;
        y -= vy;
        this.boundary.setLocation(x, y);
        this.boundary.setBounds(boundary.getBounds());

        //System.out.println(boundary.toString());
        checkBorder(savex, savey, x, y);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));

        savex = x;
        savey = y;

        x += vx;
        y += vy;

        this.boundary.setLocation(x, y);
        this.boundary.setBounds(boundary.getBounds());
        //System.out.println(boundary.toString());
        checkBorder(savex, savey, x, y);
    }




    private void checkBorder(int oldx, int oldy, int newx, int newy) {
        for(int i = 0; i < Map.mapA.size(); i++) {
            check = new Rectangle();
            check = Map.mapA.get(i);
            if (this.getRectangle().intersects(check)) {
                intersect = new Rectangle();
                intersect = this.boundary.intersection(check);
                Point tank = this.boundary.getLocation();
                Point wall = check.getLocation();
                Point intersectingBox = intersect.getLocation();
                Dimension dim = intersect.getSize();

                //if right of tank collides with wall to the right
                if(tank.getX() > wall.getX() && tank.getY() < wall.getY()){
                   y = oldy;

                }

                //if bottom of tank collides with wall at the bottom
                if(tank.getX() <  wall.getX() && tank.getY() > wall.getY()){
                    Point temp = new Point((int)tank.getX() + 16, (int)tank.getY() + 16);
                    if(check.contains(temp)) {
                        x = oldx;
                    }
                    //this means if tank is colliding with the corner of a piece
                    if(!check.contains(temp)){
                        //this means it is moving downward
                        if(oldx < newx){
                            //x = x - (int) dim.getHeight();
                            x = oldx;
                        }
                        //this means it is moving leftward
                        if(oldy > newy){
                            //y = y + (int) dim.getWidth();
                            y = newy;
                        }
                    }
                }

                if((tank.getX() > wall.getX()) && (tank.getY() > wall.getY())){

                    //if top of tank collides with bottom of wall
                    Point temp = new Point((int)tank.getX(), (int)tank.getY() + 16);
                    if(check.contains(tank) || !check.contains(temp)){
                            System.out.println("IHSLDFJLKjslkdfjlksjfdlksjflkjsldkfsjd\n");
                            x = x - (int) dim.getHeight();
                    //if left of tank hits right of wall

                    }else{
                        Point botRight = new Point((int)tank.getX() + 16, (int)tank.getY());
                        if(intersectingBox == botRight){
                            y = newy;
                        }else{
                            x = oldx;
                        }
                    }


                }
            }



//            if (x < 32) {
//                x = 32;
//            }
//            if (x > Game.screenWidth - 48) {
//                x = Game.screenWidth - 48;
//            }
//            if (y < 32) {
//                y = 32;
//            }
//            if (y > Game.screenHeight - 64) {
//                y = Game.screenHeight - 64;
//            }

            }
        }




    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
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
    public Rectangle getRectangle(){return this.boundary;}
    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


//    void drawImage(Graphics g) {
//        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
//        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawImage(this.img.getScaledInstance(16, 16, Image.SCALE_SMOOTH), rotation, null);
//    }

}
