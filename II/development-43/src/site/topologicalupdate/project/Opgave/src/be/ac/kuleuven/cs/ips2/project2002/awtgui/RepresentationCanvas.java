package be.ac.kuleuven.cs.ips2.project2002.awtgui;


import be.ac.kuleuven.cs.ips2.project2002.logicCircuits.LogicCircuit;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * <p>Instances of this class represent connected logic circuits.</p>
 *   The intended use follows 3 steps:</p>
 * <ul>
 *   <li>add the logic circuits to be represented</li>
 *   <li>close this instance; now the representations of the inputs
 *        connecting the adding logic circuits are created</li>
 *   <li>add this instance to an awt frame, pack and display it</li>
 * </ul>
 * <p>Changes to the configuration of logic circuits after the instance
 *   of this class is closed are not taken into account, and will
 *   probably result in disaster.</p>
 * <p><em><strong>Note:</strong> this class is only partially
 *   structured and formally specified.</em></p>
 *
 * @package
 * @invar | getLogicCircuitReprs() != null;
 * @invar | for each Object o in getLogicCircuitReprs(): o != null;
 * @invar | for each Object o in getLogicCircuitReprs():
 *            o instanceof LogicCircuitRepr;
 * @invar | getInputs() != null ==>
 *            for each Object o in getInputs(): o != null;
 * @invar | getInputs() != null ==>
 *            for each Object o in getInputs():
 *              o instanceof InputRepr;
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class RepresentationCanvas extends JPanel {

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


  /*<construction>*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /**
   * @post | new.getLogicCircuitReprs().isEmpty();
   * @post | new.getInputs().isEmpty();
   *
  public RepresentationCanvas();
   */

  /*</construction>*/



  /*<section name="Canvas">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /*<section name="Size">*/
  //-------------------------------------------------------------------

  /**
   * This method returns the minimum size of this awt.Canvas.
   * This is needed to have the Canvas display meaningfully in
   * an awt.Frame with most LayoutManagers.
   */
  public Dimension getMinimumSize() {
    return new Dimension(500, 500);
  }
  
  /**
   * This method returns the preferred size of this awt.Canvas.
   * This is needed to have the Canvas display meaningfully in
   * an awt.Frame with some LayoutManagers.
   */
  public Dimension getPreferredSize() {
    return new Dimension(500, 500);
  }

  /*</section>*/

  
  /*<section name="Painting">*/
  //-------------------------------------------------------------------

  /**
   * Paint <this>this</this> on the Graphics context given as argument.
   * This method first asks the representation of inputs to paint
   * themselves, connecting gates to the logic circuits they depend
   * on (i.e., use as input).
   * Then the logic circuits themselves are asked to paint themselves.
   */
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    Iterator inputs = $inputs.iterator();
    while (inputs.hasNext()) {
      ((InputRepr)inputs.next()).paint(g);
    }
    Iterator comps = $circuits.iterator();
    while (comps.hasNext()) {
      ((LogicCircuitRepr)comps.next()).paint(g);
    }
  }

  /*</section>*/
  
  /*</section>*/

  
  
  /*<section name="Represented Logic Circuits">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  Set getLogicCircuitReprs() {
    return (Set)$circuits.clone();
  }
  
  /**
   * Add a logic circuit to this representation to be represented on
   * this Canvas, on the position given as argument. A representation
   * for the circuit is generated, which will keep itself in sync with
   * changes in the circuit.
   *
   * @pre | lc != null;
   * @pre | position != null;
   * @excep IllegalStateException
   *        <this>this</this> is closed.
   */
  public void add(LogicCircuit lc, Dimension position)
      throws IllegalStateException {
    if ($inputs != null) { // this is closed
      throw new IllegalStateException("This representation is closed.");
    }    
    $circuits.add(new LogicCircuitRepr(lc, this, position));
  }

  /**
   * Close this representation. It is now ready to be shown in a frame.
   * No new gates can be added anymore.
   * This call generates representations for the inputs connecting the 
   * added logic circuits.
   */
  public void close() {
    $inputs = new HashSet();
    Iterator comps = $circuits.iterator();
    while (comps.hasNext()) {
      LogicCircuitRepr reprSource = (LogicCircuitRepr)comps.next();
      LogicCircuit lcSource = reprSource.getLogicCircuit();
      Iterator dependents = lcSource.getDependentGates().iterator();
      while (dependents.hasNext()) {
        LogicCircuit lcTarget = (LogicCircuit)dependents.next();
        LogicCircuitRepr reprTarget = getLogicCircuitReprOf(lcTarget);
        $inputs.add(new InputRepr(reprSource, reprTarget));
      }
    }
  }

  /**
   * Search for the logic circuit representation created for
   * <formal-arg>lc</formal-arg> by <this>this</this>. Return null if you
   * can't find one.
   */
  LogicCircuitRepr getLogicCircuitReprOf(LogicCircuit lc) {
    Iterator iter =  $circuits.iterator();
    while (iter.hasNext()) {
      LogicCircuitRepr repr = (LogicCircuitRepr)iter.next();
      if (repr.getLogicCircuit() == lc) {
        return repr;
      }
    }
    return null;        
  }

  /**
   * @invar | $circuits != null;
   * @invar | for each Object o in $circuits: o != null;
   * @invar | for each Object o in $circuits:
   *            o instanceof LogicCircuitRepr;
   */
  private HashSet $circuits = new HashSet();

  /*</section>*/
  


  /*<section name="Represented Inputs">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  Set getInputs() {
    try {
      return (Set)$inputs.clone();
    }
    catch (NullPointerException npExc) {
      return null;
    }
  }

  /**
   * @invar | $inputs != null ==>
   *            for each Object o in $inputs: o != null;
   * @invar | $inputs != null ==>
   *            for each Object o in $inputs:
   *              o instanceof InputRepr;
   */
  private HashSet $inputs;

  /*</section>*/



  /**
   * @pre | delay >= 0;
   */
  public void setDemoModeDelay(int delay) {
    $demoModeDelay = delay;
  }

  public int getDemoModeDelay() {
    if (! $inDemoMode) {
      return 0;
    }
    else {
      return $demoModeDelay;
    }
  }

  private int $demoModeDelay = 0;


  public void setInDemoMode(boolean inDemoMode) {
    $inDemoMode = inDemoMode;
    if ($inDemoMode) {
      $demoAnimationTimer.start();
    }
    else {
      if ($demoAnimationTimer.isRunning()) {
        // stop asap
        addDemoAnimationCommand($demoAnimationStopCommand);
      }
    }
  }

  public boolean isInDemoMode() {
    return $inDemoMode;
  }

  private boolean $inDemoMode = false;

  private MouseListener $flipper =
    new MouseInputAdapter() {
          public void mouseReleased(MouseEvent e) {
            if ($demoAnimationTimerActionListener.queueIsEmpty()) {
              Iterator comps = $circuits.iterator();
              while (comps.hasNext()) {
                LogicCircuitRepr lcr = (LogicCircuitRepr)comps.next();
                if (lcr.isUnder(e.getX(), e.getY())) {
                  lcr.mouseReleased();
                }
              }
            }
            // else let the animation finish first
          }
        };

  {
    addMouseListener($flipper);
  }
  
  final void addDemoAnimationCommand(DemoAnimationCommand dac) {
    $demoAnimationTimerActionListener.addDemoAnimationCommand(dac);
  }

  final void addDemoAnimationDelay() {
    int nrOfDelays = getDemoModeDelay() / _DEMO_ANIMATION_NOMINAL_DELAY;
    for (int i = 0; i < nrOfDelays; i++) {
      $demoAnimationTimerActionListener.
          addDemoAnimationCommand($demoAnimationDelayCommand);
    }
  }

  static final private int _DEMO_ANIMATION_NOMINAL_DELAY = 50; // ms

  private class DemoAnimationTimerActionListener implements ActionListener {
  
    public void actionPerformed(ActionEvent e) {
      try {
        DemoAnimationCommand dac =
          (DemoAnimationCommand)$demoAnimationQueue.getFirst();
            // throws NoSuchElementException
        $demoAnimationQueue.removeFirst();
        dac.perform();
        repaint();
      }
      catch (NoSuchElementException nseExc) {
        // NOP
      }
    }
    
    public boolean queueIsEmpty() {
      return $demoAnimationQueue.isEmpty();
    }
    
    void clearQueue() {
      $demoAnimationQueue.clear();
    }

    public void addDemoAnimationCommand(DemoAnimationCommand dac) {
      $demoAnimationQueue.addLast(dac);
    }

    private LinkedList $demoAnimationQueue =
      new LinkedList();
        // should be synchronized, but let's depend on the bearable slowness of flickering

  }
  
  private DemoAnimationTimerActionListener $demoAnimationTimerActionListener =
      new DemoAnimationTimerActionListener();
      
  abstract class DemoAnimationCommand {
  
    abstract public void perform();
    
  }
  
  private final DemoAnimationCommand $demoAnimationDelayCommand =
    new DemoAnimationCommand() {
          public void perform() {
            //NOP
          }
        };
 
  private final DemoAnimationCommand $demoAnimationStopCommand =
    new DemoAnimationCommand() {
          public void perform() {
            $demoAnimationTimerActionListener.clearQueue();
            $demoAnimationTimer.stop();
          }
        };

  
  private Timer $demoAnimationTimer =
      new Timer(_DEMO_ANIMATION_NOMINAL_DELAY,
                $demoAnimationTimerActionListener);

  {
    $demoAnimationTimer.setRepeats(true);
    $demoAnimationTimer.setCoalesce(false);
  }

}