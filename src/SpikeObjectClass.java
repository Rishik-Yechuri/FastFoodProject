import java.awt.*;

public class SpikeObjectClass {
    Polygon dangerousSpike;
    int xPosition;
    int yPosition;

    public SpikeObjectClass(int xPos, int yPos) {
        //Initializes dangerousSpike and sets top point coordinates
        dangerousSpike = new Polygon();
        //dangerousSpike.npoints = 2;
        xPosition = xPos;
        yPosition = yPos;

        //Triangle's points are added to polygon,the coords for the second two points are created given first point coords
        dangerousSpike.addPoint(xPosition, yPosition);
        dangerousSpike.addPoint(xPosition - 13, yPosition + 26);
        dangerousSpike.addPoint(xPosition + 13, yPosition + 26);
       // dangerousSpike.addPoint(xPosition, yPosition);
    }

    public Polygon getShape() {
        return dangerousSpike;
    }

    public void move(int x, int y) {
        xPosition = x;
        yPosition = y;
        dangerousSpike = new Polygon();
        dangerousSpike.addPoint(xPosition, yPosition);
        dangerousSpike.addPoint(xPosition - 13, yPosition + 26);
        dangerousSpike.addPoint(xPosition + 13, yPosition + 26);
    }
}
