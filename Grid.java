import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.lang.Math;

public class Grid extends JPanel {
    //private ArrayList<GridPoint> points;
    private GridPoint[][] points;   //i = x, j = y
    private ArrayList<Edge> edges;
    private boolean isDragging = false;
    private GridPoint point = null;
    private boolean lock_selection = false;
    private int count = 1;
    Timer timer;

    public Grid() {
        super();
        this.setSize(new Dimension(300,300));
        points = new GridPoint[10][10];
        //points = new GridPoint[10][10]

        //Create 10x10 grid of points. i = rows, j = columns
        for (int i = 0; i < 300; i += 30) {
            for (int j = 0; j < 300; j+= 30) {
                GridPoint point = new GridPoint(i, j, i / 30, j / 30);
                points[i / 30][j / 30] = point;
            }
        }

        ConnectPoints();
        repaint();
        revalidate();
    }

    public Grid(MouseListener listener, MouseMotionListener motion) {
        super();
        this.setSize(new Dimension(300,300));
        points = new GridPoint[10][10];
        //points = new GridPoint[10][10]

        //Create 10x10 grid of points. i = rows, j = columns
        for (int i = 0; i < 300; i += 30) {
            for (int j = 0; j < 300; j+= 30) {
                GridPoint point = new GridPoint(i, j, i / 30, j / 30);
                points[i / 30][j / 30] = point;
            }
        }

        ConnectPoints();
        repaint();
        revalidate();

        this.addMouseListener(listener);
        this.addMouseMotionListener(motion);
    }

    public GridPoint GetActivePoint() {
        return point;
    }

    public void SetActivePoint(GridPoint correspond_pnt) {
        points[correspond_pnt.GetRow()][correspond_pnt.GetColumn()].SetColor(Color.RED);
    }

    public void SetDragging(boolean value) {
        isDragging = value;
    }

    public boolean IsDragging() {
        return isDragging;
    }

    public void SetLock(boolean value) {
        lock_selection = value;
    }

    public boolean IsLocked() {
        return lock_selection;
    }

    public void Redraw() {
        repaint();
    }

    public GridPoint GetMatchingPoint(int row, int column) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].GetRow() == row && points[i][j].GetColumn() == column) {
                    return points[i][j];
                }
            }
        }
        return null;
    }

    public void ConnectPoints() {
        edges = new ArrayList<Edge>();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                /*
                if (j < points[i].length - 2 && i < points.length - 2) {
                    //Connect right
                    //Edge right = new Edge(points[i][j], points[i+1][j]);
                    //Connect bottom-right
                    //edges.add(right);
                    Edge diagonal = new Edge(points[i][j], points[i+1][j+1]);
                    edges.add(diagonal);
                }
                if (j > 0) {
                    //Connect left
                    Edge left = new Edge(points[i][j], points[i][j-1]);
                    edges.add(left);
                }
                if (i < points.length - 2) {
                    Edge down = new Edge(points[i][j], points[i+1][j]);
                    edges.add(down);
                }
                */
                if (i > 0) {
                    //Connect left
                    Edge left = new Edge(points[i][j], points[i-1][j]);
                    edges.add(left);
                }
                if (j < points[i].length - 1 && i < points.length - 1) {
                    //Connect bottom-right
                    Edge diagonal = new Edge(points[i][j], points[i+1][j+1]);
                    edges.add(diagonal);
                }
                if (j < points[i].length - 1) {
                    //Connect down
                    Edge down = new Edge(points[i][j], points[i][j+1]);
                    edges.add(down);
                }
            }
        }
    }

    public boolean ExistsClickedPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                int pntx = points[i][j].GetX();
                int pnty = points[i][j].GetY();
                if (Math.abs(x - pntx) <= 5 && Math.abs(y - pnty) <= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public GridPoint GetClickedPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                int pntx = points[i][j].GetX();
                int pnty = points[i][j].GetY();
                if (Math.abs(x - pntx) <= 5 && Math.abs(y - pnty) <= 5) {
                    return points[i][j];
                }
            }
        }
        return null;
    }

    public void Morph(Grid start, Grid end, int fps, int seconds) {
        ActionListener event = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(count);
                if (count != (fps*seconds) - 1) {
                    for (int i = 0; i < start.points.length; i++) {
                        for (int j = 0; j < start.points[i].length; j++) {
                            float dx = end.points[i][j].GetX() - start.points[i][j].GetX();
                            float dy = end.points[i][j].GetY() - start.points[i][j].GetY();
                            int start_x = start.points[i][j].GetX();
                            int start_y = start.points[i][j].GetY();
                            points[i][j].SetXY(start_x + Math.round((dx / (fps * seconds))*count), start_y + Math.round((dy / (fps * seconds))*count));
                            if (count == (fps*seconds) - 1) {
                                points[i][j].SetXY(end.points[i][j].GetX(), end.points[i][j].GetY());
                            }
                        }
                    }
                    repaint();
                    revalidate();
                    count++;
                }
                else {
                    timer.stop();
                    repaint();
                    revalidate();
                    count = 1;
                }
            }
        };
        int interval = (int)(((float)seconds/(float)fps)*1000);
        System.out.println(interval);
        timer = new Timer(interval, event);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                g.setColor(points[i][j].GetColor());
                g.fillOval(points[i][j].GetX(), points[i][j].GetY(), 10, 10);
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            g.drawLine(edges.get(i).GetPoint1().GetX() + 5, edges.get(i).GetPoint1().GetY() + 5,
                       edges.get(i).GetPoint2().GetX() + 5, edges.get(i).GetPoint2().GetY() + 5);
        }
    }

    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        Grid grid1 = new Grid();
        Grid grid2 = new Grid();
        frame.getContentPane().add(grid1);
        frame.getContentPane().add(grid2);
        //frame.pack();
        frame.setVisible(true);
    }
    */
}
