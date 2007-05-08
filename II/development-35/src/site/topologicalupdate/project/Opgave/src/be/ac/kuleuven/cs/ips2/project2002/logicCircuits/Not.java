package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


/**
 * A derived logic circuit, with 1 input, which represents the negation
 * of the input.
 *
 * <p><em>Note:</em> No invariant of the form:
 * <code>getValue() == ! getInput().getValue();</code>
 * A public invariant of the form of this form is expected, but cannot
 * be provided. It would mean that we have to make sure that our value
 * is updated as soon as the value of our inputs is updated. If the
 * value of the inputs is updated, and we are not, this invariant
 * would be violated. This can be solved, but this is <strong>not done
 * in this project</strong>.</p>
 *
 * @invar | getInput() != null;
 * @invar | getFanIn() == 1;
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class Not extends LogicGate {

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
   * @param input
   *        The input logic circuit this will represent the negation
   *        of.
   *
   * @pre | name != null;
   * @pre | ! name.equals("");
   * @pre | input != null;
   * @post | new.getValue() == ! input.getValue();
   * @post | new.getName().equals(name);
   * @post | new.getValueChangedListeners().isEmpty();
   * @post | new.getDemoUpdateListeners().isEmpty();
   * @post | new.getDependentGates().isEmpty();
   * @post | new.getInput() == input;
   */
  public Not(String name, LogicCircuit input) {
    super(name, !input.getValue());
    $input = input;
    input.addDependentGate(this);
  }
  
  final public int getFanIn() {
    return 1;
  }

  /**
   * @post | getValue() == ! getInput().getValue();
   */
  final protected void updateValue() {
    valueUpdateStarts(); // for demonstration purposes only
    setValue(! $input.getValue());
    updateDependentGates();
    valueUpdateCompleted(); // for demonstration purposes only
  }

  final public LogicCircuit getInput() {
    return $input;
  }
  
  /**
   * @pre | input != null;
   * @pre    Don't create loops.
   *       |  ! this.isDependent(input);
   * @post | getInput() == input;
   * @post listeners are warned ...
   * @post dependents are warned ...
   * @post DemoUpdateListeners are warned ...
   */
  final public void setInput(LogicCircuit input) {
    $input.removeDependentGate(this);
    $input = input;
    $input.addDependentGate(this);
    updateValue();
  }
  
  /**
   * @invar | $input != null;
   */
  private LogicCircuit $input;
  
}