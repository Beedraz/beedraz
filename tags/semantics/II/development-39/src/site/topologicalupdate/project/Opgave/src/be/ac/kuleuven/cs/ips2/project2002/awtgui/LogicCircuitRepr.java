package be.ac.kuleuven.cs.ips2.project2002.awtgui;


import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.LogicCircuit;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.ValueChangedEvent;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.ValueChangedListener;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.DemoUpdateListener;
import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.Bit;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Set;
import java.util.Iterator;


/**
 * Instances of this type paint a representation of alogic circuit.
 * <p><em><strong>Note:</strong> this class is only partially
 *   structured and formally specified.</em></p>
 *
 * @invar | getLogicCircuit() != null;
 * @invar | getRepresentationCanvas() != null;
 * @invar | getPosition() != null;
 * @invar | getSize() != null;
 * @invar COLOR... constants != null;
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
class LogicCircuitRepr {

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
   * @pre | lc != null;
   * @pre | representationCanvas != null;
   * @pre | position != null;
   * @pre | source.getRepresentationCanvas() ==
   *          target.getRepresentationCanvas();
   * @post | new.getLogicCircuit() == lc;
   * @post | getValue() == lc.getValue();
   * @post We are registered with <formal-arg>lc</formal-arg> to
   *       get value changed events.
   * @post We are registered with <formal-arg>lc</formal-arg> as
   *       a demo update listener.
   * @post | new.getRepresentationCanvas() == representationCanvas;
   * @post | new.getPosition() == position;
   */
  public LogicCircuitRepr(LogicCircuit lc,
                            RepresentationCanvas representationCanvas,
                            Dimension position) {
    $logicCircuit = lc;
    initBuffers(lc);
    lc.addValueChangedListener($vcListener);
    lc.addDemoUpdateListener($duListener);
    $representationCanvas = representationCanvas;
    $position = (Dimension)position.clone();
  }

  /*</construction>*/



  /*<property name="LogicCircuit">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  public LogicCircuit getLogicCircuit() {
    return $logicCircuit;
  }


  /*<property name="Value Buffer">*/
  //-------------------------------------------------------------------

  /**
   * @return | getLogicCircuit().getValue();
   */
  public boolean getValue() {
    return $valueBuffer;
  }
  
  private boolean $valueBuffer;
  
  /*</property>*/


  /*<property name="Type Description">*/
  //-------------------------------------------------------------------

  /**
   * @result | result.equals(getSimpleClassName(getLogicCircuit()));
   */
  public String getTypeDescription() {
    return $typeDescriptionBuffer;
    // implementation ok if $logicCircuit remains immutable
  }
  
  /**
   * @invar | $typeDescriptionBuffer.equals(
   *                                    getSimpleClassName($logicCircuit));
   */
  private String $typeDescriptionBuffer;
  
  /*</property>*/


  /*<property name="Instance Description">*/
  //-------------------------------------------------------------------

  /**
   * @result | result.equals(getLogicCircuit().getName());
   */
  public String getName() {
    return $nameBuffer;
      // correct as long as the name of a logic circuit doesn't change
  }

  /**
   * @result | result.equals(getName() + ": "
   *              + new Boolean(getValue()).toString());
   */
  public String getInstanceDescription() {
    return $nameBuffer + ": " + new Boolean($valueBuffer).toString();
  }
  
  private String $nameBuffer;

  /*</property>*/


  /**
   * Set the value of the buffers based on the actual values in
   * <formal-arg>lc</formal-arg>.
   *
   * @post | new.getTypeDescription() = getSimpleClassName(lc);
   * @post | new.getName() = lc.getName();
   * @post | new.getValue() = lc.getValue();
   * @post | new.$bodyColor.equals(new.getValueColor());
   */
  private void initBuffers(LogicCircuit lc) {
    $typeDescriptionBuffer = getSimpleClassName(lc);
    $nameBuffer = lc.getName();
    $valueBuffer = lc.getValue();
    $bodyColor = getValueColor();
  }

  static private String getSimpleClassName(Object o) {
    return getSimpleName(o.getClass());
  }

  /**
   * @invar | $logicCircuit != null;
   */
  final private LogicCircuit $logicCircuit;

  /*</property>*/



  /*<property name="RepresentationCanvas">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  public RepresentationCanvas getRepresentationCanvas() {
    return $representationCanvas;
  }

  /**
   * @invar | $representationCanvas != null;
   */
  private RepresentationCanvas $representationCanvas;
  
  /*</property>*/


  
  /*<section name="Position & Size">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  public Dimension getPosition() {
    return $position;
  }
  
  public Dimension getSize() {
    return new Dimension(50, 60);
  }

  /**
   * @result | getCenter() = getPosition() + getSize()/2;
   */
  public Dimension getCenter() {
    int deltaX = getSize().width/2;
    int deltaY = getSize().height/2;
    return new Dimension(getPosition().width + deltaX, getPosition().height + deltaY);
  }
  
  public Dimension getTypeDescriptionOffset() {
    return new Dimension(3, 15);
  }
  
  public Dimension getTypeDescriptionPosition() {
    return new Dimension(getPosition().width + getTypeDescriptionOffset().width,
                          getPosition().height + getTypeDescriptionOffset().height);
  }
  
  public Dimension getInstanceDescriptionPosition() {
    return new Dimension(getPosition().width + getInstanceDescriptionOffset().width,
                          getPosition().height + getInstanceDescriptionOffset().height);
  }
  
  public Dimension getInstanceDescriptionOffset() {
    return new Dimension(3, 30);
  }

  /**
   * @invar | $position != null;
   */
  private Dimension $position;

  /*</section>*/
  


  
  /*<property name="Color">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  final static public Color COLOR_VALUE_TRUE = Color.green;
  final static public Color COLOR_VALUE_FALSE = Color.red;
  final static public Color COLOR_FLICKER = Color.white;

  /**
   * @return | getValue() ?
   *              getValueColor().equals(COLOR_VALUE_TRUE) :
   *              getValueColor().equals(COLOR_VALUE_FALSE);
   */
  public Color getValueColor() {
    return getValue() ? COLOR_VALUE_TRUE : COLOR_VALUE_FALSE;
  }
    
  final static public Color COLOR_BORDER_STABLE = Color.black;
  final static public Color COLOR_BORDER_UNSTABLE = Color.blue;

  private class DemoAnimationBorderFlickerCommand
      extends RepresentationCanvas.DemoAnimationCommand {
  
    public DemoAnimationBorderFlickerCommand() {
      this(COLOR_FLICKER);
    }

    public DemoAnimationBorderFlickerCommand(Color color) {
      getRepresentationCanvas().super();
      $color = color;
    }
    
    public void perform() {
      $borderColor = $color;
    }
    
    private Color $color;
    
  }
  
  private void generateBorderFlickerDemoAnimationCommands(Color targetColor) {
    Color initialColor = $borderColor;
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBorderFlickerCommand());
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBorderFlickerCommand(initialColor));
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBorderFlickerCommand());
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBorderFlickerCommand(initialColor));
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBorderFlickerCommand());
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBorderFlickerCommand(targetColor));
    getRepresentationCanvas().addDemoAnimationDelay();
  }

  /**
   * @invar | $borderColor != null;
   */
  private Color $borderColor = COLOR_BORDER_STABLE;


  final private int $borderThickness = 3;

  private void generateBodyFlickerDemoAnimationCommands(boolean newValue) {
    Color initialColor = $bodyColor;
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBodyFlickerCommand());
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBodyFlickerCommand(initialColor));
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBodyFlickerCommand());
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBodyFlickerCommand(initialColor));
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationBodyFlickerCommand());
    getRepresentationCanvas().addDemoAnimationCommand(
        new DemoAnimationSetValueCommand(newValue));
    getRepresentationCanvas().addDemoAnimationDelay();
  }
  
  
  private class DemoAnimationBodyFlickerCommand
      extends RepresentationCanvas.DemoAnimationCommand {
  
    public DemoAnimationBodyFlickerCommand() {
      this(COLOR_FLICKER);
    }

    public DemoAnimationBodyFlickerCommand(Color color) {
      getRepresentationCanvas().super();
      $color = color;
    }
    
    public void perform() {
      $bodyColor = $color;
    }
    
    private Color $color;
    
  }
  
  private class DemoAnimationSetValueCommand
      extends RepresentationCanvas.DemoAnimationCommand {
  
    public DemoAnimationSetValueCommand(boolean newValue) {
      getRepresentationCanvas().super();
      $newValue = newValue;
    }
    
    public void perform() {
      $valueBuffer = $newValue;
      $bodyColor = getValueColor();
    }
    
    private boolean $newValue;
    
  }
  
  /**
   * @invar | $bodyColor != null;
   */
  private Color $bodyColor;

  final static public Color COLOR_TEXT = Color.black;

  /**
   * @invar | $textColor != null;
   */
  final private Color $textColor = COLOR_TEXT;

  /*</property>*/



  /*<section name="Painting">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------
  
  public void paint(Graphics g) {
    g.setColor($bodyColor);
    g.fillRect(getPosition().width, getPosition().height,
                getSize().width, getSize().height);
    for (int i = 0; i < $borderThickness; i++) {
      g.setColor($borderColor);
      g.drawRect(getPosition().width - i, getPosition().height - i,
                  getSize().width + 2 * i, getSize().height + 2 * i);
    }
    g.setColor($textColor);
    g.drawString(getTypeDescription(),
                  getTypeDescriptionPosition().width,
                  getTypeDescriptionPosition().height);
    g.drawString(getInstanceDescription(),
                  getInstanceDescriptionPosition().width,
                  getInstanceDescriptionPosition().height);
  }

  private void repaintMe() {
    getRepresentationCanvas().repaint();
  }
  
  /*</section>*/
  
  private ValueChangedListener $vcListener =
      new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent vce) {
              if (getRepresentationCanvas().isInDemoMode()) {
                generateBodyFlickerDemoAnimationCommands(vce.getNewValue());
              }
              else {
                $valueBuffer = vce.getNewValue();
                $bodyColor = getValueColor();
                repaintMe();
              }
            }
          };
          
  private DemoUpdateListener $duListener =
      new DemoUpdateListener() {
            public void valueUpdateStarted(LogicCircuit lc) {
              if (getRepresentationCanvas().isInDemoMode()) {
                generateBorderFlickerDemoAnimationCommands(COLOR_BORDER_UNSTABLE);
              }
            }
            public void valueUpdateCompleted(LogicCircuit lc) {
              if (getRepresentationCanvas().isInDemoMode()) {
                generateBorderFlickerDemoAnimationCommands(COLOR_BORDER_STABLE);
              }
            }
          };

  boolean isUnder(int x, int y) {
    Dimension tl = getPosition();
    Dimension size = getSize();
    int xl = tl.width;
    int xr = xl + size.width;
    int yt = tl.height;
    int yb = yt + size.height;
    return (xl <= x) && (x <= xr) && (yt <= y) && (y <= yb); 
  }
  
  void mouseReleased() {
    try {
      ((Bit)getLogicCircuit()).flip();
    }
    catch (ClassCastException ccExc) {
      // NOP
    }
  }

  static private String getSimpleName(Class c) {
    String className = c.getName();
    int lastDot = className.lastIndexOf(".");
          // -1 means class in unnamed package
    return className.substring(lastDot + 1);
  }
  
  public String toString() {
    return getInstanceDescription();
  }
  
  

}