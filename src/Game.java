package src;

import java.io.*;
import java.io.IOException;

public class Game{

    public static void main(String[] args){
        try {
            Map map1 = new Map();
            map1.generateMap();
        }catch(IOException e){
            System.out.println("***Unable to Generate Map***\n" + e);
        }
    }

}