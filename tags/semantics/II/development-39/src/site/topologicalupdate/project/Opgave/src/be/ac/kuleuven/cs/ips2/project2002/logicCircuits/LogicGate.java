package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


/**
 * <p>Instances of this type represent <def>derived logic circuits</def>.
 *   The boolean value of a logic gate depend on the boolean
 *   value of other logic circuits. As a consequence, logic gates
 *   need to be notified by the logic circuits they depend on
 *   when their boolean value changes, so that the logic gate
 *   can calculate the effect of that change on its own boolean value, and
 *   change it if necessary.</p>
 * <p>{@link LogicCircuit}s notify their dependent LogicGates
 *   by calling {@link #updateValue()} on them.</p>
 * <p>The references to the logic circuits <this>this</this> depends on, will be
 *   called <def>inputs</def>.</p>
 *
 * @invar | getFanIn() >= 0;
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
abstract public class LogicGate extends LogicCircuit {

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
   */
  public LogicGate(String name, boolean initialValue) {
    super(name, initialValue);
  }
  
  /**
   * The number of inputs of this gate.
   */
  abstract public int getFanIn();

  /**
   * <p>This method is to be called by {@link LogicCircuit}s <this>this</this>
   *   depends on when their boolean value changes.</p>
   *
   * @post Calculate whether the reported change influences the boolean
   *       value of <this>this</this>, and if it does, change the boolean value of
   *       this to its new value.
   * @post getValue() != new.getValue() ==> listeners are warned ...
   * @post getValue() != new.getValue() ==> dependents are warned ...
   * @post getValue() != new.getValue() ==> DemoUpdateListeners are warned ...
   */
  abstract void updateValue();

}