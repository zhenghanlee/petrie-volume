package convexVolume;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;

import convexVolume.algorithmUtils.*;

class volumeCalculator{
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        if (args.length > 0 && args[0].equalsIgnoreCase("new")){

            System.out.println("Input coordinates of the points in the following format (spaces between coordinates): <x-coordinate> <y-coordinate> <z-coordinate>");
            System.out.println("Enter without input to evaluate convex volume.");

            boolean finish = false;
            int inputCount = 1;
            while(!finish){
                System.out.printf("Point %d: ", inputCount);
                String string = reader.readLine();
                StringBuilder input = new StringBuilder(string);
                if (input.length() == 0){
                    finish = true;
                } else{
                    input.append(" ");
                    float x = getNumber(input);
                    float y = getNumber(input);
                    float z = getNumber(input);
                    point p = new point(x, y, z);
                    points.add(p);
                    inputCount += 1;
                }
            }

            points = algorithmMethods.RemoveRepeatedPoints(points);
            points = algorithmMethods.RemoveColinearPoints(points);
            if(points.size()<=3){
                System.out.println("Set of Points are Co-Planar.");
            }
            else if(algorithmMethods.CoplanarityTest(points,points.get(0),points.get(1),points.get(3)).size() == points.size()){
                System.out.println("Set of Points are Co-Planar.");
            }
            else{
                List<face> facets = mainAlgorithms.GiftWrappingAlgorithm(points);
                float volume = mainAlgorithms.ConvexVolumeAlgorithm(facets);
                System.out.println("Volume = " + volume);
            }
            // catch(Exception e){
            //     System.out.println("Unable to evaluate volume.");
            // }
        }
        
    }

    static float getNumber(StringBuilder string){
        int firstSpace = string.indexOf(" ");
        String numbertext = string.substring(0, firstSpace);
        string.delete(0, firstSpace + 1);
        try {
            return Float.parseFloat(numbertext);
        } catch(Exception e){
            System.out.println("Input format not adhered to!");
            throw e;
        }        
    }

    static List<point> points = new ArrayList<>();//Defining points.
}