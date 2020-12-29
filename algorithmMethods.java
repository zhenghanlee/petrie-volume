import algorithmUtils;
import java.util.ArrayList;
import java.util.List;

public class algorithmMethods {
    static List ConvexAlgorithm2D(List<point> coplanarSet){
        List<face> triangulatedFacets = new ArrayList<>();

        if (coplanarSet.size()==3){
            face f = new face(coplanarSet.get(0), coplanarSet.get(1), coplanarSet.get(2));
            triangulatedFacets.add(f);
            return triangulatedFacets;
        }

        List<point> vertices = new ArrayList<>();
        point firstPoint = FirstPoint(coplanarSet);

        vertices.add(firstPoint);
        coplanarSet.remove(firstPoint);

        point secondPoint = SecondPoint(coplanarSet,firstPoint);//Confirms second point.

        vertices.add(secondPoint);
        coplanarSet.add(firstPoint);
        coplanarSet.remove(secondPoint);
        int initsize = coplanarSet.size();

        for(int i = 0; i < initsize; i++){//Arbitrary perimeters, meaningless.
            point p1 = vertices.get(vertices.size() - 2);
            point p2 = vertices.get(vertices.size() - 1);
            point p3 = coplanarSet.get(0);
            vector rootVector = new vector(p1,p2);
            for(int j = 0; j < coplanarSet.size(); j++){
                vector testVector = new vector(p2,p3);
                vector contestVector = new vector(p2,coplanarSet.get(j));
                if (DotProduct(rootVector, contestVector)/VectorMagnitude(contestVector) > DotProduct(rootVector,testVector)/VectorMagnitude(testVector)){
                    p3 = coplanarSet.get(j);
                }
            }
            if (p3 == firstPoint){
                break;
            }
            vertices.add(p3);
            coplanarSet.remove(p3);
            face f = new face(firstPoint, p2, p3);
            triangulatedFacets.add(f);
        }//Add remaining points. Accumulate triangulated facets.

        coplanarSet.addAll(vertices);//Add back removed points.

        return triangulatedFacets;
    }

    static List ConvexHorizon2D(List<point> coplanarSet){
        List<edge> horizonEdges = new ArrayList<>();

        if (coplanarSet.size()==3){
            edge e1 = new edge(coplanarSet.get(0),coplanarSet.get(1));
            edge e2 = new edge(coplanarSet.get(2),coplanarSet.get(1));
            edge e3 = new edge(coplanarSet.get(0),coplanarSet.get(2));
            horizonEdges.add(e1);
            horizonEdges.add(e2);
            horizonEdges.add(e3);
            return horizonEdges;
        }

        List<point> vertices = new ArrayList<>();
        point firstPoint = FirstPoint(coplanarSet);

        vertices.add(firstPoint);
        coplanarSet.remove(firstPoint);

        point secondPoint = SecondPoint(coplanarSet,firstPoint);//Confirms second point.

        vertices.add(secondPoint);
        coplanarSet.add(firstPoint);
        coplanarSet.remove(secondPoint);
        int initsize = coplanarSet.size();

        for(int i = 0; i < initsize; i++){//Arbitrary perimeters, meaningless.
            point p1 = vertices.get(vertices.size() - 2);
            point p2 = vertices.get(vertices.size() - 1);
            point p3 = coplanarSet.get(0);
            vector rootVector = new vector(p1,p2);
            for(int j = 0; j < coplanarSet.size(); j++){
                vector testVector = new vector(p2,p3);
                vector contestVector = new vector(p2,coplanarSet.get(j));
                if (DotProduct(rootVector, contestVector)/VectorMagnitude(contestVector) > DotProduct(rootVector,testVector)/VectorMagnitude(testVector)){
                    p3 = coplanarSet.get(j);
                }
            }
            if (p3 == firstPoint){
                break;
            }
            vertices.add(p3);
            coplanarSet.remove(p3);
            face f = new face(firstPoint, p2, p3);
        }//Add remaining points. Accumulate triangulated facets.

        coplanarSet.addAll(vertices);//Add back removed points.

        edge e1 = new edge(vertices.get(vertices.size()-1),firstPoint);
        horizonEdges.add(e1);
        for(int i = 0; i < vertices.size()-1; i++){
            edge e = new edge(vertices.get(i),vertices.get(i+1));
            horizonEdges.add(e);
        }

        return horizonEdges;
    }

    static List CoplanarityTest(List<point> testSubjects, point p1, point p2, point p3){
        List<point> setOfCoPlanarPoints = new ArrayList<>();
        vector vector1 = new vector(p1,p2);
        vector vector2 = new vector(p1,p3);
        vector normal = CrossProduct(vector1,vector2);
        for (int i = 0; i< testSubjects.size();i++){
            point testPoint = testSubjects.get(i);
            vector testVector = new vector(p1, testPoint);
            if (DotProduct(testVector,normal)==0){
                setOfCoPlanarPoints.add(testPoint);
            }
        }
        return setOfCoPlanarPoints;
    }//To test for points in list if they are coplanar with firstPoint, secondPoint and p3 (including which).

    static List RemoveColinearPoints(List<point> randomSet){
        List<point> redundantpoints = new ArrayList<>();
        for(int i = 0; i < randomSet.size() - 2; i++){
            for(int j = i + 1; j < randomSet.size() - 1;j++){
                for(int k = j + 1; k < randomSet.size(); k++){
                    point p1 = randomSet.get(i);
                    point p2 = randomSet.get(j);
                    point p3 = randomSet.get(k);
                    vector v1 = new vector(p1,p2);
                    vector v2 = new vector(p1,p3);
                    if(DotProduct(v1,v2)/(VectorMagnitude(v1)*VectorMagnitude(v2)) <= -0.99999){
                        redundantpoints.add(p1);
                    }
                    else if(DotProduct(v1,v2)/(VectorMagnitude(v1)*VectorMagnitude(v2)) >= 0.99999){
                        vector v3 = new vector(p2,p3);
                        vector v4 = new vector(p2,p1);
                        if(DotProduct(v3,v4)/(VectorMagnitude(v3)*VectorMagnitude(v4)) <= -0.99999){
                            redundantpoints.add(p2);
                        }
                        else{
                            redundantpoints.add(p3);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < redundantpoints.size(); i++){
            randomSet.remove(redundantpoints.get(i));
        }
        return randomSet;
    }

    static List RemoveRepeatedPoints(List<point> randomSet){
        List<point> redundantpoints = new ArrayList<>();
        for(int i = 0; i < randomSet.size() - 1; i++){
            for(int j = i + 1; j < randomSet.size();j++){
                if(randomSet.get(i).x==randomSet.get(j).x && randomSet.get(i).y==randomSet.get(j).y && randomSet.get(i).z==randomSet.get(j).z){
                    redundantpoints.add(randomSet.get(i));
                }
            }
        }
        System.out.println(redundantpoints.size());
        for (int i = 0; i < redundantpoints.size(); i++){
            randomSet.remove(redundantpoints.get(i));
        }
        return randomSet;
    }

    static vector CrossProduct(vector vector1, vector vector2){
        float xvector = vector1.yvector * vector2.zvector - vector2.yvector * vector1.zvector;
        float yvector = vector1.zvector * vector2.xvector - vector2.zvector * vector1.xvector;
        float zvector = vector1.xvector * vector2.yvector - vector2.xvector * vector1.yvector;
        vector finalVector = new vector(xvector, yvector, zvector);
        return finalVector;
    }//To Calculate Cross Product of two Vectors.

    static float DotProduct(vector vector1, vector vector2){
        float dotproduct = vector1.xvector * vector2.xvector + vector1.yvector * vector2.yvector + vector1.zvector * vector2.zvector;
        return dotproduct;
    }//To Calculate Dot Product of two Vectors.

    static float VectorMagnitude(vector v){
        float magnitude = (float)Math.sqrt(Math.pow(v.xvector, 2)+Math.pow(v.yvector, 2)+Math.pow(v.zvector, 2));
        return magnitude;
    }

    static point FirstPoint(List<point> pointList){
        point firstPoint = pointList.get(0);

        for(int i = 0; i < pointList.size();i++){
            if (pointList.get(i).x <= firstPoint.x){
                firstPoint = pointList.get(i);
            }
        }//Find lowest x-value.
        List<point> lowestx = new ArrayList<>();
        for(int i = 0; i < pointList.size();i++){
            if (pointList.get(i).x == firstPoint.x){
                lowestx.add(pointList.get(i));
            }
        }//Find set of points with lowest x-value.

        for(int i = 0; i < lowestx.size();i++){
            if (lowestx.get(i).y <= firstPoint.y){
                firstPoint = lowestx.get(i);
            }
        }//Find lowest y-value in lowest x set..
        List<point> lowesty = new ArrayList<>();
        for(int i = 0; i < lowestx.size();i++){
            if (lowestx.get(i).y == firstPoint.y){
                lowesty.add(lowestx.get(i));
            }
        }//Find set of points with lowest y-value.

        for(int i = 0; i < lowesty.size();i++){
            if (lowesty.get(i).z < firstPoint.z){
                firstPoint = lowesty.get(i);
            }
        }//Confirms first point.
        return firstPoint;
    }

    static point SecondPoint(List<point> pointList, point firstPoint){
        pointList.remove(firstPoint);
        point anchorPoint = pointList.get(0);
        vector anchorVector = new vector(firstPoint,anchorPoint);
        point secondPoint = pointList.get(0);
        for(int i = 0; i < pointList.size();i++){
            vector rootVector = new vector(firstPoint,secondPoint);
            vector testVector = new vector(firstPoint,pointList.get(i));
            if (DotProduct(anchorVector, testVector)/VectorMagnitude(testVector)< DotProduct(anchorVector,rootVector)/VectorMagnitude(rootVector)){
                secondPoint = pointList.get(i);
            }
        }
        return secondPoint;
    }//Works only when first point is not in pointList.

    static float CosineBetweenTwoPlanes(edge flippingEdge, point point1, point point2){
        vector rootVector = new vector(flippingEdge.start,flippingEdge.end);
        vector vectorOne = new vector(flippingEdge.start,point1);
        vector vectorTwo = new vector(flippingEdge.start,point2);
        vector normalOne = CrossProduct(rootVector,vectorOne);
        vector normalTwo = CrossProduct(rootVector,vectorTwo);
        float cosineDelta = DotProduct(normalOne,normalTwo)/(VectorMagnitude(normalOne)*VectorMagnitude(normalTwo));
        return cosineDelta;
    }//The lower the value, the wider the angle.

    static boolean FaceContainEdge(face f, edge e){
        edge er = new edge(e.end,e.start);
        if((f.e1.start==e.start && f.e1.end==e.end)||(f.e2.start==e.start && f.e2.end==e.end)||(f.e3.start==e.start && f.e3.end==e.end)){
            return true;
        }
        return (f.e1.start==er.start && f.e1.end==er.end)||(f.e2.start==er.start && f.e2.end==er.end)||(f.e3.start==er.start && f.e3.end==er.end);
    }

    static boolean FaceContainPoint(face f, point p){
        return f.p1==p||f.p2==p||f.p3==p;
    }

    static point FaceMinusEdge(face f, edge e){
        List<point> pointSet = new ArrayList<>();
        pointSet.add(f.p1);
        pointSet.add(f.p2);
        pointSet.add(f.p3);
        pointSet.remove(e.start);
        pointSet.remove(e.end);
        point remainder = pointSet.get(0);
        return remainder;
    }

    static boolean EdgeListMinusEdge(List<edge> edgeList, edge e){
        edge er = new edge(e.end,e.start);
        for(int i =0; i< edgeList.size();i++){
            if((edgeList.get(i).start==e.start&&edgeList.get(i).end==e.end)||(edgeList.get(i).start==er.start&&edgeList.get(i).end==er.end)){
                edgeList.remove(i);
                return true;
            }
        }
        return false;
    }

    static boolean EndOfPetrieSequence(List<point> sequence){
        point p1 = sequence.get(0);
        point p2 = sequence.get(1);
        point p3 = sequence.get(2);
        point pm2 = sequence.get(sequence.size() - 3);
        point pm1 = sequence.get(sequence.size() - 2);
        point newPoint = sequence.get(sequence.size() - 1);
        return (p1==pm1)&&(p2==newPoint)&&(p3!=pm2);
    }

    static float CrossMultiplicationMethod(List<point> sequence){
        if(sequence.size()<3){
            throw new Error("Not Enough Points to perform Cross Multiplication Method");
        }
        float value = 0;
        for(int i = 0; i < sequence.size() - 2; i++){
            if(i%2==0){
                value += sequence.get(i).x * sequence.get(i+1).y * sequence.get(i+2).z - sequence.get(i).z * sequence.get(i+1).y * sequence.get(i+2).x;
            }
            else{
                value -= sequence.get(i).x * sequence.get(i+1).y * sequence.get(i+2).z - sequence.get(i).z * sequence.get(i+1).y * sequence.get(i+2).x;
            }
        }
        return value;
    }

    static List UnusedSequence(List<point> sequence, face f){
        List<face> unusedSequence = new ArrayList<>();
        face f2 = new face(f.p2,f.p3,f.p1);
        face f3 = new face(f.p3,f.p1,f.p2);
        unusedSequence.add(f2);
        unusedSequence.add(f3);
        for(int i = 0; i < sequence.size()-2; i++){
            if((sequence.get(i)==f.p1&&sequence.get(i+1)==f.p3&&sequence.get(i+2)==f.p2)||((sequence.get(i)==f.p2&&sequence.get(i+1)==f.p3&&sequence.get(i+2)==f.p1))){
                unusedSequence.remove(0);
            }
            if((sequence.get(i)==f.p2&&sequence.get(i+1)==f.p1&&sequence.get(i+2)==f.p3)||((sequence.get(i)==f.p3&&sequence.get(i+1)==f.p1&&sequence.get(i+2)==f.p2))){
                unusedSequence.remove(unusedSequence.size()-1);
            }
        }
        if(unusedSequence.isEmpty()){
            return unusedSequence;
        }
        else{
            List<point> proposedSequence = new ArrayList<>();
            proposedSequence.add(unusedSequence.get(0).p1);
            proposedSequence.add(unusedSequence.get(0).p2);
            proposedSequence.add(unusedSequence.get(0).p3);
            return proposedSequence;
        }
    }//Make sure input of face f is already in correct order as first appeared in sequence.

}

