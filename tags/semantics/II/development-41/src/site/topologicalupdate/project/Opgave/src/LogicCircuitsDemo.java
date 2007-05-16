import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.*;
import be.ac.kuleuven.cs.ips2.project2002.awtgui.*;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;
import javax.swing.JApplet;
import java.awt.Font;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;


/**
 * A demo for the logic circuits project. This should be usable with the
 * bad code, and with good code. This class can be used as an applet and
 * as an application.
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class LogicCircuitsDemo extends JApplet {

	/*<section name="Meta Information">*/
  //------------------------------------------------------------------
  //------------------------------------------------------------------

	static final public String CVS_SOURCE =
	    /*<indoc name="CVS Source">*/"$Source$"/*</indoc>*/;
	static final public String CVS_REVISION =
	    /*<indoc name="CVS Revision">*/"$Revision$"/*</indoc>*/;
	static final public String CVS_DATE =
	    /*<indoc name="CVS Date">*/"$Date$"/*</indoc>*/;
	static final public String CVS_STATE =
	    /*<indoc name="CVS State">*/"$State$"/*</indoc>*/;
	static final public String CVS_TAG =
	    /*<indoc name="CVS Tag">*/"$Name$"/*</indoc>*/;
	static final public String CVS_AUTHOR =
	    /*<indoc name="CVS Revision Author">*/"$Author: jand $"/*</indoc>*/;

  /*</section>*/


	public void init() {
		String laf = javax.swing.UIManager.getSystemLookAndFeelClassName();
		try {
			javax.swing.UIManager.setLookAndFeel(laf);
		} catch (javax.swing.UnsupportedLookAndFeelException exc) {
			System.err.println ("Warning: UnsupportedLookAndFeel: " + laf);
		} catch (Exception exc) {
			System.err.println ("Error loading " + laf + ": " + exc);
		}

    bit1 = new Bit("bit1");
    n1 = new Not("n1", bit1);
    n2 = new Not("n2", n1);
    a1 = new And("a1");
      a1.addInput(n1);
      a1.addInput(n2);
    o1 = new Or("o1");
      o1.addInput(n1);
      o1.addInput(n2);
    o2 = new Or("o2");
      o2.addInput(o1);
      o2.addInput(a1);
    bit2 = new Bit("bit2");
    n3 = new Not("n3", bit2);
    a2 = new And("a2");
      a2.addInput(o1);
      a2.addInput(n1);
      a2.addInput(n3);
	  
	  $representation = new RepresentationCanvas();
	  $representation.add(bit1, new Dimension(120, 20));
	  $representation.add(n1, new Dimension(120, 120));
	  $representation.add(n2, new Dimension(120, 220));
	  $representation.add(a1, new Dimension(20, 320));
	  $representation.add(o1, new Dimension(220, 320));
	  $representation.add(o2, new Dimension(120, 420));
	  $representation.add(bit2, new Dimension(420, 20));
	  $representation.add(n3, new Dimension(420, 120));
	  $representation.add(a2, new Dimension(420, 420));
	  $representation.close();
	  $representation.setFont(new Font("SansSerif", Font.PLAIN , 10));
	  
	  $representationCtrl = new RepresentationCanvasCtrl($representation);
	  
	  getContentPane().setLayout(new BorderLayout());
	  getContentPane().add($representation, BorderLayout.CENTER);
	  getContentPane().add($representationCtrl, BorderLayout.EAST);
  }

  public void start() {
	}
	
  public void destroy() {
    $representation = null;    
    a2 = null;
    n3 = null;
    bit2 = null;
    o1 = null;
    a1 = null;
    n2 = null;
    n1 = null;
    bit1 = null;
  }
  
	public void stop() {
	}

  private Bit bit1;
  private Not n1;
  private Not n2;
  private And a1;
  private Or o1;
  private Or o2;
  private Bit bit2;
  private Not n3;
  private And a2;
  
  private RepresentationCanvas $representation;
  private RepresentationCanvasCtrl $representationCtrl;
  
	public static void main(String args[]) {
	  _LCD = new LogicCircuitsDemo();
	  _LCD.init();
	  initFrame(_LCD);
	  _LCD.start();
	}

  public static void niam() {
	  _LCD.stop();
    _LCD.destroy();
    _LCD = null;
    destroyFrame();
    System.exit(0);
  }

	static private void initFrame(LogicCircuitsDemo lcd) {
	  _FRAME = new JFrame();
	  _FRAME.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	  _FRAME.addWindowListener($exitListener);
	  _FRAME.getContentPane().add(lcd, BorderLayout.CENTER);
	  _FRAME.setTitle("Logic Circuits Demo");
	  _FRAME.setResizable(true);
	  _FRAME.pack();
	  _FRAME.setVisible(true);
  }
  
  static private WindowListener $exitListener =
      new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              niam();
            }
          };
  
  static private void destroyFrame() {
    _FRAME.dispose();
    _FRAME = null;
  }

  static private LogicCircuitsDemo _LCD;
  static private JFrame _FRAME;
	
}
