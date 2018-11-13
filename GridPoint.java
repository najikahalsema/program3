import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class GridPoint {
    private int x;
    private int y;
    private int row;
    private int column;

    private ArrayList<Edge> edges;

    public GridPoint(int x, int y, int row, int column) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
    }

    public void AddEdge(Edge newedge) {
        edges.add(newedge);
    }
    public int GetX() {
        return x;
    }
    public int GetY() {
        return y;
    }
}
