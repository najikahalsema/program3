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

   // This frame will hold the primary components:
   // 	An object to hold the buffered image and its associated operations
   //	Components to control the interface

   // Instance variables
   private BufferedImage image;   // the image
   private MyImageObj view;       // a component in which to display an image
   private JButton PreviewButtonRL;    // Button to trigger blur operator
   private JButton PreviewButtonLR;    // Button to trigger edge detect operator
   private JButton ResetButton;// Button to restore original image
   private int x, y;              // Store x, y mouse position for paint
   private boolean firstdrag=true;// Flag to toggle draw mode
   private JLabel FpsLabel;   // Label for threshold slider
   private JSlider FpsSlider;

   // Constructor for the frame
   public UIView () {

      super();				// call JFrame constructor

      this.buildMenus();		// helper method to build menus
      this.buildComponents();		// helper method to set up components
      this.buildDisplay();		// Lay out the components on the display
   }

   //  Builds the menus to be attached to this JFrame object
   //  Primary side effect:  menus are added via the setJMenuBar call
   //  		Action listeners for the menu items are anonymous inner
   //		classes here
   //  This helper method is called once by the constructor

   private void buildMenus () {

      final JFileChooser fc = new JFileChooser(".");
      JMenuBar bar = new JMenuBar();
      this.setJMenuBar (bar);
      JMenu fileMenu = new JMenu ("File");
      JMenuItem fileopen = new JMenuItem ("Open");
      JMenuItem fileexit = new JMenuItem ("Exit");

      fileopen.addActionListener(
	 new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                int returnVal = fc.showOpenDialog(UIView.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                   File file = fc.getSelectedFile();
                   try {
                     image = ImageIO.read(file);
                   } catch (IOException e1){};

		   view.setImage(image);
		   view.showImage();
                }
             }
         }
      );
      fileexit.addActionListener(
	 new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                   System.exit(0);
             }
         }
      );

      fileMenu.add(fileopen);
      fileMenu.add(fileexit);
      bar.add(fileMenu);
   }

   //  Allocate components (these are all instance vars of this frame object)
   //  and set up action listeners for each of them 
   //  This is called once by the constructor

   private void buildComponents() {

      // Create components to in which to display image and GUI controls
      // reads a default image
      view = new MyImageObj(readImage("3.jpg"));
      //view = new MyImageObj();
      ResetButton = new JButton("Reset");
      PreviewButtonRL = new JButton("Preview R --> L");
      PreviewButtonLR = new JButton("Preview L --> R");
      FpsLabel = new JLabel("Frames per second: 30");
      FpsSlider = new JSlider( SwingConstants.VERTICAL, 1, 60, 30);
      FpsSlider.setMajorTickSpacing(5);
      FpsSlider.setPaintTicks(true);

      // slider event triggers a display of thresholded image
      FpsSlider.addChangeListener(
         new ChangeListener() {
	    public void stateChanged (ChangeEvent e) {
	       FpsLabel.setText("Frames per Second: " +
                       Integer.toString(FpsSlider.getValue()));
            }
         }
      );

      // Listen for mouse events to allow painting on image
      view.addMouseMotionListener(
         new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
	       Graphics g = view.getGraphics();
	       g.setColor (Color.white);
	       if (firstdrag) {
		  x = event.getX();  y = event.getY();
		  firstdrag = false;
               }
               else {
	          view.showImage();
                  x=event.getX();
                  y=event.getY();
                  int w=view.getImage().getWidth();
	          int h=view.getImage().getHeight();
                  g.fillOval (x-5, y-5, 10, 10);
                  g.drawLine (0,0, x, y);
                  g.drawLine (0,h, x, y);
                  g.drawLine (w,h, x, y);
                  g.drawLine (w,0, x, y);
               }
            }
         }
      );

      // Listen for mouse release to detect when we've stopped painting
      view.addMouseListener(
         new MouseAdapter() {
            public void mouseReleased(MouseEvent event) {
	       Graphics g = view.getGraphics();
	       firstdrag = true;
               x=event.getX();
               y=event.getY();
               int w=view.getImage().getWidth();
	       int h=view.getImage().getHeight();
               g.fillOval (x-5, y-5, 10, 10);
               g.drawLine (0,0, x, y);
               g.drawLine (0,h, x, y);
               g.drawLine (w,h, x, y);
               g.drawLine (w,0, x, y);

	
            }
         }
      );

      // Button listeners activate the buffered image object in order
      // to display appropriate function
      ResetButton.addActionListener(
	 new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                view.showImage();
             }
         }
      );
      PreviewButtonRL.addActionListener(
	 new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                view.PreviewRL();
             }
         }
      );
      PreviewButtonLR.addActionListener(
	 new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                view.PreviewLR();
                view.repaint();
             }
         }
      );
   }

   // This helper method adds all components to the content pane of the
   // JFrame object.  Specific layout of components is controlled here

   private void buildDisplay () {

      // Build first JPanel
      JPanel controlPanel = new JPanel();
      GridLayout grid = new GridLayout (1, 5);
      controlPanel.setLayout(grid);
      controlPanel.add(ResetButton);
      controlPanel.add(PreviewButtonRL);
      controlPanel.add(PreviewButtonLR);

      // Build second JPanel
      JPanel FpsControl = new JPanel();
      BorderLayout layout = new BorderLayout (5, 5);
      FpsControl.setLayout (layout);
      FpsControl.add(FpsLabel,BorderLayout.NORTH);
      FpsControl.add(FpsSlider,BorderLayout.CENTER);

      // Add panels and image data component to the JFrame
      Container c = this.getContentPane();
      c.add(view, BorderLayout.EAST);
      c.add(controlPanel, BorderLayout.SOUTH);
      c.add(FpsControl, BorderLayout.WEST);
   }

   // This method reads an Image object from a file indicated by
   // the string provided as the parameter.  The image is converted
   // here to a BufferedImage object, and that new object is the returned
   // value of this method.
   // The mediatracker in this method can throw an exception

   public BufferedImage readImage (String file) {

      Image image = Toolkit.getDefaultToolkit().getImage(file);
      MediaTracker tracker = new MediaTracker (new Component () {});
      tracker.addImage(image, 0);
      try { tracker.waitForID (0); }
      catch (InterruptedException e) {}
         BufferedImage bim = new BufferedImage 
             (image.getWidth(this), image.getHeight(this), 
             BufferedImage.TYPE_INT_RGB);
      Graphics2D big = bim.createGraphics();
      big.drawImage (image, 0, 0, this);
      return bim;
   }

   // The main method allocates the "window" and makes it visible.
   // The windowclosing event is handled by an anonymous inner (adapter)
   // class
   // Command line arguments are ignored.

   public static void main(String[] argv) {

      JFrame frame = new UIView();
      frame.pack();
      frame.setVisible(true);
      frame.addWindowListener (
	 new WindowAdapter () {
	    public void windowClosing ( WindowEvent e) {
		System.exit(0);
	    }
         }
      );
   }

   /*****************************************************************

   This is a helper object, which could reside in its own file, that 
   extends JLabel so that it can hold a BufferedImage

   I've added the ability to apply image processing operators to the
   image and display the result

   ***************************************************************************/

   public class MyImageObj extends JLabel {

      // instance variable to hold the buffered image
      private BufferedImage bim=null;
      private BufferedImage filteredbim=null;

      //  tell the paintcomponent method what to draw 
      private boolean showfiltered=false;

      // Default constructor
      public MyImageObj() {
      }

      // This constructor stores a buffered image passed in as a parameter
      public MyImageObj(BufferedImage img) {
	 bim = img;
         filteredbim = new BufferedImage 
            (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
         setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

         this.repaint();
      }

      // This mutator changes the image by resetting what is stored
      // The input parameter img is the new image;  it gets stored as an instance var
       // Keeping this function just in case we need to use it later
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

      //  apply the blur operator
      public void PreviewRL() {
         if (bim == null) return;
         showfiltered=true;
         this.repaint();
      }

      //  apply the detect-edge operator
      public void PreviewLR() {
         if (bim == null) return;
         showfiltered=true;
         this.repaint();
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
