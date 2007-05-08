package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.Set;
import java.util.HashSet;


/**
 * <p>This class adds support for {@link LogicGate}s with
 *   many inputs.</p>
 * <p>On initialization, the instance has an initial boolean value.
 *   On adding or removing inputs, the value changes.</p>
 * <p>When a input is added, <this>this</this> is registered with the input circuit as a
 *   dependent. {@link #updateValue()} will be called
 *   by the input circuit if its boolean value changes. On removal of the input,
 *   <this>this</this> is deregistered as dependent with the input circuit.</p>
 *
 * @invar | getInputs() != null;
 * @invar | for each Object o:
 *            getInputs().contains(o) ==> o != null;
 * @invar | for each Object o:
 *            getInputs().contains(o) ==>
 *               o instanceof LogicCircuit;
 * @invar | getFanIn() == getInputs().size();
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
abstract public class ManyInputs extends LogicGate {

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
   * @param initialValue
   *        The initial value for the boolean value of the new instance.
   *
   * @pre | name != null;
   * @pre | ! name.equals("");
   * @post | new.getName().equals(name);
   * @post | new.getValue() == initialValue;
   * @post | new.getValueChangedListeners().isEmpty();
   * @post | new.getDemoUpdateListeners().isEmpty();
   * @post | new.getDependentGates().isEmpty();
   * @post | new.getInputs().isEmpty();
   */
  public ManyInputs(String name, boolean initialValue) {
    super(name, initialValue);
  }
  
  final public Set getInputs() {
    return (Set)$inputs.clone();
  }
  
  final public int getFanIn() {
    return $inputs.size();
  }

  /**
   * <p><formal-arg>input</formal-arg> is added to the set of inputs.
   *   If <formal-arg>input</formal-arg> changes after this call, we will be
   *   notified.</p>
   *
   * @param input
   *        The logic circuit our boolean value depends upon from now on.
   * @pre    Don't create loops.
   *       |  ! this.isDependent(input);
   * @post   <formal-arg>input</formal-arg> is in the set of inputs.
   *       | (input != null) ==>
   *            new.getInputs().contains(input);
   * @post | input != null ==>
   *            {@link #inputAdded(LogicCircuit)} with
   *              <formal-arg>input</formal-arg>) as argument is called.
   * @post   input != null ==>
   *            If the boolean value of <formal-arg>input</formal-arg> changes,
   *              {@link #updateValue()} with
   *               <formal-arg>input</formal-arg>) as argument will be called.
   * @post   input != null ==>
   *            {@link #getValue()} will change to reflect the extra input.
   * @post listeners are warned ...
   * @post dependents are warned ...
   * @post DemoUpdateListeners are warned ...
   */
  final public void addInput(LogicCircuit input) {
    if (input != null) {
      $inputs.add(input);
      input.addDependentGate(this);
      inputAdded(input);
    }
  }
  
  /**
   * <p><formal-arg>input</formal-arg> is removed from the set of inputs.
   *   We will no longer be notified of changes in the boolean value of
   *   <formal-arg>input</formal-arg>.</p>
   *
   * @param dependent
   *        The object no longer to be notified of a change of our boolean value.
   * @param input
   *        The logic circuit our boolean value no longer depends on.
   *
   * @post   <formal-arg>input</formal-arg> is not in the set of inputs.
   *       | ! new.getInputs().contains(input);
   * @post | input != null ==>
   *            {@link #inputRemoved(LogicCircuit)} with
   *              <formal-arg>input</formal-arg>) as argument is called.
   * @post   input != null ==>
   *            {@link #updateValue()} will no longer be
   *              called if the boolean value of <formal-arg>input</formal-arg>
   *              changes.
   * @post   input != null ==>
   *            {@link #getValue()} will change to reflect the removal of
   *              <formal-arg>input</formal-arg>.
   * @post listeners are warned ...
   * @post dependents are warned ...
   * @post DemoUpdateListeners are warned ...
   */
  final public void removeInput(LogicCircuit input) {
    if (input != null) {
      $inputs.remove(input);
      input.removeDependentGate(this);
      inputRemoved(input);
    }
  }

  /**
   * <p>This method is called by {@link #addInput(LogicCircuit)} when
   *   <formal-arg>input</formal-arg> is added to the set of inputs. Subclasses
   *   should implement this method to update the boolean value of <this>this</this>
   *   to reflect the extra input.</p>
   *
   * @pre | input != null;
   * @post   {@link #getValue()} will change to reflect the extra input.
   * @post listeners are warned ...
   * @post DemoUpdateListeners are warned ...
   * @post dependents are warned ...
   */
  abstract protected void inputAdded(LogicCircuit input);
  
  /**
   * <p>This method is called by {@link #addInput(LogicCircuit)} when
   *   <formal-arg>input</formal-arg> is removed from the set of inputs.
   *   Subclasses should implement this method to update the boolean value
   *   of <this>this</this> to reflect the removal of
   *   <formal-arg>input</formal-arg>.</p>
   *
   * @pre | input != null;
   * @post   {@link #getValue()} will change to reflect the removal of
   *         <formal-arg>input</formal-arg>.
   * @post listeners are warned ...
   * @post DemoUpdateListeners are warned ...
   * @post dependents are warned ...
   */
  abstract protected void inputRemoved(LogicCircuit input);
  
  /**
   * @invar | $inputs != null;
   * @invar | for each Object o: $inputs.contains(o) ==> o != null;
   * @invar | for each Object o:
   *            $inputs.contains(o) ==> o instanceof LogicCircuit;
   */
  private HashSet $inputs = new HashSet();
  
  final protected void updateValue() {
    valueUpdateStarts(); // for demonstration purposes only
    localUpdate();
    updateDependentGates();
    valueUpdateCompleted(); // for demonstration purposes only
  }

  /**
   * <p>This method is to be called by {@link #updateValue} to do the actual
   *   recalculation of the boolean value.</p>
   *
   * @post Calculate whether the reported change influences the boolean
   *       value of <this>this</this>, and if it does, change the boolean value of
   *       this to its new value.
   */
  abstract void localUpdate();
  
}