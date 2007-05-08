package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * <p>A Bit is an autonomous {@link LogicCircuit} whose boolean
 *   value is under public control. The boolean value of an instance of
 *   this type does not depend on other {@link LogicCircuit}
 *   instances.</p>
 * <p>The boolean value is initially false, and can be changed by calling
 *   {@link #flip()}.</p>
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class Bit extends LogicCircuit {

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


  /**
   * @param name
   *        A string used to identify this logic circuit.
   * @pre | name != null;
   * @pre | ! name.equals("");
   * @post | new.getName().equals(name);
   * @post | new.getValue() == false;
   * @post | new.getValueChangedListeners().isEmpty();
   * @post | new.getDemoUpdateListeners().isEmpty();
   * @post | new.getDependentGates().isEmpty();
   */
  public Bit(String name) {
    super(name, false);
  }

  /**
   * Actively set the value of this LogicCircuit.
   *
   * @post | new.getValue() == ! getValue();
   * @post ValueChangedListeners are notified of the change.
   * @post Dependents are notified of the change.
   * @post Registered DemoUpdateListeners are notified of the start of this method.
   * @post Registered DemoUpdateListeners are notified of the end of this method.
   */
  final public void flip() {
    valueUpdateStarts(); // for demonstration purposes only
    setValue(! getValue());
    updateDependentGates();
    valueUpdateCompleted(); // for demonstration purposes only
  }
  
}