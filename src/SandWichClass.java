import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class SandWichClass {
    int xLocation;
    int yLocation;
    int healthLeft;
    public SandWichClass(){
        xLocation = 0;
        yLocation = 0;
        healthLeft = 3;
    }
    public void moveWich(int newX,int newY){
        xLocation = newX;
        yLocation = newY;
    }
    public int wichCollided(/*int[][] objectPoints*/Polygon collisionShape){
        //Creates WhichWich Shape which can later be compared to object created from objectPoints
        Rectangle tempWich = new Rectangle(xLocation,yLocation,80,45);
        AffineTransform affineTransform = new AffineTransform();
        Shape transformedWich = affineTransform.createTransformedShape(tempWich);
        Rectangle2D wichShape = transformedWich.getBounds2D();

        //Creates a Polygon object which represents the obstacle
        Polygon collisionPolygon = /*new Polygon()*/collisionShape;
        /*for (int[] objectPoint : objectPoints) {
            collisionPolygon.addPoint(objectPoint[0], objectPoint[1]);
        }*/

        /*Checks to see if the WhichWich has collided with the given obstacle,
        and returns true if it has
        */
        if(collisionPolygon.intersects(wichShape)){
            healthLeft--;
            //return true;
        }
        return healthLeft;
    }
}
