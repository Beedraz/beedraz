package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.EventObject;


/**
 * Instances of this type carry information about a change in the boolean
 * value of a {@link LogicCircuit} (see {@link LogicCircuit#getValue()}).
 *
 * @invar | getSource() != null;
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class ValueChangedEvent extends EventObject {

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
   * @param source
   *        The logic circuit that sent this event.
   * @param newValue
   *        The new value of <forma-arg>source</formal-arg>.
   *
   * @pre | source != null;
   * @post | new.getSource() == source;
   * @post | new.getNewValue() == newValue;
   */
  public ValueChangedEvent(LogicCircuit source, boolean newValue) {
    super(source);
    $newValue = newValue;
  }

  /*</construction>*/



  /*<property name="Source">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------
  
  /**
   * @result | getSource();
   */
  final public LogicCircuit getLogicCircuitSource() {
    return (LogicCircuit)getSource();
  }
  
  /*</property>*/

  

  /*<property name="Old Value">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /**
   * @result | ! getNewValue();
   */
  final public boolean getOldValue() {
    return ! getNewValue();
  }

  /*</property>*/
 
  

  /*<property name="New Value">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  final public boolean getNewValue() {
    return $newValue;
  }
  
  private boolean $newValue;
  
  /*</property>*/
  
  
  
  public String toString() {
    return "ValueChangedEvent from \"" + getSource()
        + "\", new value: "+ getNewValue();
  }

}