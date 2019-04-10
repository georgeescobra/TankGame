package src;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends Object{

    private BufferedReader mapFile;
    private static final String map1 = "src/newMap.txt";
    private ArrayList<List<Integer>> base;
    private ArrayList<Integer> mapA;

    public Map() throws IOException{
        this.mapFile = new BufferedReader(new FileReader(map1));
    }

    /*
    This function is to convert the txtFile into an ArrayList
     */
    public void generateMap(){
        try {
            base = new ArrayList<>();
            for(int i = 0; i < 32; i ++){
               String temp;
                while((temp = mapFile.readLine()) != null) {
                    mapA = new ArrayList<>();
                   //System.out.println(temp);
                    for (int j = 0; j < temp.length(); j++) {
                        char c = temp.charAt(j);
                        mapA.add(Character.getNumericValue(c));
                    }
                    //System.out.println(mapA);
                    base.add(mapA);
                }
            }


        }catch(IOException e) {
            System.out.println("***Unable to Parse Map\n" + e);
        }
    }


}