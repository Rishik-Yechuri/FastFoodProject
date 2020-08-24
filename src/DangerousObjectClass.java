import java.awt.*;

public class DangerousObjectClass {
    Polygon dangerousObject;
    //Stores the location of the object(top left corner)
    int xLocation;
    int yLocation;
    public DangerousObjectClass(int[][] objectPoints) {
        //Initializes dangerObject and adds list of points to it,also saves the coordinates of the top left corner
        dangerousObject = new Polygon();
        for (int[] objectPoint : objectPoints) {
            dangerousObject.addPoint(objectPoint[0], objectPoint[1]);
        }
        xLocation = objectPoints[0][0];
        yLocation = objectPoints[0][1];
    }
    public Polygon getShape(){
        return dangerousObject;
    }
}
