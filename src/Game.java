package src;

import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;



public class Game extends JPanel{

    public static final int screenWidth = 1280;
    public static final int screenHeight = 720;

    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;
    private Tank p1;
    private Tank p2;

    private void init(){
        try {
            Map map1 = new Map();
            map1.generateMap();
        }catch(IOException e){
            System.out.println("***Unable to Generate Map***\n" + e);
        }
        try {
            this.jf = new JFrame("Tank Wars");
            String path = "src/resources/Background.bmp";
            BufferedImage bg = ImageIO.read(new File(path));

            this.world = new BufferedImage(Game.screenWidth, Game.screenHeight, BufferedImage.TYPE_INT_ARGB);

            //Got this from stackoverflow https://stackoverflow.com/questions/10391778/create-a-bufferedimage-from-file-and-make-it-type-int-argb
            Graphics2D back = this.world.createGraphics();
            back.drawImage(bg.getScaledInstance(Game.screenWidth, Game.screenHeight, Image.SCALE_SMOOTH), 0, 0, null);
            back.dispose();

            jf.setContentPane(new JLabel(new ImageIcon(bg)));

            this.jf.setLayout(new BorderLayout());
            this.jf.add(this);
            //this.jf.addKeyListener(tc1);
            this.jf.setSize(Game.screenWidth, Game.screenHeight + 30);
            this.jf.setResizable(false);
            jf.setLocationRelativeTo(null);

            this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.jf.setVisible(true);

        }catch(IOException e){
            System.out.println("***Unable to Generate Game****\n" + e);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = this.world.createGraphics();
        super.paintComponent(g2);
        //this.p1.drawImage(buffer);
        g2.drawImage(this.world,0,0,null);


    }

    public static void main(String[] args) {
        Game newGame = new Game();
        newGame.init();

            while (true) {
                //newGame.repaint();
            }


    }

}