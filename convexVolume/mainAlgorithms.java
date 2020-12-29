package convexVolume;
import java.util.ArrayList;
import java.util.List;

import convexVolume.algorithmUtils.*;

public class mainAlgorithms {

    static List GiftWrappingAlgorithm(List<point> pointList){
        if(pointList.size()<=3){
            throw new Error("Set of Points are Co-Planar");
        }
        if(algorithmMethods.CoplanarityTest(pointList,pointList.get(0),pointList.get(1),pointList.get(3)).size() == pointList.size()){
            throw new Error("Set of Points are Co-Planar");
        }

        List<face> facets = new ArrayList<>();
        List<edge> unprocessedEdges = new ArrayList<>();

        point firstPoint = algorithmMethods.FirstPoint(pointList);//Confirms first point.
        pointList.remove(firstPoint);

        point secondPoint = algorithmMethods.SecondPoint(pointList,firstPoint);//Gets tentative second point.
        pointList.remove(secondPoint);

        edge firstEdge = new edge(firstPoint,secondPoint);
        point anchorPoint = pointList.get(0);
        point thirdPoint = pointList.get(0);
        for (int i =0; i< pointList.size();i++){
            if(algorithmMethods.CosineBetweenTwoPlanes(firstEdge, pointList.get(i), anchorPoint)< algorithmMethods.CosineBetweenTwoPlanes(firstEdge, thirdPoint, anchorPoint)){
                thirdPoint = pointList.get(i);
            }
        }//Gets tentative third point.

        pointList.add(firstPoint);
        pointList.add(secondPoint);
        List<point> coplanarSet = algorithmMethods.CoplanarityTest(pointList, firstPoint, secondPoint, thirdPoint);
        facets.addAll(algorithmMethods.ConvexAlgorithm2D(coplanarSet));
        unprocessedEdges.addAll(algorithmMethods.ConvexHorizon2D(coplanarSet));

        while(!unprocessedEdges.isEmpty()){
            edge flippingEdge = unprocessedEdges.get(0);
            face originalFace = facets.get(0);
            int number = 0;
            while(!algorithmMethods.FaceContainEdge(originalFace,flippingEdge)){
                number += 1;
                originalFace = facets.get(number);
            }
            point originalPoint = algorithmMethods.FaceMinusEdge(originalFace,flippingEdge);
            List<point> candidatePoints = pointList;
            candidatePoints.remove(flippingEdge.start);
            candidatePoints.remove(flippingEdge.end);
            point potentialPoint = candidatePoints.get(0);
            for(int i = 0; i < candidatePoints.size(); i++){
                if (algorithmMethods.CosineBetweenTwoPlanes(flippingEdge, originalPoint, candidatePoints.get(i)) < algorithmMethods.CosineBetweenTwoPlanes(flippingEdge, originalPoint, potentialPoint)){
                    potentialPoint = candidatePoints.get(i);
                }
            }
            candidatePoints.add(flippingEdge.start);
            candidatePoints.add(flippingEdge.end);
            candidatePoints = algorithmMethods.CoplanarityTest(candidatePoints,flippingEdge.start,flippingEdge.end,potentialPoint);
            List<edge> newEdges = algorithmMethods.ConvexHorizon2D(candidatePoints);
            for(int i = newEdges.size() - 1; i >= 0; i--){
                edge e = newEdges.get(i);
                if(algorithmMethods.EdgeListMinusEdge(unprocessedEdges,e)){
                    newEdges.remove(i);
                }
            }
            unprocessedEdges.addAll(newEdges);
            List<face> newFacets = algorithmMethods.ConvexAlgorithm2D(candidatePoints);
            facets.addAll(newFacets);
        }

        return facets;
    }//Return list of facets (triangulated)

    static float ConvexVolumeAlgorithm(List<face> facets){
        List<point> petrieSequence = new ArrayList<>();
        petrieSequence.add(facets.get(0).p1);
        petrieSequence.add(facets.get(0).p2);
        petrieSequence.add(facets.get(0).p3);
        while(!algorithmMethods.EndOfPetrieSequence(petrieSequence)){
            point nextPoint = null;
            edge flippingEdge = new edge(petrieSequence.get(petrieSequence.size()-2),petrieSequence.get(petrieSequence.size()-1));
            for(int i = 0; i < facets.size(); i++){
                if(algorithmMethods.FaceContainEdge(facets.get(i),flippingEdge)&&!algorithmMethods.FaceContainPoint(facets.get(i),petrieSequence.get(petrieSequence.size()-3))){
                    nextPoint = algorithmMethods.FaceMinusEdge(facets.get(i),flippingEdge);
                }
            }
            petrieSequence.add(nextPoint);
        }
        if(petrieSequence.size() == 3*facets.size() + 2){
            float volume = (float) (1.0/6.0 * algorithmMethods.CrossMultiplicationMethod(petrieSequence));
            volume = Math.abs(volume);
            return volume;
        }
        else{
            float volume = (float) (1.0/6.0 * algorithmMethods.CrossMultiplicationMethod(petrieSequence));
            List<point> petrieTrackingSequence = new ArrayList<>();
            petrieTrackingSequence.add(petrieSequence.get(0));
            petrieTrackingSequence.addAll(petrieSequence);
            petrieTrackingSequence.add(petrieSequence.get(petrieSequence.size()-1));
            for(int i = 0; i < petrieTrackingSequence.size() - 3; i++){
                point p1 = petrieTrackingSequence.get(i);
                point p2 = petrieTrackingSequence.get(i+1);
                point p3 = petrieTrackingSequence.get(i+2);
                if(p1!=p2 && p2!=p3){
                    face f = new face(p1,p2,p3);
                    if(i%2==0){
                        f.p1 = p3;
                        f.p2 = p2;
                        f.p3 = p1;
                    }
                    List<point> proposedSequence = algorithmMethods.UnusedSequence(petrieTrackingSequence, f);
                    if(!proposedSequence.isEmpty()){

                        petrieSequence.clear();
                        petrieSequence.addAll(proposedSequence);
                        while(!algorithmMethods.EndOfPetrieSequence(petrieSequence)){
                            point nextPoint = null;
                            edge flippingEdge = new edge(petrieSequence.get(petrieSequence.size()-2),petrieSequence.get(petrieSequence.size()-1));
                            for(int j = 0; j < facets.size(); j++){
                                if(algorithmMethods.FaceContainEdge(facets.get(j),flippingEdge)&&!algorithmMethods.FaceContainPoint(facets.get(j),petrieSequence.get(petrieSequence.size()-3))){
                                    nextPoint = algorithmMethods.FaceMinusEdge(facets.get(j),flippingEdge);
                                }
                            }
                            petrieSequence.add(nextPoint);
                        }
                        volume += (float) (1.0/6.0 * algorithmMethods.CrossMultiplicationMethod(petrieSequence));
                        petrieTrackingSequence.add(petrieSequence.get(0));
                        petrieTrackingSequence.addAll(petrieSequence);
                        petrieTrackingSequence.add(petrieSequence.get(petrieSequence.size()-1));
                    }
                }
            }
            volume = Math.abs(volume);
            return volume;
        }
    }//Calculate absolute volume.

}