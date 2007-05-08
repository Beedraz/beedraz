package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.Iterator;


/**
 * <p>A derived logic circuit that represents the AND of many inputs.</p>
 * <p>Initially, the boolean value of an instance is <code>true</code>.
 *   The boolean value changes when inputs are added or removed, or when
 *   a input changes its boolean value.</p>
 *
 * <p><em>Note:</em> No invariant of the form:
 * <code>getValue() == (for each LogicCircuit lc in getInputs():
 * lc.getValue() == true);</code>
 * A public invariant of the form of this form is expected, but cannot
 * be provided. It would mean that we have to make sure that our value
 * is updated as soon as the value of our inputs is updated. If the
 * value of the inputs is updated, and we are not, this invariant
 * would be violated. This can be solved, but this is <strong>not done
 * in this project</strong>.</p>
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class And extends ManyInputs {

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


  /**
   * @param name
   *        A string used to identify this logic circuit.
   *
   * @pre | name != null;
   * @pre | ! name.equals("");
   * @post | new.getName().equals(name);
   * @post | new.getValue() == true;
   * @post | new.getValueChangedListeners().isEmpty();
   * @post | new.getDemoUpdateListeners().isEmpty();
   * @post | new.getDependentGates().isEmpty();
   * @post | new.getInputs().isEmpty();
   */
  public And(String name) {
    super(name, true);
  }

  final protected void localUpdate() {
    boolean result = true;
    Iterator iter = getInputs().iterator();
    while (result && iter.hasNext()) {
      LogicCircuit lc = (LogicCircuit)iter.next();
      result = lc.getValue();
    }
    setValue(result);
  }
  
  final protected void inputAdded(LogicCircuit input) {
    valueUpdateStarts(); // for demonstration purposes only
    setValue(getValue() && input.getValue());
    updateDependentGates();
    valueUpdateCompleted(); // for demonstration purposes only
  }
  
  final protected void inputRemoved(LogicCircuit input) {
    updateValue();
  }
  
}