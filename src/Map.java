package src;

import java.io.*;

public class Map{
    private static final String exec = "python";
    private static final String file = "makeMaps.py";
    private static final String param = "newMap";
    public void generateMap(){
        try {
            ProcessBuilder pb = new ProcessBuilder(exec, file, param);
            Process p = pb.start();
        }catch(IOException ex){
            System.out.println("***Failed to Generate Map" + ex);
        }

    }

}