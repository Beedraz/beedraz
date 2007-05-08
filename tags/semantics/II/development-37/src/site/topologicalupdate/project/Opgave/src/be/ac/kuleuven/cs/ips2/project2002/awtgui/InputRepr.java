package be.ac.kuleuven.cs.ips2.project2002.awtgui;


import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.LogicCircuit;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.ValueChangedEvent;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.ValueChangedListener;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.DemoUpdateListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Set;
import java.util.Iterator;


/**
 * Instances of this type paint a connection between 2 logic circuits, representing
 * inputs of a logic gate from another logic circuit.
 * <p><em><strong>Note:</strong> this class is only partially
 *   structured and formally specified.</em></p>
 *
 * @invar | getSourceRepr() != null;
 * @invar | getTargetRepr() != null;
 * @invar | getSource().getRepresentationCanvas() ==
 *            getTarget().getRepresentationCanvas();
 *
 * @version $Revision$
 * @author  Jan Dockx
 * @author  Guy Veraghtert
 * @author  Vincent Stephen-Ong
 */
class InputRepr {

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
	    /*<indoc name="CVS Revision Author">*/"$Author$"/*</indoc>*/;

  /*</section>*/


  /*<construction>*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /**
   * @pre | source != null;
   * @pre | target != null;
   * @pre | source.getRepresentationCanvas() ==
   *          target.getRepresentationCanvas();
   * @post | new.getSourceRepr() == source;
   * @post | new.getTargetRepr() == target;
   */
  public InputRepr(LogicCircuitRepr source, LogicCircuitRepr target) {
    $source = source;
    $target = target;
  }

  /*</construction>*/



  /*<property name="Source LogicCircuitRepr">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  LogicCircuitRepr getSourceRepr() {
    return $source;
  }

  private LogicCircuitRepr $source;
  
  /*</property>*/



  /*<property name="Target LogicCircuitRepr">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  LogicCircuitRepr getTargetRepr() {
    return $target;
  }
  
  private LogicCircuitRepr $target;

  /*</property>*/



  /*<property name="Representation Canvas">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /**
   * @result | getSource().getRepresentationCanvas();
   */
  RepresentationCanvas getRepresentationCanvas() {
    return $source.getRepresentationCanvas();
  }

  /*</property>*/



  private final static Color _COLOR_NORMAL = Color.black;

  /**
   * Paint this input representation. This is a line between the center
   * of getSource() and the center of getTarget().
   * If <this>this</this> is reacting, a different visual representation is choosen
   * then when it is not.
   */
  void paint(Graphics g) {
    Dimension sourceCenter = $source.getCenter();
    Dimension endCenter = $target.getCenter();
    g.setColor(_COLOR_NORMAL);
    g.drawLine(sourceCenter.width, sourceCenter.height,
                endCenter.width, endCenter.height);
    drawArrow(g,
              sourceCenter.width,
              sourceCenter.height,
              (endCenter.width+sourceCenter.width) / 2,
              (endCenter.height+sourceCenter.height) / 2,
              10,
              Math.PI / 8);
  }
  
  /**
   * Draws an arrow (on a given graphics) at the end of the line 
   * between two points of which the cošrdinates are given.
   * This code was adapted from
   * <a href="http://www.cs.mcgill.ca/~cs251/OldCourses/1997/topic30/applet/My/Tools.java">public
   * domain code</a> by <a href="mailto:zibalatz@cs.mcgill.ca">Vincent Stephen-Ong</a>.
   *
   * @param angle
   *        Total angle of the arrow head in radials.
   */
  private void drawArrow(Graphics g, int x1, int y1, int x2, int y2,
                          float size, double angle) {
    double dx = (double)(x1-x2);
    double dy = (double)(y1-y2);
    double length = Math.sqrt(dx * dx + dy * dy);
    double theta = Math.atan2(dx, dy);
    double nx = dx / length + size * Math.sin(theta);
    double ny = dy / length + size * Math.cos(theta);
    double cosAngle = Math.cos(angle);
    double sinAngle = Math.sin(angle);
    int a1x;
    int a1y;
    int a2x;
    int a2y;
    a1x = x2 + (int)( nx * cosAngle + ny * sinAngle);
    a1y = y2 + (int)(-nx * sinAngle + ny * cosAngle);
    a2x = x2 + (int)( nx * cosAngle - ny * sinAngle);
    a2y = y2 + (int)( nx * sinAngle + ny * cosAngle);
    g.drawLine(x2, y2, a1x, a1y);
    g.drawLine(x2, y2, a2x, a2y);
  }

  public String toString() {
    return getSourceRepr() + " -> " + getTargetRepr();
  }

}