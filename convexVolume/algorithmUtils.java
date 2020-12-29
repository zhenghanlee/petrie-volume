package convexVolume;

public class algorithmUtils {
    
    static class point {
        float x;
        float y;
        float z;
        public point (float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    static class edge {
        point start;
        point end;
        public edge(point start, point end){
            this.start = start;
            this.end = end;
        }
    }

    static class face {
        point p1;
        point p2;
        point p3;
        edge e1;
        edge e2;
        edge e3;
        public face (point p1, point p2, point p3){
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.e1 = new edge(p1, p2);
            this.e2 = new edge(p2, p3);
            this.e3 = new edge(p1, p3);
        }
        public face (edge e1, edge e2, edge e3){
            this.e1 = e1;
            this.e2 = e2;
            this.e3 = e3;
            this.p1 = e1.start;
            this.p2 = e1.end;
            if (e2.start == p1 || e2.start == p2){
                this.p3 = e2.end;
            }
            else{
                this.p3 = e2.start;
            }
        }
    }

    static class vector {
        float xvector;
        float yvector;
        float zvector;
        public vector(point start, point end){
            this.xvector = end.x - start.x;
            this.yvector = end.y - start.y;
            this.zvector = end.z - start.z;
        }
        public vector(float xvector, float yvector, float zvector){
            this.xvector = xvector;
            this.yvector = yvector;
            this.zvector = zvector;
        }
    }
    
}