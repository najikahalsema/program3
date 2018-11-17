import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class GridPoint {
    private int x;
    private int y;
    private int pnt_center_x;
    private int pnt_center_y;
    private int row;
    private int column;
    private Color color = Color.BLACK;

    //private ArrayList<Edge> edges;

    public GridPoint(int x, int y, int row, int column) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
        pnt_center_x = x + 5;
        pnt_center_y = y + 5;
    }

    /*
    public void AddEdge(Edge newedge) {
        edges.add(newedge);
    }
    */
    public int GetX() {
        return x;
    }
    public int GetY() {
        return y;
    }
    public int GetCenterX() {
        return pnt_center_x;
    }
    public int GetCenterY() {
        return pnt_center_y;
    }

    public void SetX(int x) {
        this.x = x;
    }
    public void SetY(int y) {
        this.y = y;
    }
    public void SetXY(int x, int y) {
        this.x = x;
        this.y = y;
        pnt_center_x = x + 5;
        pnt_center_y = y + 5;
    }
    public Color GetColor() {
        return color;
    }
    public void SetColor(Color newcolor) {
        color = newcolor;
    }
    public int GetRow() {
        return row;
    }
    public int GetColumn() {
        return column;
    }
}
