package convexVolume;

import java.util.ArrayList;
import java.util.List;

import convexVolume.algorithmUtils.*;

class volumeCalculator{
    public static void main(String[] args) {

        point p = new point(0, 0, 0);
        point p2 = new point(1, 0, 0);
        point p3 = new point(0, 1, 0);
        point p4 = new point(0, 0, 1);
        points.add(p);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        try {
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
        }
        catch (Error e){
            System.out.println("Unable to evaluate volume.");
        }
        catch(Exception e){
            System.out.println("Unable to evaluate volume.");
        }
    }

    static List<point> points = new ArrayList<>();//Defining points.
}