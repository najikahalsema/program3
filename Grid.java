import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.lang.Math;

public class Grid extends JPanel {
    private ArrayList<GridPoint> points;
    private boolean isDragging = false;

    public Grid() {
        super();
        this.setSize(new Dimension(300,300));
        points = new ArrayList<GridPoint>();

        //Create 10x10 grid of points. i = rows, j = columns
        for (int i = 0; i < 300; i += 30) {
            for (int j = 0; j < 300; j+= 30) {
                GridPoint point = new GridPoint(i, j, i % 30, j % 30);
                points.add(point);
            }
        }
        repaint();
        revalidate();

        /*
        this.addMouseListener(new MouseListener(){
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseReleased(MouseEvent e){
                isDragging = false;
            }
            public void mousePressed(MouseEvent e){
                if(ExistsClickedPoint(e)){
                    isDragging = true;
                }
            }
            public void mouseClicked(MouseEvent e){}
        });

        //create a motion listener to track the mouse dragging the polygon
        this.addMouseMotionListener(new MouseMotionListener(){
            public void mouseDragged(MouseEvent e) {
                if(isDragging){
                    //int x2[] = {10,250,e.getX(),150};
                    //int y2[] = {5,150,e.getY(),200};
                    //panel.setRightX(e.getX());
                    //panel.setRightY(e.getY());
                    //panel.setPoly(new Polygon(x2,y2,4));
                    //panel.repaint();


                    Grid.super.repaint();
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });
        */
    }

    public boolean ExistsClickedPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < points.size(); i++) {
            int pntx = points.get(i).GetX();
            int pnty = points.get(i).GetY();
            if (Math.abs(x - pntx) <= 4 && Math.abs(y - pnty) <= 4) {
                return true;
            }
        }
        return false;
    }

    public GridPoint GetClickedPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < points.size(); i++) {
            int pntx = points.get(i).GetX();
            int pnty = points.get(i).GetY();
            if (Math.abs(x - pntx) <= 4 && Math.abs(y - pnty) <= 4) {
                return points.get(i);
            }
        }
        return null;
    }

    public void paintComponent(Graphics g) {
        for (int i = 0; i < points.size(); i++) {
            g.fillOval(points.get(i).GetX(), points.get(i).GetY(), 8, 8);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        Grid grid = new Grid();
        frame.getContentPane().add(grid);
        //frame.pack();
        frame.setVisible(true);
    }
}
