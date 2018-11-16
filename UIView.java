import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class UIView extends JFrame {
    private BufferedImage leftImage;
    private ViewController leftView;
    private BufferedImage rightImage;
    private ViewController rightView;
    // Buttons to preview the transformation
    private JButton PreviewButtonRL;
    private JButton PreviewButtonLR;
    private JButton ResetButton;
    private int x, y;
    private boolean firstdrag = true;
    private JLabel FpsLabel;
    private JSlider FpsSlider;

    // Constructor for the frame
    public UIView() {

        super();

        this.buildMenus();
        this.buildComponents();
        this.buildDisplay();
    }

    private void buildMenus() {

        final JFileChooser fc = new JFileChooser(".");
        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);
        JMenu fileMenu = new JMenu("File");
        JMenuItem openLeft = new JMenuItem("Open Left");
        JMenuItem openRight = new JMenuItem("Open Right");
        JMenuItem fileexit = new JMenuItem("Exit");

        // action listener to change the left image
        openLeft.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(UIView.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            try {
                                leftImage = ImageIO.read(file);
                            } catch (IOException e1) {
                            }
                            ;

                            leftView.setImage(leftImage);
                            leftView.showImage();
                        }
                    }
                }
        );
        // open a new file to use as the right image
        openRight.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(UIView.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            try {
                                rightImage = ImageIO.read(file);
                            } catch (IOException e2) {};
                            rightView.setImage(rightImage);
                            rightView.showImage();
                        }
                    }
                }
        );
        fileexit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        fileMenu.add(openLeft);
        fileMenu.add(openRight);
        fileMenu.add(fileexit);
        bar.add(fileMenu);
    }

    //  Allocate components (these are all instance vars of this frame object)
    //  and set up action listeners for each of them
    //  This is called once by the constructor

    private void buildComponents() {
        leftView = new ViewController(readImage("3.jpg"));
        rightView = new ViewController(readImage("3.jpg"));
        //view = new ViewController();
        ResetButton = new JButton("Reset");
        PreviewButtonRL = new JButton("Preview R --> L");
        PreviewButtonLR = new JButton("Preview L --> R");
        FpsLabel = new JLabel("Frames per second: 30");
        FpsSlider = new JSlider(SwingConstants.VERTICAL, 1, 60, 30);

        FpsSlider.setMajorTickSpacing(5);
        FpsSlider.setPaintTicks(true);
        FpsSlider.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        FpsLabel.setText("Frames per Second: " +
                                Integer.toString(FpsSlider.getValue()));
                    }
                }
        );
        /* Listen for mouse events to allow painting on image
        leftView.addMouseMotionListener(
                new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent event) {
                        Graphics g = leftView.getGraphics();
                        g.setColor(Color.white);
                        if (firstdrag) {
                            x = event.getX();
                            y = event.getY();
                            firstdrag = false;
                        } else {
                            leftView.showImage();
                            x = event.getX();
                            y = event.getY();
                            int w = leftView.getImage().getWidth();
                            int h = leftView.getImage().getHeight();
                            g.fillOval(x - 5, y - 5, 10, 10);
                            g.drawLine(0, 0, x, y);
                            g.drawLine(0, h, x, y);
                            g.drawLine(w, h, x, y);
                            g.drawLine(w, 0, x, y);
                        }
                    }
                }
        );
        // Listen for mouse release to detect when we've stopped painting
        leftView.addMouseListener(
                new MouseAdapter() {
                    public void mouseReleased(MouseEvent event) {
                        Graphics g = leftView.getGraphics();
                        firstdrag = true;
                        x = event.getX();
                        y = event.getY();
                        int w = leftView.getImage().getWidth();
                        int h = leftView.getImage().getHeight();
                        g.fillOval(x - 5, y - 5, 10, 10);
                        g.drawLine(0, 0, x, y);
                        g.drawLine(0, h, x, y);
                        g.drawLine(w, h, x, y);
                        g.drawLine(w, 0, x, y);
                    }
                }
        );*/
        // Button listeners activate the buffered image object in order
        // to display appropriate function
        ResetButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        leftView.showImage();
                        rightView.showImage();
                    }
                }
        );
        PreviewButtonRL.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        rightView.PreviewRL();
                        rightView.repaint();
                    }
                }
        );
        PreviewButtonLR.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        leftView.PreviewLR(leftImage);
                        leftView.repaint();
                    }
                }
        );
    }
    // This helper method adds all components to the content pane of the
    // JFrame object.  Specific layout of components is controlled here
    private void buildDisplay() {

        // Build first JPanel
        JPanel controlPanel = new JPanel();
        GridLayout grid = new GridLayout(1, 5);
        controlPanel.setLayout(grid);
        controlPanel.add(ResetButton);
        controlPanel.add(PreviewButtonRL);
        controlPanel.add(PreviewButtonLR);

        // Build second JPanel
        /**** To do:
         * Potentially change the Layout style to allow for more fluid resizing
         * of components
         */
        JPanel FpsControl = new JPanel();
        BorderLayout layout = new BorderLayout(0,0);
        FpsControl.setLayout(layout);
        FpsControl.add(FpsLabel, BorderLayout.NORTH);
        FpsControl.add(FpsSlider, BorderLayout.CENTER);

        // Add panels and image data component to the JFrame
        Container c = this.getContentPane();
        c.add(leftView, BorderLayout.CENTER);
        c.add(rightView, BorderLayout.EAST);
        c.add(controlPanel, BorderLayout.PAGE_END);
        c.add(FpsControl, BorderLayout.LINE_START);
    }
    // This method reads an Image object from a file indicated by
    // the string provided as the parameter.  The image is converted
    // here to a BufferedImage object, and that new object is the returned
    // value of this method.
    public BufferedImage readImage(String file) {

        Image image = Toolkit.getDefaultToolkit().getImage(file);
        MediaTracker tracker = new MediaTracker(new Component() {
        });
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        BufferedImage bim = new BufferedImage
                (image.getWidth(this), image.getHeight(this),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage(image, 0, 0, this);

        return bim;
    }
    public static void main(String[] argv) {

        JFrame frame = new UIView();
        frame.setPreferredSize(new Dimension(700,400));
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
    }
    /**********************************************
     * I'm trying to move this to a new class file but when I do that,
     * NOTHING WORKS
     * so it's chilling here for now
     **********************************************/
    public class ViewController extends JLabel {

        // instance variable to hold the buffered image
        public BufferedImage bim=null;
        public BufferedImage filteredbim=null;

        //  tell the paintcomponent method what to draw
        private boolean showfiltered=false;

        // Default constructor
        public ViewController() {
        }

        // This constructor stores a buffered image passed in as a parameter
        public ViewController(BufferedImage img) {
            bim = img;
            filteredbim = new BufferedImage
                    (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
            setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

            this.repaint();
        }
        // changes the image by resetting what is stored
        public void setImage(BufferedImage img) {
            if (img == null) return;
            bim = img;
            filteredbim = new BufferedImage
                    (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
            setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));
            showfiltered=false;
            this.repaint();
        }
        // accessor to get a handle to the bufferedimage object stored here
        public BufferedImage getImage() {
            return bim;
        }

        //  Preview the transformation of the image from right to left
        public void PreviewRL() {
            if (bim == null) return;
            showfiltered = true;
            this.repaint();
        }

        //  Preview the transformation of the image from left to right
        public void PreviewLR(BufferedImage img) {
            if (bim == null) return;
            // creating the temporary JFrame to preview the transformation
            JFrame frame = new JFrame("Preview");
            frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
            JPanel preview = new JPanel();
            preview.add(img);
            showfiltered = true;

            frame.add(preview);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        }

        //  show current image by a scheduled call to paint()
        public void showImage() {
            if (bim == null) return;
            showfiltered=false;
            this.repaint();
        }

        //  get a graphics context and show either filtered image or
        //  regular image
        public void paintComponent(Graphics g) {
            Graphics2D big = (Graphics2D) g;
            if (showfiltered) big.drawImage(filteredbim, 0, 0, this);
            else big.drawImage(bim, 0, 0, this);
        }
    }
}
