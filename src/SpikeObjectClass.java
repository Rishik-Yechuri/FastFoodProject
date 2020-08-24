import java.awt.*;

public class SpikeObjectClass{
    Polygon dangerousSpike;
    int xPosition;
    int yPosition;
    public SpikeObjectClass(int xPos,int yPos) {
        //Initializes dangerousSpike and sets top point coordinates
        dangerousSpike = new Polygon();
        xPosition = xPos;
        yPosition = yPos;

        //Triangle's points are added to polygon,the coords for the second two points are created given first point coords
        dangerousSpike.addPoint(xPosition,yPosition);
        dangerousSpike.addPoint(yPosition+26,xPosition-13);
        dangerousSpike.addPoint(yPosition+26,xPosition+13);
    }
    public Polygon getShape(){
        return dangerousSpike;
    }
}
