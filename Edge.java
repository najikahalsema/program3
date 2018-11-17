import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Edge {
    private GridPoint point1;
    private GridPoint point2;

    public Edge(GridPoint point1, GridPoint point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public GridPoint GetPoint1() {
        return point1;
    }
    public GridPoint GetPoint2() {
        return point2;
    }
}
